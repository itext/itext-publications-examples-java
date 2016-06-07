/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.images;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;


@Category(SampleTest.class)
public class LargeImage1 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/images/large_image1.pdf";
    public static final String SRC = "./src/test/resources/pdfs/large_image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new LargeImage1().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        Rectangle rect = srcDoc.getFirstPage().getPageSize();
        if (rect.getWidth() < 14400 && rect.getHeight() < 14400) {
            System.out.println("The size of the PDF document is within the accepted limits");
            System.exit(0);
        }
        PdfDictionary pageDict = srcDoc.getFirstPage().getPdfObject();
        PdfDictionary pageResources = pageDict.getAsDictionary(PdfName.Resources);
        PdfDictionary pageXObjects = pageResources.getAsDictionary(PdfName.XObject);
        PdfName imgRef = pageXObjects.keySet().iterator().next();
        PdfStream imgStream = pageXObjects.getAsStream(imgRef);

        //create instance for new document
        PdfDocument resultDoc = new PdfDocument(new PdfWriter(dest));

        PdfImageXObject imgObject = new PdfImageXObject(imgStream.copyTo(resultDoc));

        Image image = new Image(imgObject);
        image.scaleToFit(14400, 14400);
        image.setFixedPosition(0, 0);

        srcDoc.close();

        Document doc = new Document(resultDoc, new PageSize(image.getImageScaledWidth(), image.getImageScaledHeight()));
        doc.add(image);
        doc.close();
    }
}
