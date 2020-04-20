package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class RowspanAbsolutePosition {
    public static final String DEST = "./target/sandbox/tables/rowspan_absolute_position.pdf";

    public static final String IMG = "./src/main/resources/img/berlin2013.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RowspanAbsolutePosition().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table1 = new Table(new float[] {150, 200, 200});

        Cell cell = new Cell(1, 2).add(new Paragraph("{Month}"));
        cell.setHorizontalAlignment(HorizontalAlignment.LEFT);

        Image img = new Image(ImageDataFactory.create(IMG));
        img.setWidth(UnitValue.createPercentValue(100));
        img.setAutoScale(true);

        Cell cell2 = new Cell(2, 1).add(img);
        Cell cell3 = new Cell(1, 2).add(new Paragraph("Mr Fname Lname"));
        cell3.setHorizontalAlignment(HorizontalAlignment.LEFT);

        table1.addCell(cell);
        table1.addCell(cell2);
        table1.addCell(cell3);

        doc.add(table1);

        doc.close();
    }
}
