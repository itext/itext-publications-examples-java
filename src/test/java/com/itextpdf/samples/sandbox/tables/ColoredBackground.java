/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie and Nishanthi Grashia in answer to the following question:
 * http://stackoverflow.com/questions/27871574/appears-space-between-cells-without-border-itextpdf
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class ColoredBackground extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/colored_background.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColoredBackground().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table;
        Cell cell;
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        table = new Table(16);
        for (int aw = 0; aw < 16; aw++) {
            cell = new Cell().add(new Paragraph("hi").setFont(font).setFontColor(Color.WHITE));
            cell.setBackgroundColor(Color.BLUE);
            cell.setBorder(Border.NO_BORDER);
            cell.setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell);
        }
        doc.add(table);

        doc.close();
    }
}
