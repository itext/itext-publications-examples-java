/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/29260730/how-do-you-underline-text-with-dashedline-in-itext-pdf
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

import java.io.File;

@Category(SampleTest.class)
public class DashedUnderline extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/events/dashed_underline.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new DashedUnderline().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("This text is not underlined"));
        Text text1 = new Text("This text is underlined with a solid line");
        text1.setUnderline(1, -3);
        doc.add(new Paragraph(text1));
        Text text2 = new Text("This text is underlined with a dashed line");
        text2.setNextRenderer(new DashedLineTextRenderer(text2));
        doc.add(new Paragraph(text2));
        doc.close();
    }


    protected class DashedLineTextRenderer extends TextRenderer {
        public DashedLineTextRenderer(Text textElement) {
            super(textElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            Rectangle rect = this.getOccupiedAreaBBox();
            PdfCanvas canvas = drawContext.getCanvas();
            canvas
                    .saveState()
                    .setLineDash(3, 3)
                    .moveTo(rect.getLeft(), rect.getBottom() - 3)
                    .lineTo(rect.getRight(), rect.getBottom() - 3)
                    .stroke()
                    .restoreState();
        }
    }
}
