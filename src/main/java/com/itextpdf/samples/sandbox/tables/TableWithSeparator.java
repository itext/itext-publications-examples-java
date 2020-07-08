package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class TableWithSeparator {
    public static final String DEST = "./target/sandbox/tables/table_with_separator.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TableWithSeparator().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        table.addCell(getCell1());
        table.addCell(getCell2());
        table.addCell(getCell3());
        table.addCell(getCell4());

        doc.add(table);

        doc.close();
    }

    private static Cell getCell1() {
        Cell cell = new Cell();

        Paragraph p1 = new Paragraph("My fantastic data");
        p1.setLineThrough();
        cell.add(p1);

        Paragraph p2 = new Paragraph("Other data");
        cell.add(p2);

        return cell;
    }

    private static Cell getCell2() {
        Cell cell = new Cell();

        Paragraph p1 = new Paragraph("My fantastic data");
        cell.add(p1);

        LineSeparator ls = new LineSeparator(new SolidLine());
        cell.add(ls);

        Paragraph p2 = new Paragraph("Other data");
        cell.add(p2);

        return cell;
    }

    private static Cell getCell3() {
        Cell cell = new Cell();

        Paragraph p1 = new Paragraph("My fantastic data");
        cell.add(p1);

        LineSeparator ls = new LineSeparator(new SolidLine());
        cell.add(ls);

        Paragraph p2 = new Paragraph("Other data");
        p2.setFixedLeading(25);
        cell.add(p2);

        return cell;
    }

    private static Cell getCell4() {
        Cell cell = new Cell();

        Paragraph p1 = new Paragraph("My fantastic data");
        p1.setMarginBottom(20);
        cell.add(p1);

        LineSeparator ls = new LineSeparator(new SolidLine());
        cell.add(ls);

        Paragraph p2 = new Paragraph("Other data");
        p2.setMarginTop(10);
        cell.add(p2);

        return cell;
    }
}
