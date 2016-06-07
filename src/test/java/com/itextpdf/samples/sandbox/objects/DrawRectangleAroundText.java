/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/29037981/how-to-draw-a-rectangle-around-multiline-text
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class DrawRectangleAroundText extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/draw_rectangle_around_text.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new DrawRectangleAroundText().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        Paragraph p = new Paragraph("This is a long paragraph that doesn't"
                + "fit the width we defined for the simple column of the"
                + "ColumnText object, so it will be distributed over several"
                + "lines (and we don't know in advance how many).");

        new Canvas(canvas, pdfDoc, new Rectangle(120, 500, 130, 280))
                .add(p);
        canvas.rectangle(120, 500, 130, 280);
        canvas.stroke();

        // In the lines below the comment we try to reproduce the itext5 method to achieve the result
        // However it's much more simple to use the next line
        // p.setBorder(new SolidBorder(1));
        // Or you can implement your own ParagraphRenderer and change the behaviour of drawBorder(DrawContext)
        // or draw(DrawContext)
        ParagraphRenderer renderer = (ParagraphRenderer) p.createRendererSubTree().setParent(doc.getRenderer());
        float height = renderer.layout(new LayoutContext(new LayoutArea(0, new Rectangle(300, 500, 130, 280)))).getOccupiedArea().getBBox().getHeight();

        new Canvas(canvas, pdfDoc, new Rectangle(300, 500, 130, 280))
                .add(p);
        canvas.rectangle(300, 500 + 280 - height, 130, height);
        canvas.stroke();

        doc.close();
    }
}
