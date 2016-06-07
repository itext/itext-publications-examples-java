/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceCmyk;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;


@Category(SampleTest.class)
public class AddSpotColorImage extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/add_spot_color_image.pdf";
    public static final String SRC = "./src/test/resources/pdfs/image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddSpotColorImage().manipulatePdf(DEST);
    }

    public PdfArray getSeparationColorspace(PdfWriter writer, DeviceCmyk cmyk) {
        PdfArray array = new PdfArray(PdfName.Separation);
        array.add(new PdfName("mySpotColor"));
        array.add(PdfName.DeviceCMYK);
        PdfDictionary func = new PdfDictionary();
        func.put(PdfName.FunctionType, new PdfNumber(2));
        func.put(PdfName.Domain, new PdfArray(new float[]{0, 1}));
        func.put(PdfName.C0, new PdfArray(new float[]{0, 0, 0, 0}));
        // magic numbers are from itext5
        func.put(PdfName.C1, new PdfArray(cmyk.getColorValue()));
        func.put(PdfName.N, new PdfNumber(1));
        array.add(func);
        return array;
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));

        // suppose that this is our image data
        byte circleData[] = {(byte) 0x3c, (byte) 0x7e, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x7e,
                (byte) 0x3c};

        PdfArray colorspace = getSeparationColorspace(pdfDoc.getWriter(), new DeviceCmyk(0.8f, 0.3f, 0.3f, 0.1f));
        ImageData image = ImageDataFactory.create(8, 8, 1, 1, circleData, new int[]{0, 0});
        PdfImageXObject imageXObject = new PdfImageXObject(image);
        imageXObject.put(PdfName.ColorSpace, colorspace);
        imageXObject.makeIndirect(pdfDoc);
        // Now we add the image to the existing PDF document
        PdfPage pdfPage = pdfDoc.getFirstPage();
        pdfPage.setIgnorePageRotationForContent(true);
        PdfCanvas canvas = new PdfCanvas(pdfPage);
        canvas.addXObject(imageXObject, 100, 200, 100);

        pdfDoc.close();
    }
}
