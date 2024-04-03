package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.samples.sandbox.pdfhtml.formtagging.FormTagWorkerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PdfHtmlFormTagging {
    public static final String SRC = "./src/main/resources/pdfhtml/PdfHtmlFormTagging/changeFormRole.html";
    public static final String DEST = "./target/sandbox/pdfhtml/changeFormRole.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfHtmlFormTagging().convertToPdf(DEST, SRC);
    }

    private void convertToPdf(String dest, String src) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties();
        DefaultTagWorkerFactory tagWorkerFactory = new FormTagWorkerFactory();
        converterProperties.setTagWorkerFactory(tagWorkerFactory);

        PdfWriter taggedWriter = new PdfWriter(dest);
        PdfDocument pdfTagged = new PdfDocument(taggedWriter);
        pdfTagged.setTagged();

        HtmlConverter.convertToPdf(new FileInputStream(src), pdfTagged, converterProperties);
    }
}
