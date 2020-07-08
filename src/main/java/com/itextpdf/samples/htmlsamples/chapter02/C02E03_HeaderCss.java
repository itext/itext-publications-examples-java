package com.itextpdf.samples.htmlsamples.chapter02;

import java.io.File;
import java.io.IOException;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensekey.LicenseKey;

/**
 * Converts an HTML file with CSS in the header to PDF.
 */
public class C02E03_HeaderCss {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch02/movie03.pdf";

    /**
     * The path to the source HTML file.
     */
    public static final String SRC = "./src/main/resources/htmlsamples/html/3_header_css.html";

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        C02E03_HeaderCss app = new C02E03_HeaderCss();
        app.createPdf(SRC, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param src  the path to the source HTML file
     * @param dest the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(String src, String dest) throws IOException {
        HtmlConverter.convertToPdf(new File(src), new File(dest));
    }
}
