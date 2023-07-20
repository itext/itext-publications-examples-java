/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.xfa;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.xfa.XfaForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensing.base.LicenseKey;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class XFACalculate {
    public static final String DEST = "./target/samples/pdf/xfa/XFACalculate.pdf";
    public static final String INPUT_PDF = "src/main/resources/pdf/xfa/invoice.pdf";

    public static void main(String[] args) throws Exception{

        // Load the license file to use XFA features
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/itextkey-xfa.json")) {
            LicenseKey.loadLicenseFile(license);
        }

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new XFACalculate().modifyPdf();
    }

    public void modifyPdf() throws IOException {

        // Create a pdf document instance
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(INPUT_PDF), new PdfWriter(DEST));

        // Load the DOM Document
        XfaForm xfa = PdfFormCreator.getAcroForm(pdfDoc, false).getXfaForm();
        Document domDoc = xfa.getDomDocument();

        // The follwing 2 lines of code only work for the specific document
        // Generate the list of calculate amount Nodes
        NodeIterator nodes = findCalc(domDoc);

        // Update calculate value
        Node node;
        while((node = nodes.nextNode()) !=null){
            Node calc = node.getFirstChild().getFirstChild();
            String curVal = calc.getNodeValue();
            calc.setNodeValue("2*" + curVal);
        }

        // Write XFA back to PDF Document
        xfa.setDomDocument(domDoc);
        xfa.write(pdfDoc);
        pdfDoc.close();
    }

    // Finds all of the calculate amount Nodes
    public NodeIterator findCalc(Document doc){
        Node first = doc.getDocumentElement();
        DocumentTraversal docT = (DocumentTraversal) doc;
        return docT.createNodeIterator(first, NodeFilter.SHOW_ALL, new CalcCheck(),true);
    }

    // NodeFilter to search the DOM Document to access the calculate amount Nodes
    private class CalcCheck implements NodeFilter {
        @Override
        public short acceptNode(Node n) {
            try {
                if (n.getLocalName().equalsIgnoreCase("calculate")) {
                    if(n.getParentNode().getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("amount")) {
                        return NodeFilter.FILTER_ACCEPT;
                    }
                }
                return NodeFilter.FILTER_SKIP;
            }
            catch (NullPointerException e){
                return NodeFilter.FILTER_SKIP;
            }
        }
    }
}
