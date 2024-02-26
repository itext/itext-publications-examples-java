package com.itextpdf.samples.htmlsamples.chapter04;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.pdfa.PdfADocument;

/**
 * Creates a PDF document from an XML file using XSLT to convert the XML to HTML,
 * introducing custimized bookmarks.
 */
public class C04E08_MovieInvoice3 {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch04/movie_invoice3.pdf";

    /**
     * The Base URI of the HTML page.
     */
    public static final String BASEURI = "./src/main/resources/htmlsamples/html/";

    /**
     * The XML containing all the data.
     */
    public static final String XML = "./src/main/resources/htmlsamples/xml/movies.xml";

    /**
     * The XSLT needed to transform the XML to HTML.
     */
    public static final String XSL = "./src/main/resources/htmlsamples/xml/movies_invoice.xsl";

    /**
     * The path to the output intent file.
     */
    public static final String INTENT = "./src/main/resources/htmlsamples/color/sRGB_CS_profile.icm";

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException, TransformerException {
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
				+ "/itextkey-html2pdf_typography.json")) {
			LicenseKey.loadLicenseFile(license);
		}
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        C04E08_MovieInvoice3 app = new C04E08_MovieInvoice3();
        app.createPdf(app.getBytes(XML), app.createHtml(XML, XSL), BASEURI, DEST, INTENT);
    }

    /**
     * Creates the PDF file.
     *
     * @param xml     a byte array with XML data
     * @param html    the HTML file as a byte array
     * @param baseUri the base URI
     * @param dest    the path to the resulting PDF
     * @param intent  a path to the output intent
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(byte[] xml, byte[] html, String baseUri, String dest, String intent) throws IOException {
        PdfWriter writer = new PdfWriter(dest);
        PdfADocument pdf = new PdfADocument(writer, PdfAConformanceLevel.PDF_A_3A,
                new PdfOutputIntent("Custom", "", "http://www.color.org",
                        "sRGB IEC61966-2.1", new FileInputStream(intent)));
        pdf.setTagged();
        pdf.addFileAttachment("Movie info",
                PdfFileSpec.createEmbeddedFileSpec(pdf, xml, "Movie info", "movies.xml",
                        PdfName.ApplicationXml, new PdfDictionary(), PdfName.Data));
        ConverterProperties properties = new ConverterProperties();
        properties.setBaseUri(baseUri);
        HtmlConverter.convertToPdf(new ByteArrayInputStream(html), pdf, properties);
    }

    /**
     * Creates an HTML file by performing an XSLT transformation on an XML file.
     *
     * @param xmlPath the path to the XML file.
     * @param xslPath the path to the XSL file
     * @return the resulting HTML as a byte[]
     * @throws IOException          signals that an I/O exception has occurred.
     * @throws TransformerException the transformer exception
     */
    public byte[] createHtml(String xmlPath, String xslPath) throws IOException, TransformerException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(baos, "UTF-8");
        StreamSource xml = new StreamSource(new File(xmlPath));
        StreamSource xsl = new StreamSource(new File(xslPath));
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsl);
        transformer.transform(xml, new StreamResult(writer));
        writer.flush();
        writer.close();
        return baos.toByteArray();
    }

    /**
     * Gets the bytes from a file.
     *
     * @param file the path to the file
     * @return the bytes
     * @throws IOException signals that an I/O exception has occurred.
     */
    public byte[] getBytes(String file) throws IOException {
        return Files.readAllBytes(Paths.get(file));
    }

}
