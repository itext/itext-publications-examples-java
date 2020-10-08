package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfSpecialCs;
import com.itextpdf.kernel.pdf.function.PdfFunction;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.kernel.pdf.xobject.PdfXObject;

import java.io.File;

public class AddSpotColorImage {
    public static final String DEST = "./target/sandbox/stamper/add_spot_color_image.pdf";
    public static final String SRC = "./src/main/resources/pdfs/image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddSpotColorImage().manipulatePdf(DEST);
    }

    public PdfSpecialCs getSeparationColorspace(PdfWriter writer, DeviceCmyk cmyk) {
        PdfDictionary pdfDictionary = new PdfDictionary();
        pdfDictionary.put(PdfName.FunctionType, new PdfNumber(2));
        pdfDictionary.put(PdfName.Domain, new PdfArray(new float[] {0, 1}));
        pdfDictionary.put(PdfName.C0, new PdfArray(new float[] {0, 0, 0, 0}));
        pdfDictionary.put(PdfName.C1, new PdfArray(cmyk.getColorValue()));
        pdfDictionary.put(PdfName.N, new PdfNumber(1));

        PdfFunction pdfFunction = new PdfFunction.Type2(pdfDictionary);

        return new PdfSpecialCs.Separation("mySpotColor", cmyk.getColorSpace(), pdfFunction);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        // Suppose that this is our image data
        byte circleData[] = {(byte) 0x3c, (byte) 0x7e, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0x7e, (byte) 0x3c};

        PdfSpecialCs colorspace = getSeparationColorspace(pdfDoc.getWriter(),
                new DeviceCmyk(0.8f, 0.3f, 0.3f, 0.1f));

        // Specifying a single component colorspace in the image
        ImageData image = ImageDataFactory.create(8, 8, 1, 1,
                circleData, new int[] {0, 0});
        PdfImageXObject imageXObject = new PdfImageXObject(image);
        imageXObject.put(PdfName.ColorSpace, colorspace.getPdfObject());
        imageXObject.makeIndirect(pdfDoc);

        // Now we add the image to the existing PDF document
        PdfPage pdfPage = pdfDoc.getFirstPage();
        pdfPage.setIgnorePageRotationForContent(true);
        PdfCanvas canvas = new PdfCanvas(pdfPage);
        Rectangle rect = PdfXObject.calculateProportionallyFitRectangleWithWidth(imageXObject, 100, 200, 100);
        canvas.addXObjectFittedIntoRectangle(imageXObject, rect);

        pdfDoc.close();
    }
}
