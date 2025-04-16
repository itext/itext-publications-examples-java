package com.itextpdf.samples.sandbox.barcodes;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;

/*
 * BarcodeBackground.java
 * 
 * This class demonstrates how to create a barcode with a custom background color in a PDF document.
 * The code generates a Code 128 barcode representing the string "12345XX789XXX", converts it to 
 * a PDF form XObject, then places it on a page with a light gray rectangular background.
 * This example shows how to enhance barcode presentation by adding background elements
 * to make the barcode stand out on the page.
 */

public class BarcodeBackground {
    public static final String DEST = "./target/sandbox/barcodes/barcode_background.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new BarcodeBackground().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        Barcode128 code128 = new Barcode128(pdfDoc);
        code128.setCode("12345XX789XXX");
        code128.setCodeType(Barcode128.CODE128);
        PdfFormXObject xObject = code128.createFormXObject(ColorConstants.BLACK, ColorConstants.BLACK, pdfDoc);

        float x = 36;
        float y = 750;
        float width = xObject.getWidth();
        float height = xObject.getHeight();

        // Draw the rectangle with the set background color and add the created barcode object.
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        canvas.saveState();
        canvas.setFillColor(ColorConstants.LIGHT_GRAY);
        canvas.rectangle(x, y, width, height);
        canvas.fill();
        canvas.restoreState();
        canvas.addXObjectAt(xObject, 36, 750);

        pdfDoc.close();
    }
}
