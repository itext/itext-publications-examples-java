/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21617218/itext-or-itextsharp-rudimentary-text-edit
 * <p>
 * This is only a partial answer. It's a quick and dirty method showing how to
 * change a stream inside a PDF. Obviously, you'll have to detect words that are
 * stored in Form XObjects too, and you can seriously screw up the layout when
 * you manipulate the content stream as is done in this example.
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class ReplaceStream extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/replace_stream.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ReplaceStream().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        PdfPage page = pdfDoc.getFirstPage();
        PdfDictionary dict = page.getPdfObject();
        PdfObject object = dict.get(PdfName.Contents);
        if (object instanceof PdfStream) {
            PdfStream stream = (PdfStream) object;
            byte[] data = stream.getBytes();
            stream.setData(new String(data).replace("Hello World", "HELLO WORLD").getBytes("UTF-8"));
        }
        pdfDoc.close();
    }
}
