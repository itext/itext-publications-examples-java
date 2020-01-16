/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
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
    public static final String DEST = "./target/sandbox/acroforms/createXfdf.xfdf";


    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CreateXfdf().createXfdf(DEST);
    }

    // Currently iText xfdf implementation works in the following way:
    // data from Pdf form could be received as file with the XFDF file extension.
    public void createXfdf(String dest) throws Exception {
        String pdfDocumentName = "createXfdf.pdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(new FileInputStream
                (sourceFolder + pdfDocumentName)));

        XfdfObjectFactory factory = new XfdfObjectFactory();
        XfdfObject xfdfObject = factory.createXfdfObject(pdfDocument, pdfDocumentName);
        xfdfObject.writeToFile(dest);

        pdfDocument.close();
    }
}