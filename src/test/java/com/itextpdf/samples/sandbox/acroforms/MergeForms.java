/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to
 * http://stackoverflow.com/questions/26174675/copy-pdf-with-annotations-using-itext
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Category(SampleTest.class)
public class MergeForms extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/merge_forms.pdf";
    public static final String SRC1 = "./src/test/resources/pdfs/subscribe.pdf";
    public static final String SRC2 = "./src/test/resources/pdfs/state.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MergeForms().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfReader[] readers = {
                new PdfReader(getFile1()),
                new PdfReader(getFile2())
        };
        manipulatePdf(dest, readers);
    }

    protected void manipulatePdf(String dest, PdfReader[] readers) throws FileNotFoundException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.initializeOutlines();
        for (PdfReader reader : readers) {
            PdfDocument readerDoc = new PdfDocument(reader);
            readerDoc.copyPagesTo(1, readerDoc.getNumberOfPages(), pdfDoc, new PdfPageFormCopier());
            readerDoc.close();
        }
        pdfDoc.close();
    }

    public String getFile1() {
        return SRC1;
    }

    public String getFile2() {
        return SRC2;
    }
}
