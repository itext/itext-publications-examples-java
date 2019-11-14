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
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ParseHtmlAsPrint {
    public static final String SRC = "./src/test/resources/pdfHTML/media/";
    public static final String DEST = "./target/sandbox/pdfHTML/rainbow_asPrint.pdf";

    public static void main(String[] args) throws IOException {
        String currentSrc = SRC + "rainbow.html";
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ParseHtmlAsPrint().manipulatePdf(currentSrc, DEST, SRC);
    }

    public void manipulatePdf(String htmlSource, String pdfDest, String resourceLoc) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties().setBaseUri(resourceLoc);

        // Set media device type to correctly parsing html with media handling
        converterProperties.setMediaDeviceDescription(new MediaDeviceDescription(MediaType.PRINT));

        try (FileInputStream fileInputStream = new FileInputStream(htmlSource);
             FileOutputStream fileOutputStream = new FileOutputStream(pdfDest)) {
            HtmlConverter.convertToPdf(fileInputStream, fileOutputStream, converterProperties);
        }
    }
}
