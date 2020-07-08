package com.itextpdf.samples.htmlsamples.chapter03;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;

/**
 * Converts an HTML file to a PDF document that is meant for printing.
 */
public class C03E02_Print {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch03/sxsw_print.pdf";

    /**
     * The Base URI of the HTML page.
     */
    public static final String BASEURI = "./src/main/resources/htmlsamples/html/";

    /**
     * The path to the source HTML file.
     */
    public static final String SRC = String.format("%ssxsw.html", BASEURI);

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

        new C03E02_Print().createPdf(BASEURI, SRC, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param baseUri the base URI
     * @param src     the path to the source HTML file
     * @param dest    the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(String baseUri, String src, String dest) throws IOException {
        ConverterProperties properties = new ConverterProperties();
        properties.setBaseUri(baseUri);
        MediaDeviceDescription mediaDeviceDescription = new MediaDeviceDescription(MediaType.PRINT);
        properties.setMediaDeviceDescription(mediaDeviceDescription);
        HtmlConverter.convertToPdf(new FileInputStream(src), new FileOutputStream(dest), properties);
    }
}
