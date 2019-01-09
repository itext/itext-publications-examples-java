/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.pdfHTML.ResponsiveDesign;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;
import com.itextpdf.styledxmlparser.css.util.CssUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class pdfHTMLResponsiveDesign {

    public static final String sourceFolder = "src/test/resources/pdfHTML/ResponsiveDesign/";
    public static final String destinationFolder = "target/output/pdfHTML/ResponsiveDesign/";
    public static final String[] files = {"responsive"};

    public static final PageSize[] pageSizes = {
            PageSize.A4.rotate(),
            new PageSize(720, PageSize.A4.getHeight()),
            new PageSize(PageSize.A5.getWidth(), PageSize.A4.getHeight())
    };

    //Path to the license file
    public static final String LICENSE = "src/test/resources/pdfHTML/itextkey-html2pdf_typography.xml";

    public static void main(String[] args) throws IOException, InterruptedException {
        LicenseKey.loadLicenseFile(LICENSE);
        for (String name : files) {
            String htmlSource = sourceFolder + name + "/" + name + ".html";
            String resourceFolder = sourceFolder + name + "/";
            String pdfDest = destinationFolder + name + "/" + name + ".pdf";
            File file = new File(pdfDest);

            System.out.println("Parsing: " + htmlSource);
            file.getParentFile().mkdirs();
            pdfHTMLResponsiveDesign runner = new pdfHTMLResponsiveDesign();
            //Create a pdf for each page size
            for (int i = 0; i < pageSizes.length; i++) {
                float width = CssUtils.parseAbsoluteLength("" + pageSizes[i].getWidth());
                String dest = destinationFolder + name + "/" + width + "/" + name + "_" + width + ".pdf";
                runner.parseMedia(htmlSource, dest, resourceFolder, pageSizes[i], width);
            }

        }
    }

    public void parseMedia(String htmlSource, String pdfDest, String resourceLoc, PageSize pageSize, float screenWidth) throws IOException {
        File pdf = new File(pdfDest);
        pdf.getParentFile().mkdirs();

        PdfWriter writer = new PdfWriter(pdfDest);
        PdfDocument pdfDoc = new PdfDocument(writer);

        //Set the result to be tagged
        pdfDoc.setTagged();
        pdfDoc.setDefaultPageSize(pageSize);

        ConverterProperties converterProperties = new ConverterProperties();

        //Set media device description details
        MediaDeviceDescription mediaDescription = new MediaDeviceDescription(MediaType.SCREEN);
        mediaDescription.setWidth(screenWidth);
        converterProperties.setMediaDeviceDescription(mediaDescription);

        FontProvider fp = new FontProvider();
        fp.addStandardPdfFonts();
        //Register external font directory
        fp.addDirectory(resourceLoc);

        converterProperties.setFontProvider(fp);
        converterProperties.setBaseUri(resourceLoc);

        //Create acroforms from text and button input fields
        converterProperties.setCreateAcroForm(true);

        try (FileInputStream fileInputStream = new FileInputStream(htmlSource)) {
            HtmlConverter.convertToPdf(fileInputStream, pdfDoc, converterProperties);
        }
        pdfDoc.close();
    }
}



