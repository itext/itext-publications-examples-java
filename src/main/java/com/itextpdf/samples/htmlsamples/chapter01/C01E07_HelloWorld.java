package com.itextpdf.samples.htmlsamples.chapter01;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.licensekey.LicenseKey;

/**
 * Converts a simple HTML file to PDF using an InputStream and a PdfDocument
 * as arguments for the convertToDocument() method.
 */
public class C01E07_HelloWorld {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch01/helloWorld07.pdf";

    /**
     * The Base URI of the HTML page.
     */
    public static final String BASEURI = "./src/main/resources/htmlsamples/html/";

    /**
     * The path to the source HTML file.
     */
    public static final String SRC = String.format("%shello.html", BASEURI);

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

        new C01E07_HelloWorld().createPdf(BASEURI, SRC, DEST);
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
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = HtmlConverter.convertToDocument(new FileInputStream(src), pdf, properties);
        document.add(new Paragraph("Goodbye!"));
        document.close();
    }
}
