package com.itextpdf.samples.sandbox.svg;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.svg.converter.SvgConverter;
import com.itextpdf.svg.processors.ISvgConverterProperties;
import com.itextpdf.svg.processors.impl.SvgConverterProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ConvertSvgToPdfPage {
    private static final String SRC = "./src/main/resources/svg/";
    public static final String DEST = "./target/sandbox/svg/SvgToPdfPage.pdf";

    public static void main(String[] args) throws IOException {
        String svgImage = SRC + "cauldron.svg";
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ConvertSvgToPdfPage().manipulatePdf(svgImage, DEST);
    }

    public void manipulatePdf(String svgSource, String pdfDest) throws IOException {
        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(pdfDest))) {
            //Create new page
            PdfPage pdfPage = pdfDocument.addNewPage(PageSize.A4);

            //SVG image
            FileInputStream svgPath = new FileInputStream(svgSource);

            //Create SVG converter properties object
            ISvgConverterProperties props = new SvgConverterProperties();

            //Draw image on the page
            SvgConverter.drawOnPage(svgPath, pdfPage, props);
        }
    }
}


