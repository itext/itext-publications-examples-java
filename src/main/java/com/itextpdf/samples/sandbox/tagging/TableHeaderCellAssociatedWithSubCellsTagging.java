/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.tagging;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.tagging.PdfStructureAttributes;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;
import com.itextpdf.kernel.pdf.tagutils.WaitingTagsManager;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.tagging.LayoutTaggingHelper;
import com.itextpdf.layout.tagging.TaggingHintKey;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class TableHeaderCellAssociatedWithSubCellsTagging {
    public static final String DEST =
            "./target/sandbox/tagging/TableHeaderCellAssociatedWithSubCellsTagging.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TableHeaderCellAssociatedWithSubCellsTagging().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        pdfDocument.setTagged();

        Document doc = new Document(pdfDocument);

        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();

        // Initialize ID strings beforehand. Every ID should be unique across the document
        PdfString[] headersId = new PdfString[] {

                // Since '/ID' is a `byte string` according to specification we are not passing
                // encoding to the constructor of the PdfString
                new PdfString("header_id_0"),
                new PdfString("header_id_1"),
                new PdfString("header_id_2")
        };

        for (int i = 0; i < 3; ++i) {
            Cell c = new Cell().add(new Paragraph("Header " + (i + 1)));
            AccessibilityProperties ap = c.getAccessibilityProperties();
            ap.setRole(StandardRoles.TH)
                    .setStructureElementId(headersId[i].getValueBytes());
            table.addHeaderCell(c);
        }

        ArrayList<TaggingHintKey> colSpanHints = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Cell c;
            if (i < 3)
                c = new Cell().add(new Paragraph(String.valueOf((i + 1))));

            // Colspan creation
            else
                c = new Cell(1, 3).add(new Paragraph(String.valueOf((i + 1))));

            if (i < 3) {

                // Correct table tagging requires marking which headers correspond to the given cell.
                // The correspondence is defined by header cells tags IDs. For table cells without
                // col/row spans it's easy to reference a header: just add proper
                // PdfStructureAttributes to it. Table cells with col spans are processed below.
                PdfStructureAttributes tableAttributes = new PdfStructureAttributes("Table");
                PdfArray headers;
                headers = new PdfArray(headersId[i % headersId.length]);
                tableAttributes.getPdfObject().put(PdfName.Headers, headers);
                c.getAccessibilityProperties().addAttributes(tableAttributes);
            } else {

                // When we add PdfStructureAttributes to the element these attributes override any
                // attributes generated for it automatically. E.g. cells with colspan require properly
                // generated attributes which describe the colspan (e.g. which columns this cell spans).
                // So here we will use a different approach: fetch the tag which will be created for
                // the cell and modify attributes object directly.
                TaggingHintKey colSpanCellHint = LayoutTaggingHelper.getOrCreateHintKey(c);
                colSpanHints.add(colSpanCellHint);
            }

            table.addCell(c);
        }

        doc.add(table);

        // After table has been drawn on the page, we can modify the colspan cells tags
        for (TaggingHintKey colSpanHint : colSpanHints) {
            WaitingTagsManager waitingTagsManager = pdfDocument.getTagStructureContext().getWaitingTagsManager();
            TagTreePointer p = new TagTreePointer(pdfDocument);

            // Move tag pointer to the colspan cell using its hint
            if (!waitingTagsManager.tryMovePointerToWaitingTag(p, colSpanHint)) {

                // It is not expected to happen ever if immediate-flush is enabled (which is by default),
                // otherwise this should be done after the flush
                throw new IllegalStateException("A work-around does not work. A tag for the cell is "
                        + "not created or cannot be found.");
            }

            for (PdfStructureAttributes attr : p.getProperties().getAttributesList()) {
                if ("Table".equals(attr.getAttributeAsEnum("O"))) {

                    // Specify all the headers for the column spanning (all of 3)
                    PdfArray headers = new PdfArray(Arrays.asList(headersId));
                    attr.getPdfObject().put(PdfName.Headers, headers);
                    break;
                }
            }

        }

        doc.close();
    }
}
