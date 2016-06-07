/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/28487918/how-to-make-it-start-from-the-same-point
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class IndentationOptions extends GenericTest {
    public static final String CONTENT = "test A, test B, coconut, coconut, watermelons, apple, oranges, many more " +
            "fruites, carshow, monstertrucks thing, everything is startting on the " +
            "same point in the line now";
    public static final String LABEL = "A list of stuff: ";
    public static final String DEST = "./target/test/resources/sandbox/objects/indentation_options.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new IndentationOptions().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        List list = new List().
                setListSymbol(LABEL).
                add(CONTENT);
        doc.add(list);

        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        Paragraph p = new Paragraph(LABEL + CONTENT).setFont(font);
        float indentation = font.getWidth(LABEL, 12);
        p
                .setMarginLeft(indentation)
                .setFirstLineIndent(-indentation);
        doc.add(p);

        Table table = new Table(new float[]{indentation + 4, 519 - indentation});
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(LABEL));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(CONTENT));
        doc.add(table);

        doc.close();
    }
}
