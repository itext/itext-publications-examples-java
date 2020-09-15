package com.itextpdf.samples.htmlsamples.chapter04;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.licensekey.LicenseKey;

/**
 * Creates a PDF document from an XML file using XSLT to convert the XML to HTML,
 * introducing a single-page PDF in the background as "company stationery" and
 * as well as a custom page number.
 */
public class C04E02_MovieTable2 {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch04/movie_table2.pdf";

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
    public static final String XSL = "./src/main/resources/htmlsamples/xml/movies_table.xsl";

    /**
     * The path to a single-page PDF that will be used as company stationery.
     */
    public static final String STATIONERY = "./src/main/resources/htmlsamples/pdf/stationery.pdf";

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException          signals that an I/O exception has occurred.
     * @throws TransformerException the transformer exception
     */
    public static void main(String[] args) throws IOException, TransformerException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        C04E02_MovieTable2 app = new C04E02_MovieTable2();
        app.createPdf(app.createHtml(XML, XSL), BASEURI, STATIONERY, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param html       the HTML file as a byte array
     * @param baseUri    the base URI
     * @param stationery the path to a single-page PDF file that will act as stationery
     * @param dest       the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(byte[] html, String baseUri, String stationery, String dest) throws IOException {
        ConverterProperties properties = new ConverterProperties();
        properties.setBaseUri(baseUri);
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        IEventHandler handler = new Background(pdf, stationery);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, handler);
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
     * Implementation of the IEventHandler to add a background and a page number to every page.
     */
    class Background implements IEventHandler {

        /**
         * The Form XObject that will be added as the background for every page.
         */
        PdfXObject stationery;

        /**
         * Instantiates a new Background instance.
         *
         * @param pdf the PdfDocument instance of the PDF to which the background will be added
         * @param src the path to the single-page PDF file
         * @throws IOException signals that an I/O exception has occurred.
         */
        public Background(PdfDocument pdf, String src) throws IOException {
            PdfDocument template = new PdfDocument(new PdfReader(src));
            PdfPage page = template.getPage(1);
            stationery = page.copyAsFormXObject(pdf);
            template.close();
        }

        /* (non-Javadoc)
         * @see com.itextpdf.kernel.events.IEventHandler#handleEvent(com.itextpdf.kernel.events.Event)
         */
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdf);
            pdfCanvas.addXObjectAt(stationery, 0, 0);
            Rectangle rect = new Rectangle(36, 32, 36, 64);
            Canvas canvas = new Canvas(pdfCanvas, rect);
            canvas.add(new Paragraph(String.valueOf(pdf.getNumberOfPages())).setFontSize(48)
                    .setFontColor(ColorConstants.WHITE));
            canvas.close();
        }

    }
}
