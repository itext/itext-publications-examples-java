package com.itextpdf.samples.sandbox.typography.arabic;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfStampAnnotation;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class ArabicStampAnnotation {

    public static final String DEST = "./target/sandbox/typography/ArabicStampAnnotation.pdf";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ArabicStampAnnotation().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));

        // في القيام بنشاط
        String line1 = "\u0641\u064A\u0020\u0627\u0644\u0642\u064A\u0627\u0645\u0020\u0628\u0646\u0634\u0627\u0637";

        // Create a rectangle for an annotation
        Rectangle rectangleAnnot = new Rectangle(55, 650, 35, 35);

        // Create a stamp annotation and set some properties
        PdfAnnotation annotation = new PdfStampAnnotation(rectangleAnnot);
        annotation
                .setContents(line1)
                .setColor(ColorConstants.CYAN);

        // Add an empty page to the document, then add the annotation to the page
        PdfPage page = pdfDocument.addNewPage();
        page.addAnnotation(annotation);

        pdfDocument.close();
    }
}
