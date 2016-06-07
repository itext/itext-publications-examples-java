/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/28709603/draw-a-line-every-n-words-using-itextsharp
 */
package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TextRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.*;

@Category(SampleTest.class)
public class Every25Words extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/events/every25words.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Every25Words().manipulatePdf(DEST);
    }

    public String readFile() throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream("./src/test/resources/txt/liber1_1_la.txt"), "UTF8"));
        String str;
        while ((str = in.readLine()) != null) {
            sb.append(str);
        }
        return sb.toString();
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        String[] words = readFile().split("\\s+");
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


    private class Word25TextRenderer extends TextRenderer {
        private int count = 0;

        public Word25TextRenderer(Text textElement, int count) {
            super(textElement);
            this.count = count;
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            if (0 == count % 25) {
                Rectangle rect = getOccupiedAreaBBox();
                int pageNumber = getOccupiedArea().getPageNumber();
                PdfCanvas canvas = drawContext.getCanvas();
                canvas.saveState()
                        .setLineDash(5, 5)
                        .moveTo(drawContext.getDocument().getPage(pageNumber).getPageSize().getLeft(), rect.getBottom())
                        .lineTo(rect.getRight(), rect.getBottom())
                        .lineTo(rect.getRight(), rect.getTop())
                        .lineTo(drawContext.getDocument().getDefaultPageSize().getRight(), rect.getTop())
                        .stroke()
                        .restoreState();
            }
        }
    }
}
