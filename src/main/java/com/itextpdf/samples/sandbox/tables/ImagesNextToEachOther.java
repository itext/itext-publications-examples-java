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
import java.net.MalformedURLException;

public class ImagesNextToEachOther {
    public static final String DEST = "./target/sandbox/tables/image_next_to_each_other.pdf";

    public static final String IMG1 = "./src/main/resources/img/javaone2013.jpg";
    public static final String IMG2 = "./src/main/resources/img/berlin2013.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ImagesNextToEachOther().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.addCell(createImageCell(IMG1));
        table.addCell(createImageCell(IMG2));

        doc.add(table);

        doc.close();
    }

    private static Cell createImageCell(String path) throws MalformedURLException {
        Image img = new Image(ImageDataFactory.create(path));
        return new Cell().add(img.setAutoScale(true).setWidth(UnitValue.createPercentValue(100)));
    }
}
