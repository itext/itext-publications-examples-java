package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;

public class AddNamedDestinations {
    public static final String PDF
            = "./target/sandbox/stamper/add_named_destinations.pdf";
    public static final String SRC
            = "./src/main/resources/pdfs/primes.pdf";
    public static final String DEST
            = "./target/xml/primes_with_destination.xml";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddNamedDestinations().manipulatePdf(DEST);
    }

    public static List<Integer> getFactors(int n) {
        List<Integer> factors = new ArrayList<Integer>();
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }
        return factors;
    }

    /**
     * Create an XML file with named destinations
     *
     * @param src  The path to the PDF with the destinations
     * @param dest The path to the XML file
     * @throws java.io.IOException
     */
    public void createXml(String src, String dest)
            throws IOException, ParserConfigurationException, TransformerException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = docFactory.newDocumentBuilder();

        org.w3c.dom.Document doc = db.newDocument();
        Element root = doc.createElement("Destination");
        doc.appendChild(root);

        Map<String, PdfObject> names = pdfDoc.getCatalog().getNameTree(PdfName.Dests).getNames();
        for (Map.Entry<String, PdfObject> name : names.entrySet()) {
            Element el = doc.createElement("Name");
            el.setAttribute("Page", name.getValue().toString());
            el.setTextContent(name.getKey());
            root.appendChild(el);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("encoding", "ISO8859-1");

        transformer.transform(new DOMSource(doc), new StreamResult(dest));
        pdfDoc.close();
    }

    protected void manipulatePdf(String dest) throws Exception {

        // Creates directory and new pdf file by content of the read pdf
        File file = new File(PDF);
        file.getParentFile().mkdirs();

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(PDF));

        for (int i = 1; i < pdfDoc.getNumberOfPages(); ) {
            if (getFactors(++i).size() > 1) {
                continue;
            }

            // Adding named destinations for further usage depending on the needs
            PdfPage pdfPage = pdfDoc.getPage(i);
            Rectangle pageRect = pdfPage.getPageSize();
            float getLeft = pageRect.getLeft();
            float getTop = pageRect.getTop();
            PdfExplicitDestination destObj = PdfExplicitDestination.createXYZ(pdfPage, getLeft, getTop, 1);
            pdfDoc.addNamedDestination("prime" + i, destObj.getPdfObject());
        }

        pdfDoc.close();

        createXml(PDF, dest);
    }
}
