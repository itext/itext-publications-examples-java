/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * https://www.linkedin.com/groups/Script-Change-Author-Name-Comments-159987.S.5984062085800144899
 */
package com.itextpdf.samples.sandbox.parse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Category(SampleTest.class)
public class ParseCzech {
    public static final String DEST = "./target/test/resources/sandbox/parse/czech.txt";
    public static final String SRC = "./src/test/resources/pdfs/czech.pdf";

    @BeforeClass
    public static void beforeClass() throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ParseCzech().manipulatePdf();
    }

    @Test
    public void manipulatePdf() throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));
        FileOutputStream fos = new FileOutputStream(DEST);

        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        PdfCanvasProcessor parser = new PdfCanvasProcessor(strategy);
        parser.processPageContent(pdfDoc.getFirstPage());
        byte[] array = strategy.getResultantText().getBytes("UTF-8");
        fos.write(array);

        fos.flush();
        fos.close();

        pdfDoc.close();

        Assert.assertEquals(67, array.length);
    }
}
