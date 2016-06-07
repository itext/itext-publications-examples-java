/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example is written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/26726485/itext-add-delete-retrieve-information-in-custom-property
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class CustomMetaEntry extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/custom_meta_entry.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CustomMetaEntry().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        pdfDoc.getDocumentInfo().setTitle("Some example");
        pdfDoc.getDocumentInfo().setMoreInfo("Test", "test");

        Paragraph p = new Paragraph("Hello World");

        doc.add(p);

        doc.close();
    }
}
