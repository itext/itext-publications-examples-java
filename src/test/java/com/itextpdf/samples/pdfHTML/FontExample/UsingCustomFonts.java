/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.pdfHTML.FontExample;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UsingCustomFonts {

    public static final String sourceFolder = "src/test/resources/Develop/pdfHTML/";
    public static final String destinationFolder = "target/output/Develop/pdfHTML/";
    public static final String LICENSE = "<Path to license key>";
    public static final String[] files = {"FontExample"};
    public static final String STARWARS = "Aurebesh Bold.ttf";
    public static final String SINDARIN = "SINDAR.TTF";


    public static void main(String[] args) throws IOException, InterruptedException {
        LicenseKey.loadLicenseFile(LICENSE);
        for (String name : files) {
            String htmlSource = sourceFolder + name + ".html";
            String resourceFolder = sourceFolder;
            String fontFolder = sourceFolder+"font/";
            String fontOne = sourceFolder + SINDARIN;
            String fontTwo = sourceFolder + STARWARS;
            String pdfDest = destinationFolder + name + ".pdf";
            File file = new File(pdfDest);

            System.out.println("Parsing: " + htmlSource);
            file.getParentFile().mkdirs();

            new UsingCustomFonts().createPDF(htmlSource, pdfDest, resourceFolder,fontFolder,fontOne,fontTwo);
        }
    }

    public void createPDF(String htmlSource, String pdfDest, String resourceLoc, String fontFolder, String fontNameOne, String fontNameTwo) throws IOException, InterruptedException {
        File pdf = new File(pdfDest);
        pdf.getParentFile().mkdirs();

        PdfWriter writer  = new PdfWriter(pdfDest);
        PdfDocument pdfDoc = new PdfDocument(writer);
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri(resourceLoc);

        //Default provider will register standard fonts and free fonts shipped with iText, but not system fonts
        FontProvider provider = new DefaultFontProvider();

        //1. Register all fonts in a directory
        provider.addDirectory(fontFolder);
        //2. Register a single font by specifying path and encoding
        provider.addFont(fontNameOne,"UTF-8");
        //3. Use the raw bytes of the font file
        Path fontPathTwo  = Paths.get(fontNameTwo);
        byte[] fontBytes = Files.readAllBytes(fontPathTwo);
        provider.addFont(fontBytes);

        //Make sure the provider is used
        converterProperties.setFontProvider(provider);
        try (FileInputStream fileInputStream = new FileInputStream(htmlSource)) {
            HtmlConverter.convertToPdf(fileInputStream, pdfDoc, converterProperties);
        }
        pdfDoc.close();

    }
}
