/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.pdfHTML;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensekey.LicenseKey;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class HtmlParser {

    private static final String BASE_IN = "C:\\Temp\\html2pdf";

    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile("C:\\Code\\iText\\iText-7\\html2pdf\\src\\test\\resources\\com\\itextpdf\\html2pdf\\itextkey-html2pdf_typography.xml");


        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setTagWorkerFactory(new CustomTagWorkerFactory());

        HtmlConverter.convertToPdf(
                new FileInputStream("C:\\Temp\\qr\\qrcode.html"),
                new FileOutputStream("C:\\Temp\\qr\\out.pdf"),
                converterProperties);
    }
}
