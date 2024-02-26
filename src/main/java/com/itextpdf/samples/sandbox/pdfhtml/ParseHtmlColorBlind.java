package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.samples.sandbox.pdfhtml.colorblindness.ColorBlindnessCssApplierFactory;
import com.itextpdf.samples.sandbox.pdfhtml.colorblindness.ColorBlindnessTransforms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ParseHtmlColorBlind {
    public static final String SRC = "./src/main/resources/pdfhtml/rainbow/";
    public static final String DEST = "./target/sandbox/pdfhtml/rainbow_colourBlind.pdf";

    public static void main(String[] args) throws IOException {
        String currentSrc = SRC + "rainbow.html";
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ParseHtmlColorBlind().manipulatePdf(currentSrc, DEST, SRC);
    }

    public void manipulatePdf(String htmlSource, String pdfDest, String resourceLoc) throws IOException {
        // Base URI is required to resolve the path to source files
        ConverterProperties converterProperties = new ConverterProperties().setBaseUri(resourceLoc);

        // Create custom css applier factory.
        // Current custom css applier factory handle <div> and <span> tags of html and returns corresponding css applier.
        // All of that css appliers change value of RGB colors
        // to simulate color blindness of people (like Tritanopia, Achromatopsia, etc.)
        DefaultCssApplierFactory cssApplierFactory =
                new ColorBlindnessCssApplierFactory(ColorBlindnessTransforms.DEUTERANOMALY);
        converterProperties.setCssApplierFactory(cssApplierFactory);

        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);
    }
}
