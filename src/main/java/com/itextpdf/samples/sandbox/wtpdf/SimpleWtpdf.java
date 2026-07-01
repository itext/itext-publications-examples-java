package com.itextpdf.samples.sandbox.wtpdf;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WellTaggedPdfConformance;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.pdfua.wtpdf.WellTaggedPdfConfig;
import com.itextpdf.pdfua.wtpdf.WellTaggedPdfDocument;
import com.itextpdf.styledxmlparser.resolver.font.BasicFontProvider;

import java.io.File;


/*
 * SimpleWtpdf.java
 *
 * Example showing how to create well tagged pdf for reuse compliant document.
 * Demonstrates HTML to PDF conversion.
 */

public class SimpleWtpdf {

    public static final String DEST = "./target/sandbox/wtpdf/pdf_wtpdf.pdf";
    public static final String ARTICLE_SOURCE_FOLDER = "./src/main/resources/articledata/";


    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SimpleWtpdf().manipulatePdf(DEST);
    }

    private void manipulatePdf(String dest) throws Exception {
        PdfWriter writer = new PdfWriter(FileUtil.getFileOutputStream(dest), new WriterProperties().
                setPdfVersion(PdfVersion.PDF_2_0));

        //Just use WellTaggedPdfDocument class, it's that simple!
        PdfDocument pdf = new WellTaggedPdfDocument(writer, new WellTaggedPdfConfig(WellTaggedPdfConformance.FOR_REUSE,
                "well tagged pdf", "en-US"));

        BasicFontProvider fontProvider = new BasicFontProvider(false, false, false);
        fontProvider.addFont(ARTICLE_SOURCE_FOLDER + "NotoSans-Regular.ttf");
        fontProvider.addFont(ARTICLE_SOURCE_FOLDER + "NotoEmoji-Regular.ttf");

        ConverterProperties props = new ConverterProperties().setBaseUri(ARTICLE_SOURCE_FOLDER)
                .setFontProvider(fontProvider);

        HtmlConverter.convertToPdf(FileUtil.getInputStreamForFile(ARTICLE_SOURCE_FOLDER + "article.html"), pdf, props);
        pdf.close();
    }
}
