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
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.licensekey.LicenseKey;

/**
 * Converts an HTML page to a PDF that consists of a single page.
 */
public class C04E03_MovieTable3 {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch04/movie_table3.pdf";

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

        C04E03_MovieTable3 app = new C04E03_MovieTable3();
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
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        pdf.setDefaultPageSize(new PageSize(595, 14400));
        Document document = HtmlConverter.convertToDocument(new ByteArrayInputStream(html), pdf, properties);
        EndPosition endPosition = new EndPosition();
        LineSeparator separator = new LineSeparator(endPosition);
        document.add(separator);
        document.getRenderer().close();
        PdfPage page = pdf.getPage(1);
        float y = endPosition.getY() - 36;
        page.setMediaBox(new Rectangle(0, y, 595, 14400 - y));
        document.close();
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
     * Implementation of the ILineDrawer interface that won't draw a line,
     * but that will allow us to get the Y-position at the end of the file.
     */
    class EndPosition implements ILineDrawer {

        /**
         * A Y-position.
         */
        protected float y;

        /**
         * Gets the Y-position.
         *
         * @return the Y-position
         */
        public float getY() {
            return y;
        }

        /* (non-Javadoc)
         * @see com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#draw(com.itextpdf.kernel.pdf.canvas.PdfCanvas, com.itextpdf.kernel.geom.Rectangle)
         */
        @Override
        public void draw(PdfCanvas pdfCanvas, Rectangle rect) {
            this.y = rect.getY();
        }

        /* (non-Javadoc)
         * @see com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#getColor()
         */
        @Override
        public Color getColor() {
            return null;
        }

        /* (non-Javadoc)
         * @see com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#getLineWidth()
         */
        @Override
        public float getLineWidth() {
            return 0;
        }

        /* (non-Javadoc)
         * @see com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#setColor(com.itextpdf.kernel.color.Color)
         */
        @Override
        public void setColor(Color color) {
        }

        /* (non-Javadoc)
         * @see com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#setLineWidth(float)
         */
        @Override
        public void setLineWidth(float lineWidth) {
        }

    }
}
