package com.itextpdf.samples.sandbox.svg;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfUAConformance;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.pdfua.PdfUAConfig;
import com.itextpdf.pdfua.PdfUADocument;
import com.itextpdf.svg.converter.SvgConverter;
import com.itextpdf.svg.processors.impl.SvgConverterProperties;

import java.io.File;
import java.io.IOException;

public class ConvertSvgToUA2Pdf {
    private static final String SRC = "./src/main/resources/svg/";
    public static final String DEST = "./target/sandbox/svg/Svg2UA2CompliantPdf.pdf";

    public static void main(String[] args) throws IOException {
        String svgSource = SRC + "cauldron.svg";
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ConvertSvgToUA2Pdf().manipulatePdf(svgSource, DEST);
    }

    public void manipulatePdf(String svgSource, String pdfDest) throws IOException {
        WriterProperties writerProperties = new WriterProperties();
        writerProperties.setPdfVersion(PdfVersion.PDF_2_0);
        PdfUADocument pdfDocument = new PdfUADocument(
                new PdfWriter(pdfDest, writerProperties),
                new PdfUAConfig(PdfUAConformance.PDF_UA_2, "ua title", "en-US"));

        pdfDocument.addNewPage();
        SvgConverterProperties properties = new SvgConverterProperties();
        properties.getAccessibilityProperties().setAlternateDescription("Hello there");

        SvgConverter.drawOnDocument(FileUtil.getInputStreamForFile(svgSource), pdfDocument, 1, properties);

        PdfPage page = pdfDocument.addNewPage();
        SvgConverter.drawOnPage(FileUtil.getInputStreamForFile(svgSource), page, properties);
        pdfDocument.close();
    }
}
