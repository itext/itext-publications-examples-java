package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.Transform;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.IOException;

/*
 * SetRotationAngleAndSetTransform.java
 *
 * This sample shows the difference between setRotationAngle and setTransform.
 *
 * setRotationAngle rotates the layout element itself, so iText takes the rotation into account when
 * calculating the occupied area. setTransform changes how the element is drawn and can also combine
 * rotation with scaling, moving, or other transformations.
 */
public class SetRotationAngleAndSetTransform {
    public static final String DEST = "./target/sandbox/objects/setRotationAngleAndSetTransform.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SetRotationAngleAndSetTransform().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        addFlowLayoutExample(document);
        document.add(new AreaBreak());
        addFixedPositionExample(document);

        document.close();
    }

    private static void addFlowLayoutExample(Document document) {
        document.add(new Paragraph("Flow layout")
                .setFontSize(16));

        document.add(new Paragraph("Gray rectangles mark the occupied areas calculated by the layout engine."));

        // The next paragraph is positioned after the area occupied by the rotated element.
        document.add(new Paragraph("setRotationAngle changes the layout element rotation. "
                + "The occupied area used by the layout engine is calculated for the rotated element."));
        document.add(createSampleParagraph("setRotationAngle(Math.toRadians(12))")
                .setRotationAngle(Math.toRadians(12)));
        document.add(createAfterParagraph());

        // The transform changes the rendered appearance, not the flow layout box reserved for it.
        document.add(new Paragraph("setTransform changes the drawing while rendering. "
                + "The following content is laid out after the original, non-transformed element box."));
        document.add(createSampleParagraph("setTransform(new Transform().rotate(...))")
                .setTransform(new Transform().rotate((float) Math.toRadians(12))));
        document.add(createAfterTransformedParagraph());

        document.add(new Paragraph("setTransform can also rotate around a point other than the element center. "
                + "Here -50% and -50% move the rotation point to the lower-left corner."));
        document.add(createSampleParagraph("setTransform(new Transform().rotate(..., -50%, -50%))")
                .setTransform(new Transform().rotate((float) Math.toRadians(12),
                        UnitValue.createPercentValue(-50), UnitValue.createPercentValue(-50))));
        document.add(createAfterTransformedParagraph());

        // Reference rectangles for fixed-position elements. The rectangles are drawn in the PDF coordinate system.
        PdfCanvas pdfCanvas = new PdfCanvas(document.getPdfDocument().getLastPage());
        addReferenceRectangle(pdfCanvas, 36f, 618.15f, 232, 74);
        addReferenceRectangle(pdfCanvas, 36f, 388.18f, 232, 74);
        addReferenceRectangle(pdfCanvas, 36f, 188.31f, 232, 74);
    }

    private static void addFixedPositionExample(Document document) {
        document.add(new Paragraph("Fixed position")
                .setFontSize(16)
                .setFixedPosition(36, 760, 520));

        // Fixed-position coordinates still use the PDF coordinate system.
        document.add(createFixedPositionParagraph("setRotationAngle: rotation is part of layout positioning.",
                ColorConstants.ORANGE)
                .setFixedPosition(75, 560, 180)
                .setRotationAngle(Math.toRadians(30)));

        // setTransform changes the drawing and may extend outside the reference rectangle.
        document.add(createFixedPositionParagraph("setTransform.rotate: rendered around the occupied area's center.",
                ColorConstants.CYAN)
                .setFixedPosition(330, 560, 180)
                .setTransform(new Transform().rotate((float) Math.toRadians(30))));

        document.add(createFixedPositionParagraph("setRotationAngle keeps the element model simple: only rotation.",
                ColorConstants.ORANGE)
                .setFixedPosition(75, 360, 180)
                .setRotationAngle(Math.toRadians(-30)));

        // Unlike setRotationAngle, setTransform can combine several transformations.
        document.add(createFixedPositionParagraph("setTransform can combine rotation with scaling and other transformations.",
                ColorConstants.CYAN)
                .setFixedPosition(330, 360, 180)
                .setTransform(new Transform()
                        .rotate((float) Math.toRadians(-30))
                        .scaleX(1.15f)
                        .skewX((float) Math.toRadians(8))));

        document.add(new Paragraph("Gray rectangles mark the fixed-position boxes before rotation or transform.")
                .setFontSize(9)
                .setFixedPosition(75, 240, 435));

        // Reference rectangles for fixed-position elements. The rectangles are drawn in the PDF coordinate system.
        PdfCanvas pdfCanvas = new PdfCanvas(document.getPdfDocument().getLastPage());
        addReferenceRectangle(pdfCanvas, 74, 559, 182, 38);
        addReferenceRectangle(pdfCanvas, 329, 559, 182, 56);
        addReferenceRectangle(pdfCanvas, 74, 359, 182, 56);
        addReferenceRectangle(pdfCanvas, 329, 359, 182, 56);
    }

    private static Paragraph createSampleParagraph(String text) {
        Paragraph paragraph = new Paragraph(text + " - text wraps inside a bordered 230 pt paragraph. "
                + "This makes the difference in occupied area visible.")
                .addStyle(createSampleStyle())
                .setWidth(230);
        return paragraph;
    }

    private static Paragraph createAfterParagraph() {
        return new Paragraph("Next paragraph after the transformed element.")
                .setFontColor(ColorConstants.DARK_GRAY)
                .setMarginBottom(20);
    }

    private static Paragraph createAfterTransformedParagraph() {
        return createAfterParagraph()
                .setMarginTop(20);
    }

    private static Paragraph createFixedPositionParagraph(String text, Color color) {
        return new Paragraph(text)
                .addStyle(createSampleStyle())
                .setBackgroundColor(color)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private static Style createSampleStyle() {
        return new Style()
                .setBorder(new SolidBorder(ColorConstants.BLUE, 1))
                .setMarginTop(12)
                .setMarginBottom(12);
    }

    private static void addReferenceRectangle(PdfCanvas canvas, float x, float y, float width, float height) {
        canvas.saveState()
                .setStrokeColor(ColorConstants.LIGHT_GRAY)
                .rectangle(new Rectangle(x, y, width, height))
                .stroke()
                .restoreState();
    }
}
