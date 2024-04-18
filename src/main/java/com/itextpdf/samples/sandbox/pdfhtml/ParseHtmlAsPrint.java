package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ParseHtmlAsPrint {
    public static final String SRC = "./src/main/resources/pdfhtml/media/";
    public static final String DEST = "./target/sandbox/pdfhtml/rainbow_asPrint.pdf";

    public static void main(String[] args) throws IOException {
        String currentSrc = SRC + "rainbow.html";
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ParseHtmlAsPrint().manipulatePdf(currentSrc, DEST, SRC);
    }

    public void manipulatePdf(String htmlSource, String pdfDest, String resourceLoc) throws IOException {
        // Base URI is required to resolve the path to source files
        ConverterProperties converterProperties = new ConverterProperties().setBaseUri(resourceLoc);

        // Set media device type to correctly parsing html with media handling
        converterProperties.setMediaDeviceDescription(new MediaDeviceDescription(MediaType.PRINT));

        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);
    }
}
