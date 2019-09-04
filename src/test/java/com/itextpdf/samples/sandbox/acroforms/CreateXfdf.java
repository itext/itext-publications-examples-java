/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/*
 * Example written by Bruno Lowagie in answer to
 * http://stackoverflow.com/questions/36523371
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.xfdf.XfdfObject;
import com.itextpdf.forms.xfdf.XfdfObjectFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;

import java.io.File;
import java.io.FileInputStream;

public class CreateXfdf {
    public static final String sourceFolder = "./src/test/resources/pdfs/";
    public static final String destinationFolder = "./target/sandbox/acroforms/";


    public static void main(String[] args) throws Exception {
        File file = new File(destinationFolder);
        file.getParentFile().mkdirs();
        new CreateXfdf().createXfdf(destinationFolder);
    }

    public void createXfdf(String destinationFolder) throws Exception {
        String pdfDocumentName = "subscribe.pdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(new FileInputStream(sourceFolder + pdfDocumentName)));
        String xfdfFilename = destinationFolder + "subscribe.xfdf";

        XfdfObjectFactory factory = new XfdfObjectFactory();
        XfdfObject xfdfObject = factory.createXfdfObject(pdfDocument, pdfDocumentName);
        xfdfObject.writeToFile(xfdfFilename);

        pdfDocument.close();
    }
}