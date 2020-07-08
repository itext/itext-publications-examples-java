package com.itextpdf.samples.htmlsamples.chapter03;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;

/**
 * Converts an HTML file to a PDF document that is meant to view on a medium-sized screen (tablet).
 */
public class C03E04_Medium {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch03/sxsw_medium.pdf";

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

        new C03E04_Medium().createPdf(BASEURI, SRC, DEST);
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
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        pdf.setTagged();
        PageSize pageSize = new PageSize(575, 1500);
        pdf.setDefaultPageSize(pageSize);
        ConverterProperties properties = new ConverterProperties();
        properties.setBaseUri(baseUri);
        MediaDeviceDescription mediaDeviceDescription = new MediaDeviceDescription(MediaType.SCREEN);
        mediaDeviceDescription.setWidth(pageSize.getWidth());
        properties.setMediaDeviceDescription(mediaDeviceDescription);
        HtmlConverter.convertToPdf(new FileInputStream(src), pdf, properties);
    }
}
