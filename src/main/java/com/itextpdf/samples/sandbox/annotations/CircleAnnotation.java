package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfCircleAnnotation;

import java.io.File;

public class CircleAnnotation {
    public static final String DEST = "./target/sandbox/annotations/circle_annotation.pdf";

    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CircleAnnotation().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        Rectangle rect = new Rectangle(150, 770, 50, 50);
        PdfAnnotation annotation = new PdfCircleAnnotation(rect)
                .setBorderStyle(PdfAnnotation.STYLE_DASHED)
                .setDashPattern(new PdfArray(new int[]{3, 2}))

                // This method sets the text that will be displayed for the annotation or the alternate description,
                // if this type of annotation does not display text.
                .setContents("Circle")
                .setTitle(new PdfString("Circle"))
                .setColor(ColorConstants.BLUE)

                // Set to print the annotation when the page is printed
                .setFlags(PdfAnnotation.PRINT)
                .setBorder(new PdfArray(new float[]{0, 0, 2}))

                // Set the interior color
                .put(PdfName.IC, new PdfArray(new int[]{1, 0, 0}));
        pdfDoc.getFirstPage().addAnnotation(annotation);

        pdfDoc.close();
    }
}
