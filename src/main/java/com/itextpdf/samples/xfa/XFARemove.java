package com.itextpdf.samples.xfa;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.xfa.XfaForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensekey.LicenseKey;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;

public class XFARemove {
    public static final String DEST = "./target/samples/pdf/xfa/XFARemove.pdf";
    public static final String INPUT_PDF = "src/main/resources/pdf/xfa/invoice.pdf";

    public static void main(String[] args) throws Exception{

        // Load the license file to use XFA features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-xfa.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new XFARemove().modifyPdf();
    }

    public void modifyPdf() throws IOException {

        // Create a pdf document instance
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(INPUT_PDF), new PdfWriter(DEST));

        // Load the DOM Document
        XfaForm xfa = PdfAcroForm.getAcroForm(pdfDoc, false).getXfaForm();
        Document domDoc = xfa.getDomDocument();

        // The follwing 2 lines of code only work for the specific document
        // Access the Script Node of the DOM Document
        Node template = domDoc.getFirstChild().getFirstChild().getNextSibling().getNextSibling().getNextSibling();
        // This is manually accessing a specific Node and only works for this specific XFA document
        Node script = template.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getFirstChild()
                .getNextSibling().getFirstChild().getNextSibling().getNextSibling().getNextSibling()
                .getNextSibling().getNextSibling().getNextSibling().getFirstChild().getFirstChild();

        // Remove the Script from the Node
        script.setNodeValue("");

        // Write XFA back to PDF Document
        xfa.setDomDocument(domDoc);
        xfa.write(pdfDoc);
        pdfDoc.close();
    }
}
