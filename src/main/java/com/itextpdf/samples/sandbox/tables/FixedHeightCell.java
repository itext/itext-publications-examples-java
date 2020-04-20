package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class FixedHeightCell {
    public static final String DEST = "./target/sandbox/tables/fixed_height_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FixedHeightCell().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(5)).useAllAvailableWidth();

        for (int r = 'A'; r <= 'Z'; r++) {
            for (int c = 1; c <= 5; c++) {
                Cell cell = new Cell();
                cell.add(new Paragraph(String.valueOf((char) r) + c));

                if (r == 'D') {
                    cell.setHeight(60);
                }
                if (r == 'E') {
                    cell.setHeight(60);
                    if (c == 4) {
                        cell.setHeight(120);
                    }
                }
                if (r == 'F') {
                    cell.setMinHeight(60);

                    cell.setHeight(60);
                    if (c == 2) {
                        cell.add(new Paragraph("This cell has more content than the other cells"));
                    }
                }

                table.addCell(cell);
            }
        }

        doc.add(table);

        doc.close();
    }
}
