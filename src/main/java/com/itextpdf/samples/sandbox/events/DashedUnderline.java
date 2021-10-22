package com.itextpdf.samples.sandbox.events;

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

import java.io.File;

public class DashedUnderline {
    public static final String DEST = "./target/sandbox/events/dashed_underline.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DashedUnderline().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
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

    private static class DashedLineTextRenderer extends TextRenderer {

        public DashedLineTextRenderer(Text textElement) {
            super(textElement);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new DashedLineTextRenderer((Text) modelElement);
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
