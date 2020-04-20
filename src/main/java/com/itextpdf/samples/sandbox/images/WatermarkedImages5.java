package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;

public class WatermarkedImages5 {
    public static final String DEST = "./target/sandbox/images/watermarked_images5.pdf";

    public static final String IMAGE1 = "./src/main/resources/img/bruno.jpg";
    public static final String IMAGE2 = "./src/main/resources/img/dog.bmp";
    public static final String IMAGE3 = "./src/main/resources/img/fox.bmp";
    public static final String IMAGE4 = "./src/main/resources/img/bruno_ingeborg.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new WatermarkedImages5().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Image img = getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE1)));
        doc.add(img);

        img = getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE2)));
        doc.add(img);

        img = getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE3)));
        doc.add(img);

        Image srcImage = new Image(ImageDataFactory.create(IMAGE4));
        srcImage.scaleToFit(400, 700);
        img = getWatermarkedImage(pdfDoc, srcImage);
        doc.add(img);

        doc.close();
    }

    private static Image getWatermarkedImage(PdfDocument pdfDocument, Image img) {
        float width = img.getImageScaledWidth();
        float height = img.getImageScaledHeight();

        Table table = initTable(width);

        TableRenderer renderer = (TableRenderer) table.createRendererSubTree();
        renderer.setParent(new DocumentRenderer(new Document(pdfDocument)));

        // Simulate the positioning of the renderer to find out how much space the table will occupy.
        LayoutResult result = renderer.layout(new LayoutContext(new LayoutArea(
                1, new Rectangle(10000, 10000))));

        PdfFormXObject template = new PdfFormXObject(new Rectangle(width, height));
        new Canvas(template, pdfDocument)
                .add(img)
                .close();

        float left = 0;
        float bottom = height - result.getOccupiedArea().getBBox().getHeight();
        new Canvas(template, pdfDocument)
                .add(table.setFixedPosition(left, bottom, width))
                .close();

        return new Image(template);
    }

    private static Table initTable(float width) {
        Table table = new Table(UnitValue.createPercentArray(2)).setWidth(width);
        table.addCell(new Cell().add(new Paragraph("Test1")).setBorder(new SolidBorder(ColorConstants.YELLOW, 1)));
        table.addCell(new Cell().add(new Paragraph("Test2")).setBorder(new SolidBorder(ColorConstants.YELLOW, 1)));
        table.addCell(new Cell().add(new Paragraph("Test3")).setBorder(new SolidBorder(ColorConstants.YELLOW, 1)));
        table.addCell(new Cell().add(new Paragraph("Test4")).setBorder(new SolidBorder(ColorConstants.YELLOW, 1)));
        return table;
    }
}
