/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/29561417/draw-lines-on-image-in-pdf-using-itextsharp
 */
package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.TableRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class WatermarkedImages5 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/images/watermarked_images5.pdf";
    public static final String IMAGE1 = "./src/test/resources/img/bruno.jpg";
    public static final String IMAGE2 = "./src/test/resources/img/dog.bmp";
    public static final String IMAGE3 = "./src/test/resources/img/fox.bmp";
    public static final String IMAGE4 = "./src/test/resources/img/bruno_ingeborg.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new WatermarkedImages5().manipulatePdf(DEST);
    }

    public Image getWatermarkedImage(PdfDocument pdfDocument, Image img) {
        float width = img.getImageScaledWidth();
        float height = img.getImageScaledHeight();
        PdfFormXObject template = new PdfFormXObject(new Rectangle(width, height));

        Table table = new Table(2);
        table.setWidth(width);

        table.addCell(new Cell().add("Test1").setBorder(new SolidBorder(Color.YELLOW, 1)));
        table.addCell(new Cell().add("Test2").setBorder(new SolidBorder(Color.YELLOW, 1)));
        table.addCell(new Cell().add("Test3").setBorder(new SolidBorder(Color.YELLOW, 1)));
        table.addCell(new Cell().add("Test4").setBorder(new SolidBorder(Color.YELLOW, 1)));

        // find the height of the table
        TableRenderer renderer = (TableRenderer)table.createRendererSubTree();
        renderer.setParent(new DocumentRenderer(new Document(pdfDocument)));
        LayoutResult result = renderer.layout(new LayoutContext(new LayoutArea(1, new Rectangle(10000, 10000))));

        Canvas canvas = new Canvas(template, pdfDocument);
        canvas.add(img);
        canvas = new Canvas(template, pdfDocument);
        canvas.add(table.setFixedPosition(0, height-result.getOccupiedArea().getBBox().getHeight(), width));

        return new Image(template);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE1))));
        doc.add(getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE2))));
        doc.add(getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE3))));

        Image image = new Image(ImageDataFactory.create(IMAGE4));
        image.scaleToFit(400, 700);
        doc.add(getWatermarkedImage(pdfDoc, image));

        doc.close();
    }
}