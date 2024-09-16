package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.pdf.PdfAConformance;
import com.itextpdf.kernel.pdf.PdfOutputIntent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HtmlToPdfA3Convert {
    public static final String SRC = "./src/main/resources/pdfhtml/HtmlToPdfA3Convert/";
    public static final String DEST = "./target/sandbox/pdfhtml/HtmlToPdfA3Convert.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new HtmlToPdfA3Convert().manipulatePdf(DEST);
    }

    public void manipulatePdf(String pdfDest) throws IOException {
        String htmlSource = SRC + "MixedContent.html";

        InputStream inputStream = new FileInputStream(SRC + "sRGB Color Space Profile.icm");

        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri(SRC);
        converterProperties.setPdfAConformance(PdfAConformance.PDF_A_3B);
        converterProperties.setDocumentOutputIntent(
                new PdfOutputIntent("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1",
                        inputStream));


        converterProperties.setFontProvider(new DefaultFontProvider(false, true, false));

        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);
    }
}
