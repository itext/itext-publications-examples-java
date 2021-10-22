package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TextRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class CreateTOC {
    public static final String DEST = "./target/sandbox/events/create_toc.pdf";

    private static List<AbstractMap.SimpleEntry<String, Integer>> toc = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CreateTOC().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws FileNotFoundException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        for (int i = 0; i < 10; i++) {
            String title = "This is title " + i;
            Text text = new Text(title).setFontSize(16);
            text.setNextRenderer(new TOCTextRenderer(text));
            doc.add(new Paragraph(text));
            for (int j = 0; j < 50; j++) {
                doc.add(new Paragraph("Line " + j + " of title " + i));
            }
        }

        doc.add(new AreaBreak());

        // Create a table of contents
        doc.add(new Paragraph("Table of Contents").setFontSize(16));
        for (AbstractMap.SimpleEntry<String, Integer> entry : toc) {
            Paragraph p = new Paragraph(entry.getKey());
            p.addTabStops(new TabStop(750, TabAlignment.RIGHT, new DottedLine()));
            p.add(new Tab());
            p.add(String.valueOf(entry.getValue()));
            doc.add(p);
        }

        doc.close();
    }


    private static class TOCTextRenderer extends TextRenderer {
        public TOCTextRenderer(Text modelElement) {
            super(modelElement);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new TOCTextRenderer((Text) modelElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            String title = ((Text) modelElement).getText();

            int pageNumber = getOccupiedArea().getPageNumber();
            toc.add(new AbstractMap.SimpleEntry(title, pageNumber));
        }
    }
}
