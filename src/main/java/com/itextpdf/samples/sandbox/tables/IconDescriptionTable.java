package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class IconDescriptionTable {
    public static final String DEST = "./target/sandbox/tables/icon_description_table.pdf";
    public static final String IMG = "./src/main/resources/img/bulb.gif";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new IconDescriptionTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(new float[] {10, 90}));
        Image img = new Image(ImageDataFactory.create(IMG));

        // Width and height of image are set to autoscale
        table.addCell(img.setAutoScale(true));
        table.addCell("A light bulb icon");

        doc.add(table);

        doc.close();
    }
}
