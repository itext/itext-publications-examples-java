/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.typography.thai;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfCaretAnnotation;
import com.itextpdf.licensing.base.LicenseKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ThaiCaretAnnotation {

    public static final String DEST = "./target/sandbox/typography/ThaiCaretAnnotation.pdf";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/itextkey-typography.json")) {
            LicenseKey.loadLicenseFile(license);
        }

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ThaiCaretAnnotation().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));

        // ความยุติธรรม
        String line1 = "\u0E04\u0E27\u0E32\u0E21\u0E22\u0E38\u0E15\u0E34\u0E18\u0E23\u0E23\u0E21";

        // Create a rectangle for an annotation
        Rectangle rectangleAnnot = new Rectangle(55, 750, 35, 35);

        // Create the annotation, set its contents and color
        PdfAnnotation annotation = new PdfCaretAnnotation(rectangleAnnot);
        annotation
                .setContents(line1)
                .setColor(ColorConstants.MAGENTA);

        // Add an empty page to the document, then add the annotation to the page
        PdfPage page = pdfDocument.addNewPage();
        page.addAnnotation(annotation);

        pdfDocument.close();
    }
}







