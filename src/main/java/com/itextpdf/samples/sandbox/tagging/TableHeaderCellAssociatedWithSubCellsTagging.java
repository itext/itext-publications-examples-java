package com.itextpdf.samples.sandbox.tagging;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNameTree;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.tagging.PdfStructElem;
import com.itextpdf.kernel.pdf.tagging.PdfStructureAttributes;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.pdf.tagutils.TagStructureContext;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;
import com.itextpdf.kernel.pdf.tagutils.WaitingTagsManager;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
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

        PdfName idTreeName = new PdfName("IDTree");
        PdfNameTree idTree = new PdfNameTree(pdfDocument.getCatalog(), idTreeName);
        for (int i = 0; i < 3; ++i) {
            Cell c = new Cell().add(new Paragraph("Header " + (i + 1)));
            c.getAccessibilityProperties().setRole(StandardRoles.TH);
            table.addHeaderCell(c);
            PdfString headerId = headersId[i];

            // Use custom renderer for cell header element in order to add ID to its tag
            CellRenderer renderer = new StructIdCellRenderer(c, doc, headerId, idTree);
            c.setNextRenderer(renderer);
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

        pdfDocument.getStructTreeRoot().getPdfObject().put(idTreeName, idTree.buildTree().makeIndirect(pdfDocument));

        doc.close();
    }

    private static class StructIdCellRenderer extends CellRenderer {
        private final PdfDocument pdfDocument;
        private final PdfNameTree idTree;
        private final TagStructureContext tagContext;
        private final PdfString headerId;

        public StructIdCellRenderer(Cell c, Document document, PdfString headerId, PdfNameTree idTree) {
            super(c);
            this.pdfDocument = document.getPdfDocument();
            this.idTree = idTree;
            this.tagContext = pdfDocument.getTagStructureContext();
            this.headerId = headerId;
        }

        @Override
        public void draw(DrawContext drawContext) {
            LayoutTaggingHelper taggingHelper = getProperty(Property.TAGGING_HELPER);

            // We want to reach the actual tag from logical structure tree, in order to set custom properties, for
            // which iText doesn't provide convenient API at the moment. Specifically we are aiming at setting /ID
            // entry in structure element dictionary corresponding to the table header cell. Here we are creating tag
            // for the current element in logical structure tree right at the beginning of #draw method.
            // If this particular instance of header cell is paging artifact it would be marked so by layouting
            // engine and it would not allow to create a tag (return value of the method would be 'false').
            // If this particular instance of header cell is the header which is to be tagged, a tag will be created.
            // It's safe to create a tag at this moment, it will be picked up and placed at correct position in the
            // logical structure tree later by layout engine.

            TagTreePointer p = new TagTreePointer(pdfDocument);
            if (taggingHelper.createTag(this, p)) {

                // After the tag is created, we can fetch low level entity PdfStructElem
                // in order to work with it directly. These changes would be directly reflected
                // in the PDF file inner structure.
                PdfStructElem structElem = tagContext.getPointerStructElem(p);
                PdfDictionary structElemDict = structElem.getPdfObject();
                structElemDict.put(PdfName.ID, headerId);
                idTree.addEntry(headerId.getValue(), structElemDict);
            }

            super.draw(drawContext);
        }
    }
}
