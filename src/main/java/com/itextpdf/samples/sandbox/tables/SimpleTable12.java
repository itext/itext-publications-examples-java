package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class SimpleTable12 {
    public static final String DEST = "./target/sandbox/tables/simple_table12.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SimpleTable12().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4.rotate());

        Table table = new Table(UnitValue.createPercentArray(8)).useAllAvailableWidth();

        // The Style instance will be passed by default always until the moment we desire to specify some custom border
        Style style = new Style();

        // Default border width value is 0.5, we set it on 1 for cells borders of the table which will
        // use the Style instance
        style.setBorder(new SolidBorder(1));

        table.addCell(createCell("Examination", 1, 2, style));
        table.addCell(createCell("Board", 1, 2, style));
        table.addCell(createCell("Month and Year of Passing", 1, 2, style));
        table.addCell(createCell("", 1, 1, new Style()
                .setBorder(Border.NO_BORDER)
                .setBorderTop(new SolidBorder(1))
                .setBorderBottom(new SolidBorder(1))
                .setBorderLeft(new SolidBorder(1))));
        table.addCell(createCell("Marks", 2, 1, new Style()
                .setBorder(Border.NO_BORDER)
                .setBorderTop(new SolidBorder(1))
                .setBorderBottom(new SolidBorder(1))
                .setBorderRight(new SolidBorder(1))));
        table.addCell(createCell("Percentage", 1, 2, style));
        table.addCell(createCell("Class / Grade", 1, 2, style));
        table.addCell(createCell("", 1, 1, new Style()));
        table.addCell(createCell("Obtained", 1, 1, style));
        table.addCell(createCell("Out of", 1, 1, style));
        table.addCell(createCell("12th / I.B. Diploma", 1, 1, style));
        table.addCell(createCell("", 1, 1, style));
        table.addCell(createCell("", 1, 1, style));
        table.addCell(createCell("Aggregate (all subjects)", 1, 1, style));
        table.addCell(createCell("", 1, 1, style));
        table.addCell(createCell("", 1, 1, style));
        table.addCell(createCell("", 1, 1, style));
        table.addCell(createCell("", 1, 1, style));

        doc.add(table);

        doc.close();
    }

    private static Cell createCell(String content, int colspan, int rowspan, Style style) {
        Paragraph paragraph = new Paragraph(content)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER);

        Cell cell = new Cell(rowspan, colspan).add(paragraph);
        cell.addStyle(style);

        return cell;
    }
}
