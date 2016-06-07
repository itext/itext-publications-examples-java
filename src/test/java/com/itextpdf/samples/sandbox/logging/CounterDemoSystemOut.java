/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.logging;

import com.itextpdf.kernel.log.CounterFactory;
import com.itextpdf.kernel.log.DefaultCounter;
import com.itextpdf.kernel.log.SystemOutCounter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class CounterDemoSystemOut extends GenericTest {

    public static final String SRC = "./target/test/resources/sandbox/logging/hello2.pdf";
    public static final String DEST = "./target/test/resources/sandbox/logging/stamp2.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CounterDemoSystemOut().manipulatePdf(DEST);
    }

    private void createPdf() throws FileNotFoundException {
        Document document = new Document(new PdfDocument(new PdfWriter(SRC)));
        document.add(new Paragraph("Hello World!"));
        document.close();
    }

    @Override
    public void manipulatePdf(String dest) throws IOException {
        CounterFactory.getInstance().setCounter(new SystemOutCounter());

        createPdf();
        PdfReader reader = new PdfReader(SRC);
        PdfDocument pdfDocument = new PdfDocument(reader, new PdfWriter(dest));
        Document document = new Document(pdfDocument).showTextAligned(new Paragraph("Stamped text"), 559, 806, TextAlignment.RIGHT);
        document.close();

        CounterFactory.getInstance().setCounter(new DefaultCounter());
    }

}
