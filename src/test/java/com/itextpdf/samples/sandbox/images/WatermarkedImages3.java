/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/28515474/how-to-add-text-on-the-last-page-through-pdfcontentbyte
 */
package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class WatermarkedImages3 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/images/watermarked_images3.pdf";
    public static final String IMAGE1 = "./src/test/resources/img/bruno.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new WatermarkedImages3().manipulatePdf(DEST);
    }

    public Image getWatermarkedImage(PdfDocument pdfDoc, Image img, String watermark) {
        float width = img.getImageScaledWidth();
        float height = img.getImageScaledHeight();
        PdfFormXObject template = new PdfFormXObject(new Rectangle(width, height));
        new Canvas(template, pdfDoc).
                add(img).
                setFontColor(DeviceGray.WHITE).
                showTextAligned(watermark, width / 2, height / 2, TextAlignment.CENTER, (float) Math.PI * 30f / 180f);
        return new Image(template);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(1).setWidthPercent(80);
        for (int i = 0; i < 35; i++) {
            table.addCell(new Cell().add("rahlrokks doesn't listen to what people tell him"));
        }
        table.addCell(new Cell().add(getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE1)), "Bruno").setAutoScale(true)));
        doc.add(table);
        doc.showTextAligned("Bruno knows best", 260, 400, TextAlignment.CENTER, 45f * (float) Math.PI / 180f);
        doc.close();
    }
}
