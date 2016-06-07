/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to
 * http://stackoverflow.com/questions/29229629/how-to-add-a-printable-or-non-printable-bitmap-stamp-to-a-pdf
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfStampAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class AddStamp extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/annotations/add_stamp.pdf";
    public static final String IMG = "./src/test/resources/img/itext.png";
    public static final String SRC = "./src/test/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddStamp().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));

        ImageData img = ImageDataFactory.create(IMG);
        float w = img.getWidth();
        float h = img.getHeight();
        Rectangle location = new Rectangle(36, 770 - h, w, h);
        PdfStampAnnotation stamp = new PdfStampAnnotation(location)
                .setStampName(new PdfName("ITEXT"));
        PdfFormXObject xObj = new PdfFormXObject(new Rectangle(w, h));
        PdfCanvas canvas = new PdfCanvas(xObj, pdfDoc);
        canvas.addImage(img, 0, 0, false);
        stamp.setNormalAppearance(xObj.getPdfObject());
        stamp.setFlags(PdfAnnotation.PRINT);

        pdfDoc.getFirstPage().addAnnotation(stamp);
        pdfDoc.close();
    }
}
