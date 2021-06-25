package com.itextpdf.samples.sandbox.layout;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TwoColumnParagraphLayout {

    public static final String DEST = "./target/sandbox/layout/complexElementLayout.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TwoColumnParagraphLayout().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));

        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            text.append("A very long text is here...");
        }

        Paragraph twoColumnParagraph = new Paragraph();

        // Set the custom renderer to create a layout consisted of two columns
        twoColumnParagraph.setNextRenderer(new TwoColumnParagraphRenderer(twoColumnParagraph));
        twoColumnParagraph.add(text.toString());
        doc.add(twoColumnParagraph);

        doc.add(new Paragraph("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));

        doc.close();
    }

    private static class TwoColumnParagraphRenderer extends ParagraphRenderer {

        public TwoColumnParagraphRenderer(Paragraph modelElement) {
            super(modelElement);
        }

        @Override
        public List<Rectangle> initElementAreas(LayoutArea area) {
            List<Rectangle> areas = new ArrayList<>();
            Rectangle firstArea = area.getBBox().clone();
            Rectangle secondArea = area.getBBox().clone();

            firstArea.setWidth(firstArea.getWidth() / 2 - 5);
            secondArea.setX(secondArea.getX() + secondArea.getWidth() / 2 + 5);
            secondArea.setWidth(firstArea.getWidth());

            areas.add(firstArea);
            areas.add(secondArea);
            return areas;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new TwoColumnParagraphRenderer((Paragraph) modelElement);
        }
    }

}
