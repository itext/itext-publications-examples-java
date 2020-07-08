package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;
import java.io.IOException;

public class CenterColumnVertically {
    public static final String DEST = "./target/sandbox/objects/center_column_vertically.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CenterColumnVertically().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        float llx = 50;
        float lly = 650;
        float urx = 400;
        float ury = 800;

        Rectangle rect = new Rectangle(llx, lly, urx - llx, ury - lly);
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        canvas.setStrokeColor(ColorConstants.RED)
                .setLineWidth(0.5f)
                .rectangle(rect)
                .stroke();

        Paragraph p = new Paragraph("This text is centered vertically. "
                + "It is rendered in the middle of the red rectangle.");
        float width = resolveTextHeight(doc, rect, p);
        new Canvas(canvas, rect)
                .add(p.setFixedPosition(llx, (ury + lly) / 2 - width / 2, urx - llx).setMargin(0));

        doc.close();
    }

    private static float resolveTextHeight(Document doc, Rectangle rect, Paragraph p) {
        IRenderer pRenderer = p.createRendererSubTree().setParent(doc.getRenderer());
        LayoutResult pLayoutResult = pRenderer.layout(new LayoutContext(new LayoutArea(0, rect)));

        Rectangle pBBox = pLayoutResult.getOccupiedArea().getBBox();
        return pBBox.getHeight();
    }
}
