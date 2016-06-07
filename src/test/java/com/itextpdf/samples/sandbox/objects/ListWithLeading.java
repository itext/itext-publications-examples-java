/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/29566115/adding-veritcal-spacing-itextsharp-in-list
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class ListWithLeading extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/list_with_leading.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ListWithLeading().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        List list1 = new List().
                setSymbolIndent(12).
                setListSymbol("\u2022").
                setFont(font);
        list1.add(new ListItem("Value 1")).
                add(new ListItem("Value 2")).
                add(new ListItem("Value 3"));
        doc.add(list1);

        List list2 = new List().
                setSymbolIndent(12).
                setListSymbol("\u2022");
        list2.add((ListItem) new ListItem().add(new Paragraph("Value 1").setFixedLeading(30).setMargins(0, 0, 0, 0))).
                add((ListItem) new ListItem().add(new Paragraph("Value 2").setFixedLeading(30).setMargins(0, 0, 0, 0))).
                add((ListItem) new ListItem().add(new Paragraph("Value 3").setFixedLeading(30).setMargins(0, 0, 0, 0)));
        list2.setMarginLeft(60);
        doc.add(list2);

        doc.close();
    }
}
