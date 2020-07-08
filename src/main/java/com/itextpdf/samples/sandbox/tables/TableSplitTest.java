package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;

public class TableSplitTest {
    public static final String DEST = "./target/sandbox/tables/tables_split_test.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TableSplitTest().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(595, 842));

        doc.setMargins(55, 15, 35, 15);

        ILineDrawer line = new SolidLine(2);
        line.setColor(ColorConstants.LIGHT_GRAY);

        LineSeparator tableEndSeparator = new LineSeparator(line);
        tableEndSeparator.setMarginTop(10);

        String[] header = new String[]{"Header1", "Header2", "Header3",
                "Header4", "Header5"};
        String[] content = new String[]{"column 1", "column 2",
                "some Text in column 3", "Test data ", "column 5"};

        Table table = new Table(UnitValue.createPercentArray(new float[] {3, 2, 4, 3, 2})).useAllAvailableWidth();

        for (String columnHeader : header) {
            Paragraph headerParagraph = new Paragraph(columnHeader)
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                    .setFontSize(10);

            Cell headerCell = new Cell()
                    .add(headerParagraph)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                    .setPadding(8);

            table.addHeaderCell(headerCell);
        }

        for (int i = 0; i < 15; i++) {
            int j = 0;

            for (String text : content) {
                Paragraph paragraph = new Paragraph((i == 13 && j == 3) ? "Test data \n Test data \n Test data" : text)
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                        .setFontSize(10);

                Cell cell = new Cell()
                        .add(paragraph)
                        .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                        .setPaddingLeft(5)
                        .setPaddingTop(5)
                        .setPaddingRight(5)
                        .setPaddingBottom(5);

                table.addCell(cell);

                j++;
            }
        }

        doc.add(table);
        doc.add(tableEndSeparator);

        for (int k = 0; k < 5; k++) {
            Paragraph info = new Paragraph("Some title")
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                    .setFontSize(10)
                    .setMarginTop(12);

            doc.add(info);

            table = new Table(UnitValue.createPercentArray(new float[] {3, 2, 4, 3, 2})).useAllAvailableWidth();
            table.setMarginTop(15);

            for (String columnHeader : header) {
                Paragraph paragraph = new Paragraph(columnHeader)
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontSize(10);

                Cell headerCell = new Cell()
                        .add(paragraph)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                        .setPaddingLeft(8)
                        .setPaddingTop(8)
                        .setPaddingRight(8)
                        .setPaddingBottom(8);

                table.addHeaderCell(headerCell);
            }

            for (String text : content) {
                Paragraph paragraph = new Paragraph(text)
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                        .setFontSize(10);

                Cell cell = new Cell()
                        .add(paragraph)
                        .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                        .setPaddingLeft(5)
                        .setPaddingTop(5)
                        .setPaddingRight(5)
                        .setPaddingBottom(5);

                table.addCell(cell);
            }

            doc.add(table);
            doc.add(tableEndSeparator);
        }

        doc.close();
    }
}
