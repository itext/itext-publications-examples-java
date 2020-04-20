package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class WatermarkedImages3 {
    public static final String DEST = "./target/sandbox/images/watermarked_images3.pdf";

    public static final String IMAGE1 = "./src/main/resources/img/bruno.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new WatermarkedImages3().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(1).setWidth(UnitValue.createPercentValue(80));
        for (int i = 0; i < 35; i++) {
            table.addCell(new Cell().add(new Paragraph("rahlrokks doesn't listen to what people tell him")));
        }

        Image img = getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE1)), "Bruno")
                .setAutoScale(true);
        table.addCell(new Cell().add(img));
        doc.add(table);

        doc.showTextAligned("Bruno knows best", 260, 400,
                TextAlignment.CENTER, 45f * (float) Math.PI / 180f);

        doc.close();
    }

    private static Image getWatermarkedImage(PdfDocument pdfDoc, Image img, String watermark) {
        float width = img.getImageScaledWidth();
        float height = img.getImageScaledHeight();
        float coordX = width / 2;
        float coordY = height / 2;
        float angle = (float) Math.PI * 30f / 180f;
        PdfFormXObject template = new PdfFormXObject(new Rectangle(width, height));
        new Canvas(template, pdfDoc)
                .add(img)
                .setFontColor(DeviceGray.WHITE)
                .showTextAligned(watermark, coordX, coordY, TextAlignment.CENTER, angle)
                .close();
        return new Image(template);
    }
}
