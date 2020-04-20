package com.itextpdf.samples.sandbox.typography.gujarati;

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

public class GujaratiCircleAnnotation {

    public static final String DEST = "./target/sandbox/typography/GujaratiCircleAnnotation.pdf";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new GujaratiCircleAnnotation().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));

        // વાઈસરૉયને
        String text = "\u0AB5\u0ABE\u0A88\u0AB8\u0AB0\u0AC9\u0AAF\u0AA8\u0AC7";

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


