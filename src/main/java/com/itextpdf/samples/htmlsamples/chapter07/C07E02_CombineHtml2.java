package com.itextpdf.samples.htmlsamples.chapter07;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.licensekey.LicenseKey;

/**
 * Can we parse different HTML files and combine them into one PDF?
 * Yes, this can be done in different ways. This example shows how
 * to convert HTML to iText elements, and how to add the elements
 * of the different HTML files to a single PDF document.
 */
public class C07E02_CombineHtml2 {

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
    public static final String DEST = "./target/htmlsamples/ch07/bundle2.pdf";

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

        new C07E02_CombineHtml2().createPdf(BASEURI, SRC, DEST);
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
        Document document = new Document(pdf);
        for (String html : src) {
            List<IElement> elements = HtmlConverter.convertToElements(new FileInputStream(html), properties);
            for (IElement element : elements) {
                document.add((IBlockElement) element);
            }
        }
        document.close();
    }
}
