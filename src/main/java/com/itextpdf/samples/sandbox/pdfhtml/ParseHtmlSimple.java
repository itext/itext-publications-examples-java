package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ParseHtmlSimple {
    public static final String SRC = "./src/main/resources/pdfhtml/rainbow/";
    public static final String DEST = "./target/sandbox/pdfhtml/rainbow_simple.pdf";

    public static void main(String[] args) throws IOException {
        String currentSrc = SRC + "rainbow.html";
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ParseHtmlSimple().manipulatePdf(currentSrc, DEST, SRC);
    }

    public void manipulatePdf(String htmlSource, String pdfDest, String resourceLoc) throws IOException {
        // Base URI is required to resolve the path to source files
        ConverterProperties converterProperties = new ConverterProperties().setBaseUri(resourceLoc);

        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);
    }
}