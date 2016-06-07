/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example is written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/32582927/itext-pdfwriter-writing-table-header-if-the-few-table-rows-go-to-new-page
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class HeaderRowRepeated extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/header_row_repeated.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new HeaderRowRepeated().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        // table with 2 columns:
        Table table = new Table(2);
        // header row:
        table.addHeaderCell("Key");
        table.addHeaderCell("Value");
        table.setSkipFirstHeader(true);
        // many data rows:
        for (int i = 1; i < 51; i++) {
            table.addCell("key: " + i);
            table.addCell("value: " + i);
        }
        doc.add(table);
        doc.close();
    }
}
