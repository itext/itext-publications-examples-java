package com.itextpdf.samples.sandbox.svg;

import com.itextpdf.svg.converter.SvgConverter;

import java.io.File;
import java.io.IOException;

public class ConvertSvgToPdf {
    private static final String SRC = "./src/main/resources/svg/";
    public static final String DEST = "./target/sandbox/svg/ConvertSvgToPdf.pdf";

    public static void main(String[] args) throws IOException {
        String svgImage = SRC + "cauldron.svg";
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ConvertSvgToPdf().manipulatePdf(svgImage, DEST);
    }

    public void manipulatePdf(String svgSource, String pdfDest) throws IOException {
        File svgFile = new File(svgSource);
        File destinationPdf = new File(pdfDest);

        //Directly convert the SVG file to a PDF.
        SvgConverter.createPdf(svgFile, destinationPdf);
    }
}


