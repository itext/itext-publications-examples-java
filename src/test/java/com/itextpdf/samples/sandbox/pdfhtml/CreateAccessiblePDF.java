/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
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
import com.itextpdf.samples.pdfhtml.accessiblepdf.headertagging.AccessibilityTagWorkerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateAccessiblePDF {
    public static final String SRC = "./src/test/resources/pdfHTML/AccessiblePDF/";
    public static final String DEST = "./target/sandbox/pdfHTML/Accessibility.pdf";

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

        //Title is derived from html

        // pdf conversion
        ConverterProperties props = new ConverterProperties();
        FontProvider fontProvider = new FontProvider();
        fontProvider.addStandardPdfFonts();
        fontProvider.addDirectory(SRC);

        //The noto-nashk font file (.ttf extension) is placed in the resources
        props.setFontProvider(fontProvider);
        props.setBaseUri(SRC);

        //Setup custom tagworker factory for better tagging of headers
        DefaultTagWorkerFactory tagWorkerFactory = new AccessibilityTagWorkerFactory();
        props.setTagWorkerFactory(tagWorkerFactory);

        try (FileInputStream fileInputStream = new FileInputStream(src)) {
            HtmlConverter.convertToPdf(fileInputStream, pdfDoc, props);
        }

        pdfDoc.close();
    }
}
