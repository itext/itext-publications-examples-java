package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.pdfa.PdfADocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HtmlToPdfAConvert {
    public static final String SRC = "./src/main/resources/pdfhtml/HtmlToPdfAConvert/";
    public static final String DEST = "./target/sandbox/pdfhtml/HtmlToPdfAConvert.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new HtmlToPdfAConvert().manipulatePdf(DEST);
    }

    public void manipulatePdf(String pdfDest) throws IOException {
        String htmlSource = SRC + "hello.html";

        InputStream inputStream = new FileInputStream(SRC + "sRGB Color Space Profile.icm");

        ConverterProperties converterProperties = new ConverterProperties();

        // Pdf/A files should have only embedded fonts inside. That's why the standard pdf fonts should be removed from
        // the FontProvider, which contains fonts to be used during conversion
        converterProperties.setFontProvider(new DefaultFontProvider(false, true, false));

        PdfADocument pdfADocument = new PdfADocument(new PdfWriter(pdfDest), PdfAConformanceLevel.PDF_A_1B,
                new PdfOutputIntent("Custom", "", "http://www.color.org",
                        "sRGB IEC61966-2.1", inputStream));

        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), pdfADocument, converterProperties);
    }
}
