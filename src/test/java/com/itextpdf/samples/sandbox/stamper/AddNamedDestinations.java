/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/34117589
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Category(SampleTest.class)
public class AddNamedDestinations extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/stamper/add_named_destinations.pdf";
    public static final String SRC
            = "./src/test/resources/pdfs/primes.pdf";
    public static final String XML
            = "./target/test/resources/sandbox/stamper/primes_with_destination.xml";

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
    public void createXml(String src, String dest) throws IOException,
            ParserConfigurationException, TransformerException {
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

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty("encoding", "ISO8859-1");

        t.transform(new DOMSource(doc), new StreamResult(dest));
        pdfDoc.close();
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        int n = pdfDoc.getNumberOfPages();
        for (int i = 1; i < n; ) {
            if (getFactors(++i).size() > 1) {
                continue;
            }
            PdfArray array = new PdfArray();
            array.add(pdfDoc.getPage(i).getPdfObject());
            array.add(PdfName.XYZ);
            array.add(new PdfNumber(pdfDoc.getPage(i).getPageSize().getLeft()));
            array.add(new PdfNumber(pdfDoc.getPage(i).getPageSize().getTop()));
            array.add(new PdfNumber(1));
            // Notice that the document has already destinations like "Prime+i"
            pdfDoc.addNamedDestination("prime" + i, array);
        }
        pdfDoc.close();

        createXml(DEST, XML);
    }
}