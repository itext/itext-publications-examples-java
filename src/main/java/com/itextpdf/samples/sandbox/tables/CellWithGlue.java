package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class CellWithGlue {
    public static final String DEST = "./target/sandbox/tables/cell_with_glue.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CellWithGlue().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // By default column width is calculated automatically for the best fit.
        // useAllAvailableWidth() method makes table use the whole page's width while placing the content.
        Table table = new Table(UnitValue.createPercentArray(2));
        table.setHorizontalAlignment(HorizontalAlignment.LEFT);
        table.setWidth(UnitValue.createPercentValue(60));
        table.setMarginBottom(20);

        Cell cell = new Cell().add(new Paragraph("Received Rs (in Words):"));

        // Set all the cell's borders except for the right one to have black color and width of 1 point
        cell.setBorder(new SolidBorder(1));
        cell.setBorderRight(Border.NO_BORDER);
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("Priceless"));
        cell.setTextAlignment(TextAlignment.RIGHT);
        cell.setBorder(new SolidBorder(1));
        cell.setBorderLeft(Border.NO_BORDER);
        table.addCell(cell);

        doc.add(table);

        table.setWidth(UnitValue.createPercentValue(50));

        doc.add(table);

        table = new Table(UnitValue.createPercentArray(1));
        table.setHorizontalAlignment(HorizontalAlignment.LEFT);
        table.setWidth(UnitValue.createPercentValue(50));

        Paragraph p = new Paragraph();
        p.add(new Text("Received Rs (In Words):"));
        p.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
        p.add(new Tab());
        p.add(new Text("Priceless"));
        table.addCell(new Cell().add(p));

        doc.add(table);

        doc.close();
    }
}
