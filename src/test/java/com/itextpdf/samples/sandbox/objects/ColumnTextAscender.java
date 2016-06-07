/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/27906725/itext-placement-of-phrase-within-columntext
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class ColumnTextAscender extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/column_text_ascender.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColumnTextAscender().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Rectangle[] areas = new Rectangle[]{new Rectangle(50, 750, 200, 50), new Rectangle(300, 750, 200, 50)};
        // for canvas usage one should create a page
        pdfDoc.addNewPage();
        for (Rectangle rect : areas) {
            new PdfCanvas(pdfDoc.getFirstPage()).setLineWidth(0.5f).setStrokeColor(Color.RED).rectangle(rect).stroke();
        }
        doc.setRenderer(new ColumnDocumentRenderer(doc, areas));
        addColumn(doc, false);
        addColumn(doc, true);
        doc.close();
    }

    public void addColumn(Document doc, boolean useAscender) {
        Paragraph p = new Paragraph("This text is added at the top of the column.");
        // No setUseAscender(boolean). We can change leading instead. This is a bit different, but
        // for now we do not see the need to implement this method in iText7
        if (useAscender) {
            p.setMargin(0);
            p.setMultipliedLeading(1);
        }

        doc.add(p);
    }
}
