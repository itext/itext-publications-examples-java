/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;


@Category(SampleTest.class)
public class IndentTable extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/indent_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new IndentTable().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfCanvas cb = new PdfCanvas(pdfDoc.addNewPage());
        cb.moveTo(36, 842);
        cb.lineTo(36, 0);
        cb.stroke();
        Table table = new Table(8);
        table.setHorizontalAlignment(HorizontalAlignment.LEFT);
        table.setWidth(150);
        for (int aw = 0; aw < 16; aw++) {
            table.addCell(new Cell().add(new Paragraph("hi")));
        }
        table.setMarginLeft(25);
        doc.add(table);

        doc.close();
    }
}