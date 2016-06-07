/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/30911216/how-to-re-arrange-pages-in-pdf-using-itext
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Category(SampleTest.class)
public class ReorderPages extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/reorder_pages.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ReorderPages().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
        Document doc = new Document(pdfDoc);

        for (int i = 1; i < 17; i++) {
            doc.add(new Paragraph(String.format("Page %s", i)));
            if (16 != i) {
                doc.add(new AreaBreak());
            }
        }
        doc.close();

        PdfDocument srcDoc = new PdfDocument(new PdfReader(new RandomAccessSourceFactory().createSource(baos.toByteArray())
                , new ReaderProperties()));
        PdfDocument resultDoc = new PdfDocument(new PdfWriter(dest));
        resultDoc.initializeOutlines();

        List<Integer> pages = new ArrayList<>();
        pages.add(1);
        for (int i = 13; i <= 15; i++) {
            pages.add(i);
        }
        for (int i = 2; i <= 12; i++) {
            pages.add(i);
        }
        pages.add(16);
        srcDoc.copyPagesTo(pages, resultDoc);

        resultDoc.close();
        srcDoc.close();
    }
}
