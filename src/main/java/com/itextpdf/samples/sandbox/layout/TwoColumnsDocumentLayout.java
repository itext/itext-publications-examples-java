package com.itextpdf.samples.sandbox.layout;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

/**
 * Example demonstrates how to build complex layouts using layout manager
 */
public class TwoColumnsDocumentLayout {

    public static final String DEST = "./target/sandbox/layout/complexDocumentLayout.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TwoColumnsDocumentLayout().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Rectangle[] columns = {
                new Rectangle(100, 100, 100, 500),
                new Rectangle(400, 100, 100, 500)
        };

        // Set a renderer to create a layout consisted of two vertical rectangles created above
        doc.setRenderer(new ColumnDocumentRenderer(doc, columns));

        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            text.append("A very long text is here...");
        }

        Paragraph paragraph = new Paragraph(text.toString());
        doc.add(paragraph);

        doc.close();
    }

}
