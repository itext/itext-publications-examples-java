/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.columntext;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class DropTablePart extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/columntext/drop_table_part.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new DropTablePart().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        for (int i = 0; i < 4; ) {
            Table table = new Table(UnitValue.createPercentArray(new float[]{25, 25, 25, 25}));

            Cell cell = new Cell(1, 4).add(new Paragraph("inner table " + (++i)));
            table.addCell(cell);
            for (int j = 0; j < 18; j++) {
                table.addCell("test Data " + (j + 1) + ".1");
                table.addCell("test Data " + (j + 1) + ".1");
                table.addCell("test Data " + (j + 1) + ".1");
                table.addCell("test Data " + (j + 1) + ".1");
            }

            doc.add(table);
        }

        doc.close();
    }
}
