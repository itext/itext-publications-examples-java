/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class CellMethod extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/cell_method.pdf";
    public static final String FONT = "./src/test/resources/font/FreeSans.ttf";

    private static PdfFont czechFont = null;
    private static PdfFont defaultFont = null;
    private static PdfFont greekFont = null;

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CellMethod().manipulatePdf(DEST);
    }

    public static Cell getNormalCell(String string, String language, float size) throws IOException {
        if (string != null && "".equals(string)) {
            return new Cell();
        }
        PdfFont f = getFontForThisLanguage(language);
        Cell cell = new Cell().add(new Paragraph(string).setFont(f));
        cell.setHorizontalAlignment(HorizontalAlignment.LEFT);
        if (size < 0) {
            size = -size;
            cell.setFontSize(size);
            cell.setFontColor(Color.RED);
        }
        return cell;
    }

    public static PdfFont getFontForThisLanguage(String language) throws IOException {
        if ("czech".equals(language)) {
            return czechFont;
        }
        if ("greek".equals(language)) {
            return greekFont;
        }
        return defaultFont;
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        setCompareRenders(true);

        czechFont = PdfFontFactory.createFont(FONT, "Cp1250", true);
        greekFont = PdfFontFactory.createFont(FONT, "Cp1253", true);
        defaultFont = PdfFontFactory.createFont(FONT, null, true);

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(2);
        table.addCell("Winansi");
        table.addCell(getNormalCell("Test", null, 12));
        table.addCell("Winansi");
        table.addCell(getNormalCell("Test", null, -12));
        table.addCell("Greek");
        table.addCell(getNormalCell("\u039d\u03cd\u03c6\u03b5\u03c2", "greek", 12));
        table.addCell("Czech");
        table.addCell(getNormalCell("\u010c,\u0106,\u0160,\u017d,\u0110", "czech", 12));
        table.addCell("Test");
        table.addCell(getNormalCell(" ", null, 12));
        table.addCell("Test");
        table.addCell(getNormalCell(" ", "greek", 12));
        table.addCell("Test");
        table.addCell(getNormalCell(" ", "czech", 12));
        doc.add(table);

        doc.close();
    }
}
