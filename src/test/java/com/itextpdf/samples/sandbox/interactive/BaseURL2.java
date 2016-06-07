/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/24568386/set-baseurl-of-an-existing-pdf-document
 */
package com.itextpdf.samples.sandbox.interactive;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class BaseURL2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/interactive/base_url2.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new BaseURL2().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfDictionary uri = new PdfDictionary();
        uri.put(PdfName.Type, PdfName.URI);
        uri.put(new PdfName("Base"), new PdfString("http://itextpdf.com/"));
        pdfDoc.getCatalog().put(PdfName.URI, uri);

        PdfAction action = PdfAction.createURI("index.php", false);
        Link link = new Link("Home page", action);
        doc.add(new Paragraph(link));

        pdfDoc.close();
    }
}
