package com.itextpdf.samples.sandbox.typography.odia;

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

public class OdiaCircleAnnotation {

    public static final String DEST = "./target/sandbox/typography/OdiaCircleAnnotation.pdf";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new OdiaCircleAnnotation().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));

        // ଜନ୍ମକାଳରୁ
        String text = "\u0B1C\u0B28\u0B4D\u0B2E\u0B15\u0B3E\u0B33\u0B30\u0B41";

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

