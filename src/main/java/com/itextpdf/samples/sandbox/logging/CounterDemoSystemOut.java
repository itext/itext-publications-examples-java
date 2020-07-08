package com.itextpdf.samples.sandbox.logging;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.counter.EventCounterHandler;
import com.itextpdf.kernel.counter.SystemOutEventCounterFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CounterDemoSystemOut {
    public static final String DEST = "./target/sandbox/logging/CounterDemoSystemOut.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CounterDemoSystemOut().manipulatePdf();
    }

    protected void manipulatePdf() throws IOException {

        // Implement default SystemOut factory and register it
        SystemOutEventCounterFactory counterFactory = new SystemOutEventCounterFactory();
        EventCounterHandler.getInstance().register(counterFactory);

        // Generate 3 core events by creating 3 pdf documents
        for (int i = 0; i < 3; i++) {
            createPdf();
        }

        String html = "<p>iText</p>";

        // Generate 2 events (core and html-convert) by converting html to pdf: the first during pdf document creation,
        // the second one during conversion
        convertToPdf(html);

        EventCounterHandler.getInstance().unregister(counterFactory);
    }

    private static void createPdf() throws FileNotFoundException {
        Document document = new Document(new PdfDocument(new PdfWriter(DEST)));
        document.add(new Paragraph("Hello World!"));
        document.close();
    }

    private static void convertToPdf(String html) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new ByteArrayOutputStream()));
        HtmlConverter.convertToPdf(html, pdfDocument, new ConverterProperties());
    }
}
