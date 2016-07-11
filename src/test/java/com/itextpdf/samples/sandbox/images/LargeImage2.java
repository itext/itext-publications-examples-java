/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.images;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class LargeImage2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/images/large_image2.pdf";
    public static final String SRC = "./src/test/resources/pdfs/large_image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new LargeImage2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfReader reader = new PdfReader(SRC);
        reader.setCloseStream(false);

        File tmp = File.createTempFile("large_image", ".pdf", new File("."));
        tmp.deleteOnExit();

        PdfDocument tempDoc = new PdfDocument(reader, new PdfWriter(tmp.getAbsolutePath()));
        Rectangle rect = tempDoc.getFirstPage().getPageSize();

        if (rect.getWidth() < 14400 && rect.getHeight() < 14400) {
            System.out.println("The size of the PDF document is within the accepted limits");
            System.exit(0);
        }

        PdfDictionary pageDict = tempDoc.getFirstPage().getPdfObject();
        PdfDictionary pageResources = pageDict.getAsDictionary(PdfName.Resources);
        PdfDictionary pageXObjects = pageResources.getAsDictionary(PdfName.XObject);
        PdfName imgRef = pageXObjects.keySet().iterator().next();
        PdfStream imgStream = pageXObjects.getAsStream(imgRef);
        PdfImageXObject imgObject = new PdfImageXObject(imgStream);

        Image img = new Image(imgObject);
        img.scaleToFit(14400, 14400);
        img.setFixedPosition(0, 0);
        tempDoc.addNewPage(1, new PageSize(img.getImageScaledWidth(), img.getImageScaledHeight()));
        new Document(tempDoc)
                .add(img)
                .close();

        // Open temporary document only for reading
        tempDoc = new PdfDocument(new PdfReader(tmp.getAbsolutePath()));

        PdfDocument resultDoc = new PdfDocument(new PdfWriter(DEST));
        tempDoc.copyPagesTo(1, 1, resultDoc);

        resultDoc.close();
        tempDoc.close();
    }
}
