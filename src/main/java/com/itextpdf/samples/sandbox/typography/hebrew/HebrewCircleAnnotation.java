package com.itextpdf.samples.sandbox.typography.hebrew;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfCircleAnnotation;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class HebrewCircleAnnotation {

    public static final String DEST = "./target/sandbox/typography/HebrewCircleAnnotation.pdf";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new HebrewCircleAnnotation().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));

        // רע ומר היה להם ליהודים
        String text = "\u05E8\u05E2\u0020\u05D5\u05DE\u05E8\u0020\u05D4\u05D9\u05D4\u0020\u05DC\u05D4\u05DD" +
                "\u0020\u05DC\u05D9\u05D4\u05D5\u05D3\u05D9\u05DD";

        // Create a rectangle for an annotation
        Rectangle rectangleAnnot = new Rectangle(55, 750, 35, 35);

        // Create the annotation, set its contents and color
        PdfAnnotation annotation = new PdfCircleAnnotation(rectangleAnnot);
        annotation
                .setContents(text)
                .setColor(ColorConstants.MAGENTA);

        // Add an empty page to the document, then add the annotation to the page
        PdfPage page = pdfDocument.addNewPage();
        page.addAnnotation(annotation);

        pdfDocument.close();
    }
}

