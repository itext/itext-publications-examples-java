/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.pdfHTML.AccessiblePDF;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.samples.pdfHTML.AccessiblePDF.HeaderTagging.AccessibilityTagWorkerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class CreateAccessiblePDF {

    public static final String sourceFolder = "src/test/resources/pdfHTML/AccessiblePDF/";
    public static final String destinationFolder = "target/output/pdfHTML/AccessiblePDF/";
    public static final String[] files = {"Accessibility"};
    //License key path
    public static final String LICENSE = "src/test/resources/pdfHTML/itextkey-html2pdf_typography.xml";


    public static void main(String[] args) throws IOException, InterruptedException {
        LicenseKey.loadLicenseFile(LICENSE);
        for (String name : files) {
            String htmlSource = sourceFolder + name  + ".html";
            String resourceFolder = sourceFolder;
            String pdfDest = destinationFolder + name + ".pdf";
            File file = new File(pdfDest);

            System.out.println("Parsing: " + htmlSource);
            file.getParentFile().mkdirs();

            new CreateAccessiblePDF().createPdf(htmlSource,pdfDest,resourceFolder);
        }
    }

    public void createPdf(String src, String dest, String resources) throws IOException {
        try {
            FileOutputStream outputStream = new FileOutputStream(dest);

            WriterProperties writerProperties = new WriterProperties();
            //Add metadata
            writerProperties.addXmpMetadata();

            PdfWriter pdfWriter = new PdfWriter(outputStream, writerProperties);

            PdfDocument pdfDoc = new PdfDocument(pdfWriter);
            pdfDoc.getCatalog().setLang(new PdfString("en-US"));
            //Set the document to be tagged
            pdfDoc.setTagged();
            pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));

            //Set meta tags
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
            FontProvider fp = new FontProvider();
            fp.addStandardPdfFonts();
            fp.addDirectory(resources);//The noto-nashk font file (.ttf extension) is placed in the resources

            props.setFontProvider(fp);
            props.setBaseUri(resources);
            //Setup custom tagworker factory for better tagging of headers
            DefaultTagWorkerFactory tagWorkerFactory = new AccessibilityTagWorkerFactory();
            props.setTagWorkerFactory(tagWorkerFactory);

            try (FileInputStream fileInputStream = new FileInputStream(src)) {
                HtmlConverter.convertToPdf(fileInputStream, pdfDoc, props);
            }
            pdfDoc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
