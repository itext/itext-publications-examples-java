/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to a question on StackOverflow
 * <p>
 * When concatenating documents, we add a named destination every time
 * a new document is started. After we've finished merging, we add an extra
 * page with the table of contents and links to the named destinations.
 */
package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class MergeWithOutlines extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/merge/merge_with_outlines.pdf";
    public static final String SRC1
            = "./src/test/resources/pdfs/hello.pdf";
    public static final String SRC2
            = "./src/test/resources/pdfs/links1.pdf";
    public static final String SRC3
            = "./src/test/resources/pdfs/links2.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MergeWithOutlines().manipulatePdf(DEST);
    }

    public void manipulatePdf(String destFolder) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        PdfMerger merger = new PdfMerger(pdfDoc);
        PdfOutline rootOutline = pdfDoc.getOutlines(false);

        int page = 1;

        PdfDocument srcDoc1 = new PdfDocument(new PdfReader(SRC1));
        merger.merge(srcDoc1, 1, srcDoc1.getNumberOfPages());

        PdfDocument srcDoc2 = new PdfDocument(new PdfReader(SRC2));
        PdfDocument srcDoc3 = new PdfDocument(new PdfReader(SRC3));
        merger.setCloseSourceDocuments(true)
                .merge(srcDoc2, 1, srcDoc2.getNumberOfPages())
                .merge(srcDoc3, 1, srcDoc3.getNumberOfPages());

        PdfOutline helloWorld = rootOutline.addOutline("Hello World");
        helloWorld.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(page)));
        page += srcDoc1.getNumberOfPages();

        PdfOutline link1 = helloWorld.addOutline("link1");
        link1.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(page)));
        page += srcDoc1.getNumberOfPages();

        PdfOutline link2 = rootOutline.addOutline("Link 2");
        link2.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(page)));

        srcDoc1.close();
        pdfDoc.close();
    }
}
