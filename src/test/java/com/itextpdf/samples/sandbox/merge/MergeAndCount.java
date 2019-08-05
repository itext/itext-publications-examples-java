/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class MergeAndCount {

    public static final String DEST = "./target/sandbox/merge/splitDocument1_<pageNumber>.pdf";

    public static final String PAGE_NUMBER_TAG = "<pageNumber>";
    public static final String RESOURCE
            = "./src/test/resources/pdfs/Wrong.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MergeAndCount().manipulatePdf(DEST);
    }

    public void manipulatePdf(final String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(RESOURCE));

        List<PdfDocument> splitDocuments = new PdfSplitter(pdfDoc) {
            int partNumber = 1;

            @Override
            protected PdfWriter getNextPdfWriter(PageRange documentPageRange) {
                try {
                    return new PdfWriter(dest.replace(PAGE_NUMBER_TAG, String.valueOf(partNumber++)));
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
