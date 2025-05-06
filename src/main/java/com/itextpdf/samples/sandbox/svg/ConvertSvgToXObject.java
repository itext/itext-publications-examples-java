package com.itextpdf.samples.sandbox.svg;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfXObject;
import com.itextpdf.svg.converter.SvgConverter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConvertSvgToXObject {
    private static final String SRC = "./src/main/resources/svg/";
    public static final String DEST = "./target/sandbox/svg/SvgToXObject.pdf";

    public static void main(String[] args) throws IOException {
        String svgImage = SRC + "cauldron.svg";
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ConvertSvgToXObject().manipulatePdf(svgImage, DEST);
    }

    public void manipulatePdf(String svgSource, String pdfDest) throws IOException {
        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(pdfDest))) {
            //Create new page
            PdfPage pdfPage = pdfDocument.addNewPage(PageSize.A4);

            //SVG image
            InputStream in = Files.newInputStream(Paths.get(svgSource));

            //Convert directly to an XObject
            PdfXObject xObj = SvgConverter.convertToXObject(in, pdfDocument);

            //Get the PdfCanvas of the page
            PdfCanvas canvas = new PdfCanvas(pdfPage);

            //Add the XObject to the page
            canvas.addXObjectFittedIntoRectangle(xObj, new Rectangle(100, 180, PageSize.A4.getWidth()/2, PageSize.A4.getHeight()/2));
        }
    }
}


