package com.itextpdf.samples.sandbox.svg;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.svg.converter.SvgConverter;
import com.itextpdf.svg.processors.impl.SvgConverterProperties;

import java.io.File;
import java.io.IOException;

public class ConvertSvgStringToPdf {
    public static final String DEST = "./target/sandbox/svg/SvgStringToPdf.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ConvertSvgStringToPdf().manipulatePdf(DEST);
    }

    public void manipulatePdf(String pdfDest) throws IOException {
        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(pdfDest))) {
            //Create new page
            pdfDocument.addNewPage(PageSize.A4);

            //SVG String
            String contents = "<svg viewBox=\"0 0 240 240\" xmlns=\"http://www.w3.org/2000/svg\">\n"
                    + "  <style>\n"
                    + "    circle {\n"
                    + "      fill: green;\n"
                    + "    }\n"
                    + "  </style>\n"
                    + "\n"
                    + "  <circle cx=\"100\" cy=\"100\" r=\"50\"/>\n"
                    + "</svg>";

            //Convert and draw the SVG as string directly to the PDF.
            SvgConverter.drawOnDocument(contents, pdfDocument, 1, new SvgConverterProperties());
        }
    }
}