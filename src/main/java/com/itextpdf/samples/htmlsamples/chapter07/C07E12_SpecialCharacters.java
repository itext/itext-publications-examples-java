package com.itextpdf.samples.htmlsamples.chapter07;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensekey.LicenseKey;

/**
 * Converts an HTML file with special entities to a PDF.
 */
public class C07E12_SpecialCharacters {

    /**
     * An HTML file as a String value.
     */
    public static final String HTML = "<html><head></head>" +
            "<body style=\"font-size:12.0pt; font-family:Arial\">" +
            "<p>Special symbols: " +
            "&larr;  &darr; &harr; &uarr; &rarr; &euro; &copy; &#9786;</p>" +
            "</body></html>";

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch07/special_characters.pdf";

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

        new C07E12_SpecialCharacters().createPdf(HTML, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param html the source HTML file as a String value.
     * @param dest the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(String html, String dest) throws IOException {
        HtmlConverter.convertToPdf(html, new FileOutputStream(dest));
    }
}
