/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/35454039
 */
package com.itextpdf.samples.sandbox.barcodes;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class BarcodeBackground extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/barcodes/barcode_background.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new BarcodeBackground().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        Barcode128 code128 = new Barcode128(pdfDoc);
        code128.setCode("12345XX789XXX");
        code128.setCodeType(Barcode128.CODE128);
        PdfFormXObject xObject = code128.createFormXObject(Color.BLACK, Color.BLACK, pdfDoc);
        float x = 36;
        float y = 750;
        float w = xObject.getWidth();
        float h = xObject.getHeight();
        canvas.saveState();
        canvas.setFillColor(Color.LIGHT_GRAY);
        canvas.rectangle(x, y, w, h);
        canvas.fill();
        canvas.restoreState();
        canvas.addXObject(xObject, 36, 750);
        pdfDoc.close();
    }
}
