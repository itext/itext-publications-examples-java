/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example is written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/23117200/itext-create-document-with-unequal-page-sizes
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class UnequalPages extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/unequal_pages.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new UnequalPages().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        PageSize one = new PageSize(70, 140);
        PageSize two = new PageSize(700, 400);

        pdfDoc.setDefaultPageSize(one);
        doc.setMargins(2, 2, 2, 2);

        Paragraph p = new Paragraph("Hi");
        doc.add(p);

        pdfDoc.setDefaultPageSize(two);
        doc.setMargins(20, 20, 20, 20);
        doc.add(new AreaBreak());

        doc.add(p);

        doc.close();
    }
}
