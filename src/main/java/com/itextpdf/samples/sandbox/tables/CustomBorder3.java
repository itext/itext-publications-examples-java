package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class CustomBorder3 {
    public static final String DEST = "./target/sandbox/tables/custom_border3.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CustomBorder3().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDoc);

        // By default column width is calculated automatically for the best fit.
        // useAllAvailableWidth() method makes table use the whole page's width while placing the content.
        Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        table.setMarginBottom(30);

        Cell cell = new Cell().add(new Paragraph("dotted left border"));

        // By default all the cell's borders are black and have 0.5pt width.
        // To write only the left border one should set the value of the others to null.
        cell.setBorder(Border.NO_BORDER);
        cell.setBorderLeft(new DottedBorder(0.5f));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("solid right border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setBorderRight(new SolidBorder(0.5f));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("dashed top border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setBorderTop(new DashedBorder(1f));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("bottom border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setBorderBottom(new SolidBorder(1f));
        table.addCell(cell);

        document.add(table);

        table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        table.setMarginBottom(30);

        cell = new Cell().add(new Paragraph("dotted left and solid top border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setBorderTop(new SolidBorder(1f));
        cell.setBorderLeft(new DottedBorder(0.5f));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("dashed right and dashed bottom border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setBorderBottom(new DashedBorder(1f));
        cell.setBorderRight(new DashedBorder(0.5f));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("no border"));
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("full solid border"));
        table.addCell(cell);

        document.add(table);

        document.close();
    }
}
