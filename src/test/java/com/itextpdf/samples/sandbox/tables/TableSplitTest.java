/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;


@Category(SampleTest.class)
public class TableSplitTest extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/tables_split_test.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TableSplitTest().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(595, 842));
        doc.setMargins(55, 15, 35, 15);

        ILineDrawer line = new SolidLine(2);
        line.setColor(Color.LIGHT_GRAY);
        LineSeparator tableEndSeparator = new LineSeparator(line);

        tableEndSeparator.setMarginTop(10);

        String[] header = new String[]{"Header1", "Header2", "Header3",
                "Header4", "Header5"};
        String[] content = new String[]{"column 1", "column 2",
                "some Text in column 3", "Test data ", "column 5"};

        Table table = new Table(new float[]{3, 2, 4, 3, 2});
        table.setWidthPercent(98);

        for (String columnHeader : header) {
            Cell headerCell = new Cell().add(new Paragraph(columnHeader).setFont(
                    PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD))
                    .setFontSize(10));
            headerCell.setTextAlignment(TextAlignment.CENTER);
            headerCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            headerCell.setBorder(new SolidBorder(Color.LIGHT_GRAY, 1));
            headerCell.setPadding(8);
            table.addHeaderCell(headerCell);
        }
        for (int i = 0; i < 15; i++) {
            int j = 0;
            for (String text : content) {
                if (i == 13 && j == 3) {
                    text = "Test data \n Test data \n Test data";
                }
                j++;
                Cell cell = new Cell().add(new Paragraph(text).setFont(
                        PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(10));
                cell.setBorder(new SolidBorder(Color.LIGHT_GRAY, 1));
                cell.setPaddingLeft(5);
                cell.setPaddingTop(5);
                cell.setPaddingRight(5);
                cell.setPaddingBottom(5);
                table.addCell(cell);
            }
        }
        doc.add(table);
        doc.add(tableEndSeparator);
        for (int k = 0; k < 5; k++) {
            Paragraph info = new Paragraph("Some title").setFont(
                    PdfFontFactory.createFont(FontConstants.HELVETICA))
                    .setFontSize(10);
            info.setMarginTop(12f);
            doc.add(info);
            table = new Table(new float[]{3, 2, 4, 3, 2});
            table.setWidthPercent(98);
            table.setMarginTop(15);

            for (String columnHeader : header) {
                Cell headerCell = new Cell().add(new Paragraph(columnHeader).setFont(
                        PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD))
                        .setFontSize(10));
                headerCell.setTextAlignment(TextAlignment.CENTER);
                headerCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
                headerCell.setBorder(new SolidBorder(Color.LIGHT_GRAY, 1));
                headerCell.setPaddingLeft(8);
                headerCell.setPaddingTop(8);
                headerCell.setPaddingRight(8);
                headerCell.setPaddingBottom(8);
                table.addHeaderCell(headerCell);
            }
            for (String text : content) {
                Cell cell = new Cell().add(new Paragraph(text).setFont(
                        PdfFontFactory.createFont(FontConstants.HELVETICA))
                        .setFontSize(10));
                cell.setBorder(new SolidBorder(Color.LIGHT_GRAY, 1));
                cell.setPaddingLeft(5);
                cell.setPaddingTop(5);
                cell.setPaddingRight(5);
                cell.setPaddingBottom(5);
                table.addCell(cell);
            }
            doc.add(table);
            doc.add(tableEndSeparator);
        }

        doc.close();
    }
}