package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.samples.sandbox.pdfhtml.qrcodetag.QRCodeTagCssApplierFactory;
import com.itextpdf.samples.sandbox.pdfhtml.qrcodetag.QRCodeTagWorkerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ParseHtmlQRcode {
    public static final String SRC = "./src/main/resources/pdfhtml/qrcode/";
    public static final String DEST = "./target/sandbox/pdfhtml/qrcode.pdf";

    public static void main(String[] args) throws IOException {
        String currentSrc = SRC + "qrcode.html";
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ParseHtmlQRcode().manipulatePdf(currentSrc, DEST, SRC);
    }

    public void manipulatePdf(String htmlSource, String pdfDest, String resourceLoc) throws IOException {

        // Create custom tagworker factory
        // The tag <qr> is mapped on a QRCode tagworker. Every other tag is mapped to the default.
        // The tagworker processes a <qr> tag using iText Barcode functionality
        DefaultTagWorkerFactory tagWorkerFactory = new QRCodeTagWorkerFactory();

        // Creates custom css applier factory
        // The tag <qr> is mapped on a BlockCssApplier. Every other tag is mapped to the default.
        DefaultCssApplierFactory cssApplierFactory = new QRCodeTagCssApplierFactory();

        ConverterProperties converterProperties = new ConverterProperties()
                // Base URI is required to resolve the path to source files
                .setBaseUri(resourceLoc)
                .setTagWorkerFactory(tagWorkerFactory)
                .setCssApplierFactory(cssApplierFactory);

       HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);
    }
}
