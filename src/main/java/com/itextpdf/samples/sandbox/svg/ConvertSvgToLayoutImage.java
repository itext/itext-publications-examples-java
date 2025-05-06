package com.itextpdf.samples.sandbox.svg;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.svg.converter.SvgConverter;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

public class ConvertSvgToLayoutImage {
    private static final String SRC = "./src/main/resources/svg/";
    public static final String DEST = "./target/sandbox/svg/ConvertSvgToLayoutImage.pdf";

    public static void main(String[] args) throws IOException {
        String svgImage = SRC + "cauldron.svg";
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ConvertSvgToLayoutImage().manipulatePdf(svgImage, DEST);
    }

    public void manipulatePdf(String svgSource, String pdfDest) throws IOException {
        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(pdfDest))) {
            Document doc = new Document(pdfDocument);

            //Create new page
            pdfDocument.addNewPage(PageSize.A4);

            doc.add(new Paragraph("This is some text added before the SVG image."));

            //SVG image
            FileInputStream svgPath = new FileInputStream(svgSource);

            //Convert to image
            Image image = SvgConverter.convertToImage(svgPath, pdfDocument);

            //Set scale
            image.scaleToFit(PageSize.A4.getWidth()/2, PageSize.A4.getHeight()/2);

            //Add to the document
            doc.add(image);

            doc.add(new Paragraph("This is some text added after the SVG image."));
        }
    }
}


