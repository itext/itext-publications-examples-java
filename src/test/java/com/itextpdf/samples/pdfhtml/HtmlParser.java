/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class HtmlParser {

    private static final String BASE_IN = "C:\\itext\\java\\examples\\src\\test\\resources\\pdfHTML\\qrcode";

    public static void main(String[] args) throws IOException {


        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setTagWorkerFactory(new CustomTagWorkerFactory());

        HtmlConverter.convertToPdf(
                new FileInputStream("C:\\itext\\java\\examples\\src\\test\\resources\\pdfHTML\\qrcode\\qrcode.html"),
                new FileOutputStream("C:\\itext\\java\\examples\\src\\test\\resources\\pdfHTML\\qrcode\\out.pdf"),
                converterProperties);
    }
}
