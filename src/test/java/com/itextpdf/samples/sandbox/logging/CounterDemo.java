/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.logging;

import com.itextpdf.kernel.log.Counter;
import com.itextpdf.kernel.log.CounterFactory;
import com.itextpdf.kernel.log.DefaultCounter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.junit.Assert;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class CounterDemo extends GenericTest {
    public static final String SRC = "./target/test/resources/sandbox/logging/hello.pdf";
    public static final String DEST = "./target/test/resources/sandbox/logging/stamp.pdf";
    public static final String LOG_RESULT = "./target/test/resources/sandbox/logging/log.txt";
    public static final String CMP_LOG = "./src/test/resources/sandbox/logging/cmp_log.txt";

    MyCounter myCounter;

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CounterDemo().manipulatePdf(DEST);
    }

    private void createPdf() throws FileNotFoundException {
        Document document = new Document(new PdfDocument(new PdfWriter(SRC)));
        document.add(new Paragraph("Hello World!"));
        document.close();
    }

    @Override
    public void manipulatePdf(String dest) throws IOException {
        myCounter = new MyCounter(getClass());
        CounterFactory.getInstance().setCounter(myCounter);

        createPdf();
        PdfReader reader = new PdfReader(SRC);
        PdfDocument pdfDocument = new PdfDocument(reader, new PdfWriter(dest));
        Document document = new Document(pdfDocument).showTextAligned(new Paragraph("Stamped text"), 559, 806, TextAlignment.RIGHT);
        document.close();

        myCounter.close();
        CounterFactory.getInstance().setCounter(new DefaultCounter());

        compareTxt(CMP_LOG, LOG_RESULT);
    }

    public class MyCounter implements Counter {

        protected FileWriter writer;
        protected String yourClass;
        protected String iTextClass;

        public MyCounter(Class<?> klass) throws IOException {
            this.yourClass = klass.getName();
            writer = new FileWriter(LOG_RESULT, false);
        }

        private MyCounter(Class<?> klass, String yourClass, FileWriter writer)
                throws IOException {
            this.yourClass = yourClass;
            this.iTextClass = klass.getName();
            this.writer = writer;
        }

        @Override
        public Counter getCounter(Class<?> klass) {
            try {
                return new MyCounter(klass, yourClass, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onDocumentRead(long size) {
            if (writer == null)
                throw new RuntimeException("No writer defined!");
            try {
                writer.write(String.format(
                        "[%s:%s] %s: %s read\n", yourClass, iTextClass, new Date().toString(), size));
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onDocumentWritten(long size) {
            if (writer == null)
                throw new RuntimeException("No writer defined!");
            try {
                writer.write(String.format(
                        "[%s:%s] %s: %s written\n", yourClass, iTextClass, new Date().toString(), size));
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void close() throws IOException {
            writer.close();
        }
    }

    private void compareTxt(String cmp, String out) throws IOException {
        Assert.assertEquals(fileToString(cmp), fileToString(out));
    }

    private String fileToString(String filePath) throws IOException {
        FileInputStream fin = new FileInputStream(filePath);
        BufferedReader myInput = new BufferedReader(new InputStreamReader(fin));
        StringBuilder sb = new StringBuilder();
        String thisLine;
        while ((thisLine = myInput.readLine()) != null) {
            if (thisLine.length() > 10) {
                sb.append(thisLine.substring(thisLine.length() - 10, thisLine.length()));
            }
        }
        return sb.toString();
    }

}
