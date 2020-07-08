package com.itextpdf.samples.htmlsamples.chapter07;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.licensekey.LicenseKey;

/**
 * Can we parse different HTML files and combine them into one PDF?
 * Yes, this can be done in different ways. This example shows how
 * to create a PDF in memory for each HTML, then use PdfMerger to
 * merge the different PDFs into one, on a page per page basis.
 */
public class C07E01_CombineHtml {

    /**
     * The Base URI of the HTML page.
     */
    public static final String BASEURI = "./src/main/resources/htmlsamples/html/";

    /**
     * An array containing the paths to different HTML files.
     */
    public static final String[] SRC = {
            String.format("%sinvitation.html", BASEURI),
            String.format("%ssxsw.html", BASEURI),
            String.format("%smovies.html", BASEURI)
    };

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch07/bundle.pdf";

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

        new C07E01_CombineHtml().createPdf(BASEURI, SRC, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param baseUri the base URI
     * @param src     an array with the paths to different source HTML files
     * @param dest    the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(String baseUri, String[] src, String dest) throws IOException {
        ConverterProperties properties = new ConverterProperties();
        properties.setBaseUri(baseUri);
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        PdfMerger merger = new PdfMerger(pdf);
        for (String html : src) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfDocument temp = new PdfDocument(new PdfWriter(baos));
            HtmlConverter.convertToPdf(new FileInputStream(html), temp, properties);
            temp = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
            merger.merge(temp, 1, temp.getNumberOfPages());
            temp.close();
        }
        pdf.close();
    }
}
