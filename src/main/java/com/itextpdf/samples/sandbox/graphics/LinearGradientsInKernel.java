package com.itextpdf.samples.sandbox.graphics;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.gradients.AbstractLinearGradientBuilder;
import com.itextpdf.kernel.colors.gradients.GradientColorStop;
import com.itextpdf.kernel.colors.gradients.GradientColorStop.OffsetType;
import com.itextpdf.kernel.colors.gradients.GradientSpreadMethod;
import com.itextpdf.kernel.colors.gradients.LinearGradientBuilder;
import com.itextpdf.kernel.colors.gradients.StrategyBasedLinearGradientBuilder;
import com.itextpdf.kernel.colors.gradients.StrategyBasedLinearGradientBuilder.GradientStrategy;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.styledxmlparser.css.util.CssGradientUtil;

import java.io.File;

public class LinearGradientsInKernel {
    public static final String DEST = "./target/sandbox/graphics/linearGradientsInKernel.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new LinearGradientsInKernel().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        addLinearGradientITextAPIApproach(pdfDoc);
        addLinearGradientCssApproach(pdfDoc);
        addLinearGradientDirectCoordinatesApproach(pdfDoc);

        pdfDoc.close();
    }

    private void addLinearGradientITextAPIApproach(PdfDocument pdfDoc) {
        AbstractLinearGradientBuilder gradientBuilder = new StrategyBasedLinearGradientBuilder()
                .setGradientDirectionAsStrategy(GradientStrategy.TO_TOP_RIGHT)
                .setSpreadMethod(GradientSpreadMethod.PAD)
                .addColorStop(new GradientColorStop(ColorConstants.CYAN.getColorValue()))
                .addColorStop(new GradientColorStop(ColorConstants.GREEN.getColorValue()))
                .addColorStop(new GradientColorStop(new float[]{1f, 0f, 0f}, 0.5f, OffsetType.RELATIVE));

        AffineTransform canvasTransform = AffineTransform.getTranslateInstance(50, -50);
        canvasTransform.scale(0.8, 1.1);
        canvasTransform.rotate(Math.PI/3, 400f, 550f);

        Rectangle rectangleToDraw = new Rectangle(50f, 450f, 500f, 300f);

        generatePdf(pdfDoc, canvasTransform, gradientBuilder, rectangleToDraw);
    }

    private void addLinearGradientCssApproach(PdfDocument pdfDoc) {
        String gradientValue = "linear-gradient(to left, #ff0000, #008000, #0000ff)";

        if (CssGradientUtil.isCssLinearGradientValue(gradientValue)) {
            StrategyBasedLinearGradientBuilder gradientBuilder = CssGradientUtil

                    // "em/rem" parameters are mandatory but don't have effect in case of such parameters aren't used
                    // within passing "gradientValue" variable
                    .parseCssLinearGradient(gradientValue, 12, 12);
            Rectangle rectangleToDraw = new Rectangle(50f, 450f, 500f, 300f);

            generatePdf(pdfDoc, null, gradientBuilder, rectangleToDraw);
        } else {
            System.out.println("The passed parameter: " + "\n" + gradientValue + "\n" +
                    " is not a linear gradient or repeating linear gradient function");
        }
    }

    private void addLinearGradientDirectCoordinatesApproach(PdfDocument pdfDoc) {
        Rectangle targetBoundingBox = new Rectangle(50f, 450f, 300f, 300f);
        AbstractLinearGradientBuilder gradientBuilder = new LinearGradientBuilder()
                .setGradientVector(targetBoundingBox.getLeft() + 100f, targetBoundingBox.getBottom() + 100f,
                        targetBoundingBox.getRight() - 100f, targetBoundingBox.getTop() - 100f)
                .setSpreadMethod(GradientSpreadMethod.REPEAT)

                // For the RELATIVE offset type "0" value means the target vector start and the "1" value means the target vector end
                .addColorStop(new GradientColorStop(ColorConstants.BLUE.getColorValue(), 0.5, OffsetType.RELATIVE))
                .addColorStop(new GradientColorStop(ColorConstants.GREEN.getColorValue(), 1, OffsetType.RELATIVE));

        generatePdf(pdfDoc, null, gradientBuilder, targetBoundingBox);
    }

    private void generatePdf(PdfDocument pdfDocument, AffineTransform transform,
            AbstractLinearGradientBuilder gradientBuilder, Rectangle rectangleToDraw) {
            PdfCanvas canvas = new PdfCanvas(pdfDocument.addNewPage());

            if (transform != null) {
                canvas.concatMatrix(transform);
            }

            canvas.setFillColor(gradientBuilder.buildColor(rectangleToDraw, transform, pdfDocument))
                    .setStrokeColor(ColorConstants.BLACK)
                    .rectangle(rectangleToDraw)
                    .fillStroke();
    }
}
