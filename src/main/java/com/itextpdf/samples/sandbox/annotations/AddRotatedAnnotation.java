package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfStampAnnotation;

import java.io.File;

public class AddRotatedAnnotation {
    public static final String DEST = "./target/sandbox/annotations/add_rotated_annotation.pdf";

    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddRotatedAnnotation().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfPage firstPage = pdfDoc.getFirstPage();
        PdfAction linkAction = PdfAction.createURI("https://pages.itextpdf.com/ebook-stackoverflow-questions.html");

        Rectangle annotLocation = new Rectangle(30, 770, 90, 30);
        PdfAnnotation link = new PdfLinkAnnotation(annotLocation)

                // Set highlighting type which is enabled after a click on the annotation
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)
                .setAction(linkAction)
                .setColor(ColorConstants.RED.getColorValue());
        firstPage.addAnnotation(link);

        annotLocation = new Rectangle(30, 670, 30, 90);
        link = new PdfLinkAnnotation(annotLocation)
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)
                .setAction(linkAction)
                .setColor(ColorConstants.GREEN.getColorValue());
        firstPage.addAnnotation(link);

        annotLocation = new Rectangle(150, 770, 90, 30);
        PdfAnnotation stamp = new PdfStampAnnotation(annotLocation)
                .setStampName(new PdfName("Confidential"))

                // This method sets the text that will be displayed for the annotation or the alternate description,
                // if this type of annotation does not display text.
                .setContents("Landscape");
        firstPage.addAnnotation(stamp);

        annotLocation = new Rectangle(150, 670, 90, 90);
        stamp = new PdfStampAnnotation(annotLocation)
                .setStampName(new PdfName("Confidential"))
                .setContents("Portrait")
                .put(PdfName.Rotate, new PdfNumber(90));
        firstPage.addAnnotation(stamp);

        annotLocation = new Rectangle(250, 670, 90, 90);
        stamp = new PdfStampAnnotation(annotLocation)
                .setStampName(new PdfName("Confidential"))
                .setContents("Portrait")
                .put(PdfName.Rotate, new PdfNumber(45));
        firstPage.addAnnotation(stamp);

        pdfDoc.close();
    }
}
