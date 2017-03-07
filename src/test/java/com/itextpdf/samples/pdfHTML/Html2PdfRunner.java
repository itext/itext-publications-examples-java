/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.pdfHTML;


import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.html2pdf.css.media.MediaDeviceDescription;
import com.itextpdf.html2pdf.css.media.MediaType;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.samples.pdfHTML.ColourBlindness.ColourBlindnessCssApplierFactory;
import com.itextpdf.samples.pdfHTML.ColourBlindness.ColourBlindnessTransforms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by SamuelHuylebroeck on 10/28/2016.
 */
public class Html2PdfRunner {


    public static final String sourceFolder = "src/test/resources/Develop/Html2Pdf/";
    public static final String destinationFolder = "target/output/Develop/Html2Pdf/";
    //public static final String[] files = {"HelloWorld", "rainbow","arabic", "simple", "Blogpost", "lists", "media", "index", "listbug", "tables", "qrcode","boldStyle"};
    public static final String[] files = {"tables"};

    public static void main(String[] args) throws IOException, InterruptedException {
        LicenseKey.loadLicenseFile("src/test/resources/itextkey-html2pdf_typography.xml");
        //LicenseKey.loadLicenseFile("src/test/resources/pdfHtml.xml");
        for (String name : files) {
            String htmlSource = sourceFolder + name + "/" + name + ".html";
            String resourceFolder = sourceFolder + name + "/";
            String pdfDest = destinationFolder + name + ".pdf";
            String pdfAsPrintDest = destinationFolder + name + "_print.pdf";
            String pdfColourBLind = destinationFolder+name+ "_colourblind.pdf";
            String pdfQrCode = destinationFolder+name+ "_qrcode.pdf";
            File file = new File(pdfDest);

            System.out.println("Parsing: " + htmlSource);
            file.getParentFile().mkdirs();
            new Html2PdfRunner().parseSimple(htmlSource, pdfDest, resourceFolder);
            //new Html2PdfRunner().parseAsPrint(htmlSource, pdfAsPrintDest, resourceFolder);
            //new Html2PdfRunner().parseColourBlind(htmlSource,pdfColourBLind,resourceFolder);
            new Html2PdfRunner().parseQrCode(htmlSource,pdfQrCode,resourceFolder);
        }
    }

    public void parseSimple(String htmlSource, String pdfDest, String resoureLoc) throws IOException, InterruptedException {

        File pdf = new File(pdfDest);
        pdf.getParentFile().mkdirs();

        ConverterProperties converterProperties = new ConverterProperties().setBaseUri(resoureLoc);
        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);

    }


    public void parseAsPrint(String htmlSource, String pdfDest, String resoureLoc) throws IOException, InterruptedException {
        File pdf = new File(pdfDest);
        pdf.getParentFile().mkdirs();

        ConverterProperties converterProperties = new ConverterProperties().setBaseUri(resoureLoc);
        converterProperties.setMediaDeviceDescription(new MediaDeviceDescription(MediaType.PRINT));
        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);

    }

    public void parseColourBlind(String htmlSource, String pdfDest, String resoureLoc) throws IOException, InterruptedException {
        File pdf = new File(pdfDest);
        pdf.getParentFile().mkdirs();
        ConverterProperties converterProperties = new ConverterProperties().setBaseUri(resoureLoc);
        DefaultCssApplierFactory cssApplierFactory = new ColourBlindnessCssApplierFactory(ColourBlindnessTransforms.DEUTERANOMALY);
        converterProperties.setCssApplierFactory(cssApplierFactory);
        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);
    }



    public void parseQrCode(String htmlSource, String pdfDest, String resoureLoc) throws IOException, InterruptedException {
        File pdf = new File(pdfDest);
        pdf.getParentFile().mkdirs();
        ConverterProperties converterProperties = new ConverterProperties().setBaseUri(resoureLoc);

        DefaultTagWorkerFactory tagWorkerFactory = new QRCodeTagWorkerFactory();
        converterProperties.setTagWorkerFactory(tagWorkerFactory);


        DefaultCssApplierFactory cssApplierFactory = new QRCodeTagCssApplierFactory();
        converterProperties.setCssApplierFactory(cssApplierFactory);

        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);
    }

}



