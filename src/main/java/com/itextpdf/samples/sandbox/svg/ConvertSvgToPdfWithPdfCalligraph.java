package com.itextpdf.samples.sandbox.svg;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.svg.converter.SvgConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ConvertSvgToPdfWithPdfCalligraph {
    private static final String SRC = "./src/main/resources/svg/";
    public static final String DEST = "./target/sandbox/svg/ConvertSvgToPdfWithPdfCalligraph.pdf";

    public static void main(String[] args) throws IOException {
        // Load the license file to use typography features
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/itextkey-typography.json")) {
            LicenseKey.loadLicenseFile(license);
        }

        String svgImage = SRC + "cauldronWithText.svg";
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ConvertSvgToPdfWithPdfCalligraph().manipulatePdf(svgImage, DEST);
    }

    public void manipulatePdf(String svgSource, String pdfDest) throws IOException {
        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(pdfDest))) {
            //Create new page
            PdfPage pdfPage = pdfDocument.addNewPage(PageSize.A4);

            //SVG image
            FileInputStream svgPath = new FileInputStream(svgSource);

            //Convert SVG image and add it to the page
            SvgConverter.drawOnPage(svgPath, pdfPage);
        }
    }
}


