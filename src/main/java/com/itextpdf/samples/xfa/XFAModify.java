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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class XFAModify {
    public static final String DEST = "./target/samples/pdf/xfa/XFAModify.pdf";
    public static final String INPUT_PDF = "src/main/resources/pdf/xfa/invoice.pdf";

    public static void main(String[] args) throws Exception{

        // Load the license file to use XFA features
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/itextkey-xfa.json")) {
            LicenseKey.loadLicenseFile(license);
        }

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new XFAModify().modifyPdf();
    }

    public void modifyPdf() throws IOException {

        // Create a pdf document instance
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(INPUT_PDF), new PdfWriter(DEST));

        // Load the DOM Document
        XfaForm xfa = PdfFormCreator.getAcroForm(pdfDoc, false).getXfaForm();
        Document domDoc = xfa.getDomDocument();

        // This works for the specific document
        // Access the Script Node of the DOM Document
        Node template = domDoc.getFirstChild().getFirstChild().getNextSibling().getNextSibling().getNextSibling();
        Node script = template.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getFirstChild()
                .getNextSibling().getFirstChild().getNextSibling().getNextSibling().getNextSibling()
                .getNextSibling().getNextSibling().getNextSibling().getFirstChild().getFirstChild();

        // Update the script message
        String message = "xfa.host.messageBox(\"XFA SCRIPT Message!!!\")";
        script.setNodeValue(message);

        // Write XFA back to PDF Document
        xfa.setDomDocument(domDoc);
        xfa.write(pdfDoc);
        pdfDoc.close();
    }

}
