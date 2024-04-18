package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TextRenderer;

import java.io.*;

public class Every25Words {
    public static final String DEST = "./target/sandbox/events/every25words.pdf";

    public static final String SRC = "./src/main/resources/txt/liber1_1_la.txt";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Every25Words().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        String[] words = readFile(SRC).split("\\s+");
        Paragraph paragraph = new Paragraph();
        Text text = null;
        int i = 0;
        for (String word : words) {
            if (text != null) {
                paragraph.add(" ");
            }

            text = new Text(word);
            text.setNextRenderer(new Word25TextRenderer(text, ++i));
            paragraph.add(text);
        }

        doc.add(paragraph);

        doc.close();
    }

    private static String readFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"))) {
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
        }

        return sb.toString();
    }


    private static class Word25TextRenderer extends TextRenderer {
        private int count = 0;

        public Word25TextRenderer(Text textElement, int count) {
            super(textElement);
            this.count = count;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new Word25TextRenderer((Text) modelElement, count);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);

            // Draws a line to delimit the text every 25 words
            if (0 == count % 25) {
                Rectangle textRect = getOccupiedAreaBBox();
                int pageNumber = getOccupiedArea().getPageNumber();
                PdfCanvas canvas = drawContext.getCanvas();
                Rectangle pageRect = drawContext.getDocument().getPage(pageNumber).getPageSize();
                canvas
                        .saveState()
                        .setLineDash(5, 5)
                        .moveTo(pageRect.getLeft(), textRect.getBottom())
                        .lineTo(textRect.getRight(), textRect.getBottom())
                        .lineTo(textRect.getRight(), textRect.getTop())
                        .lineTo(pageRect.getRight(), textRect.getTop())
                        .stroke()
                        .restoreState();
            }
        }
    }
}
