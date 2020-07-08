package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class ColoredBorder {
    public static final String DEST = "./target/sandbox/tables/colored_border.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ColoredBorder().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        Cell cell = new Cell().add(new Paragraph("Cell 1"));
        cell.setBorderTop(new SolidBorder(ColorConstants.RED, 1));
        cell.setBorderBottom(new SolidBorder(ColorConstants.BLUE, 1));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("Cell 2"));
        cell.setBorderLeft(new SolidBorder(ColorConstants.GREEN, 5));
        cell.setBorderTop(new SolidBorder(ColorConstants.YELLOW, 8));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("Cell 3"));
        cell.setBorderLeft(new SolidBorder(ColorConstants.RED, 1));
        cell.setBorderBottom(new SolidBorder(ColorConstants.BLUE, 1));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("Cell 4"));
        cell.setBorderLeft(new SolidBorder(ColorConstants.GREEN, 5));
        cell.setBorderTop(new SolidBorder(ColorConstants.YELLOW, 8));
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }
}
