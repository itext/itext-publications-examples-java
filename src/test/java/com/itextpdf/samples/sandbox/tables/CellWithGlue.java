/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;


@Category(SampleTest.class)
public class CellWithGlue extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/cell_with_glue.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CellWithGlue().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table;
        Cell cell;

        table = new Table(2);
        table.setHorizontalAlignment(HorizontalAlignment.LEFT);
        table.setWidthPercent(60);
        table.setMarginBottom(20);
        cell = new Cell().add(new Paragraph("Received Rs (in Words):"));
        cell.setBorder(Border.NO_BORDER);
        cell.setBorderLeft(new SolidBorder(1));
        cell.setBorderTop(new SolidBorder(1));
        cell.setBorderBottom(new SolidBorder(1));
        table.addCell(cell);
        cell = new Cell().add(new Paragraph("Priceless"));
        cell.setTextAlignment(TextAlignment.RIGHT);
        cell.setBorder(Border.NO_BORDER);
        cell.setBorderRight(new SolidBorder(1));
        cell.setBorderTop(new SolidBorder(1));
        cell.setBorderBottom(new SolidBorder(1));
        table.addCell(cell);
        doc.add(table);

        table.setWidthPercent(50);
        doc.add(table);

        table = new Table(1);
        table.setHorizontalAlignment(HorizontalAlignment.LEFT);
        table.setWidthPercent(50);
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
