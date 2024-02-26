package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.samples.sandbox.pdfhtml.headertagging.AccessibilityTagWorkerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateAccessiblePDF {
    public static final String SRC = "./src/main/resources/pdfhtml/AccessiblePDF/";
    public static final String DEST = "./target/sandbox/pdfhtml/Accessibility.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        String htmlSource = SRC + "Accessibility.html";

        new CreateAccessiblePDF().manipulatePdf(htmlSource, DEST);
    }

    public void manipulatePdf(String src, String dest) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(dest);
        WriterProperties writerProperties = new WriterProperties();
        writerProperties.addXmpMetadata();

        PdfWriter pdfWriter = new PdfWriter(outputStream, writerProperties);
        PdfDocument pdfDoc = new PdfDocument(pdfWriter);
        pdfDoc.getCatalog().setLang(new PdfString("en-US"));

        pdfDoc.setTagged();
        pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));

        PdfDocumentInfo pdfMetaData = pdfDoc.getDocumentInfo();
        pdfMetaData.setAuthor("Samuel Huylebroeck");
        pdfMetaData.addCreationDate();
        pdfMetaData.getProducer();
        pdfMetaData.setCreator("iText Software");
        pdfMetaData.setKeywords("example, accessibility");
        pdfMetaData.setSubject("PDF accessibility");
        // Title is derived from html

        // pdf conversion
        FontProvider fontProvider = new FontProvider();
        fontProvider.addStandardPdfFonts();
        // The noto-nashk font file (.ttf extension) is placed in the resources
        fontProvider.addDirectory(SRC);

        ConverterProperties props = new ConverterProperties();
        props.setFontProvider(fontProvider);
        // Base URI is required to resolve the path to source files
        props.setBaseUri(SRC);

        // Setup custom tagworker factory for better tagging of headers
        DefaultTagWorkerFactory tagWorkerFactory = new AccessibilityTagWorkerFactory();
        props.setTagWorkerFactory(tagWorkerFactory);

        HtmlConverter.convertToPdf(new FileInputStream(src), pdfDoc, props);

        pdfDoc.close();
    }
}
