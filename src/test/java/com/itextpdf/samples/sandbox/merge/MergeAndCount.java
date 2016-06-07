/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie.
 */
package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PageRange;
import com.itextpdf.kernel.utils.PdfSplitter;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Category(SampleTest.class)
public class MergeAndCount {
    public static final String DESTFOLDER
            = "./samples/target/test/resources/sandbox/merge/";
    public static final String RESOURCE
            = "./samples/src/test/resources/pdfs/Wrong.pdf";

    public static void main(String[] args) throws IOException {
        new MergeAndCount().manipulatePdf(DESTFOLDER);
    }

    public void manipulatePdf(final String destFolder) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(RESOURCE));

        List<PdfDocument> splitDocuments = new PdfSplitter(pdfDoc) {
            int partNumber = 1;

            @Override
            protected PdfWriter getNextPdfWriter(PageRange documentPageRange) {
                try {
                    return new PdfWriter(destFolder + "splitDocument1_" + String.valueOf(partNumber++) + ".pdf");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException();
                }
            }
        }.splitBySize(200000);

        for (PdfDocument doc : splitDocuments)
            doc.close();
        pdfDoc.close();
    }
}
