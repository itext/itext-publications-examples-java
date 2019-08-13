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
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.samples.pdfhtml.colorblindness.ColorBlindnessCssApplierFactory;
import com.itextpdf.samples.pdfhtml.colorblindness.ColorBlindnessTransforms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ParseHtmlColorBlind {
    public static final String SRC = "./src/test/resources/pdfHTML/rainbow/";
    public static final String DEST = "./target/sandbox/pdfHTML/rainbow_colourBlind.pdf";

    public static void main(String[] args) throws IOException {
        String currentSrc = SRC + "rainbow.html";
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ParseHtmlColorBlind().manipulatePdf(currentSrc, DEST, SRC);
    }

    public void manipulatePdf(String htmlSource, String pdfDest, String resourceLoc) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties().setBaseUri(resourceLoc);

        // Create custom css applier factory.
        // Current custom css applier factory handle <div> and <span> tags of html and returns corresponding css applier.
        // All of that css appliers change value of RGB colors
        // to simulate color blindness of people (like Tritanopia, Achromatopsia, etc.)
        DefaultCssApplierFactory cssApplierFactory = new ColorBlindnessCssApplierFactory(ColorBlindnessTransforms.DEUTERANOMALY);
        converterProperties.setCssApplierFactory(cssApplierFactory);

        try (FileInputStream fileInputStream = new FileInputStream(htmlSource);
             FileOutputStream fileOutputStream = new FileOutputStream(pdfDest)) {
            HtmlConverter.convertToPdf(fileInputStream, fileOutputStream, converterProperties);
        }
    }
}
