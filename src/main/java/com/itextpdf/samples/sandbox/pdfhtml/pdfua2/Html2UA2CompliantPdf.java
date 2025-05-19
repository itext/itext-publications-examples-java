package com.itextpdf.samples.sandbox.pdfhtml.pdfua2;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfUAConformance;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.xmp.XMPException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Html2UA2CompliantPdf {

    public static final String DEST = "./target/sandbox/pdfua2/html2UA2CompliantPdf.pdf";
    public static final String SRC = "./src/main/resources/pdfhtml/pdfua2/";

    public static void main(String[] args) throws IOException, XMPException {
        String currentSrc = SRC + "html2UA2CompliantPdf.html";
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Html2UA2CompliantPdf().manipulatePdf(currentSrc, DEST);
    }

    public void manipulatePdf(String htmlSource, String pdfDest) throws IOException, XMPException {
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setPdfUAConformance(PdfUAConformance.PDF_UA_2);
        WriterProperties writerProperties = new WriterProperties();
        writerProperties.setPdfVersion(PdfVersion.PDF_2_0);
        FileInputStream fileInputStream = new FileInputStream(htmlSource);
        try (PdfWriter pdfWriter = new PdfWriter(pdfDest, writerProperties)) {
            HtmlConverter.convertToPdf(fileInputStream, pdfWriter, converterProperties);
        }
    }
}
