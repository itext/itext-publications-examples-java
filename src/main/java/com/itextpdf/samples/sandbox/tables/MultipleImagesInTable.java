package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class MultipleImagesInTable {
    public static final String DEST
            = "./target/sandbox/tables/multiple_images_in_table.pdf";

    public static final String IMG1
            = "./src/main/resources/img/brasil.png";

    public static final String IMG2
            = "./src/main/resources/img/dog.bmp";

    public static final String IMG3
            = "./src/main/resources/img/fox.bmp";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new MultipleImagesInTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Image img1 = new Image(ImageDataFactory.create(IMG1));
        Image img2 = new Image(ImageDataFactory.create(IMG2));
        Image img3 = new Image(ImageDataFactory.create(IMG3));

        Table table = new Table(UnitValue.createPercentArray(1));
        table.setWidth(UnitValue.createPercentValue(20));

        img1.setAutoScale(true);
        img2.setAutoScale(true);
        img3.setAutoScale(true);

        table.addCell(img1);
        table.addCell("Brazil");
        table.addCell(img2);
        table.addCell("Dog");
        table.addCell(img3);
        table.addCell("Fox");

        doc.add(table);

        doc.close();
    }
}
