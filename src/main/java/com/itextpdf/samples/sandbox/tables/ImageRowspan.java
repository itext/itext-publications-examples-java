package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class ImageRowspan {
    public static final String DEST = "./target/sandbox/tables/image_rowspan.pdf";

    public static final String IMG = "./src/main/resources/img/bruno.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ImageRowspan().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.addCell(new Cell(2, 1).add(new Image(ImageDataFactory.create(IMG))
                .setWidth(UnitValue.createPercentValue(100))));
        table.addCell("1");
        table.addCell("2");

        doc.add(table);

        doc.close();
    }
}
