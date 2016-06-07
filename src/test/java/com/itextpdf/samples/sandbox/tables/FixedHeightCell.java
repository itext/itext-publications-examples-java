/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;


@Category(SampleTest.class)
public class FixedHeightCell extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/fixed_height_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FixedHeightCell().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(5);
        Cell cell;
        for (int r = 'A'; r <= 'Z'; r++) {
            for (int c = 1; c <= 5; c++) {
                cell = new Cell();
                cell.add(new Paragraph(String.valueOf((char) r) + String.valueOf(c)));
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
                    // TODO DEVSIX-555
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
