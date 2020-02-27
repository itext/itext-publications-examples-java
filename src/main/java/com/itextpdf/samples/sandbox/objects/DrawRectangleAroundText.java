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

import java.io.File;
import java.io.IOException;

public class DrawRectangleAroundText {
    public static final String DEST = "./target/sandbox/objects/draw_rectangle_around_text.pdf";
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new DrawRectangleAroundText().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        Paragraph p = new Paragraph("This is a long paragraph that doesn't"
                + "fit the width we defined for the simple column of the"
                + "ColumnText object, so it will be distributed over several"
                + "lines (and we don't know in advance how many).");

        Rectangle firstRect = new Rectangle(120, 500, 130, 280);
        new Canvas(canvas, firstRect)
                .add(p);
        canvas.rectangle(firstRect);
        canvas.stroke();

        // In the lines below the comment we try to reproduce the iText5 method to achieve the result
        // However it's much more simple to use the next line
        // p.setBorder(new SolidBorder(1));
        // Or you can implement your own ParagraphRenderer and change the behaviour of drawBorder(DrawContext)
        // or draw(DrawContext)
        Rectangle secRect = new Rectangle(300, 500, 130, 280);
        ParagraphRenderer renderer = (ParagraphRenderer) p.createRendererSubTree().setParent(doc.getRenderer());
        float height = renderer.layout(new LayoutContext(new LayoutArea(0, secRect)))
                .getOccupiedArea().getBBox().getHeight();

        new Canvas(canvas, secRect)
                .add(p);
        canvas.rectangle(secRect.getX(), secRect.getY() + secRect.getHeight() - height, secRect.getWidth(), height);
        canvas.stroke();

        doc.close();
    }
}
