package com.itextpdf.samples.htmlsamples.chapter04;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.OutlineHandler;
import com.itextpdf.licensekey.LicenseKey;

/**
 * Creates a PDF document from an XML file using XSLT to convert the XML to HTML,
 * introducing customized bookmarks.
 */
public class C04E05_MovieOverview2 {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch04/movie_overview2.pdf";

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
    public static final String XSL = "./src/main/resources/htmlsamples/xml/movies_overview.xsl";

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException, TransformerException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        C04E05_MovieOverview2 app = new C04E05_MovieOverview2();
        app.createPdf(app.createHtml(XML, XSL), BASEURI, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param html    the HTML file as a byte array
     * @param baseUri the base URI
     * @param dest    the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(byte[] html, String baseUri, String dest) throws IOException {
        ConverterProperties properties = new ConverterProperties();
        properties.setBaseUri(baseUri);
        OutlineHandler outlineHandler = new OutlineHandler();
        outlineHandler.putTagPriorityMapping("h1", 1);
        outlineHandler.putTagPriorityMapping("p", 2);
        properties.setOutlineHandler(outlineHandler);
        HtmlConverter.convertToPdf(new ByteArrayInputStream(html), new FileOutputStream(dest), properties);
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

}
