/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/27820159/itext-list-how-to-remove-symbol-from-list
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class RemoveListSymbol extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/remove_list_symbol.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RemoveListSymbol().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        List list = new List();
        list.setListSymbol("");
        list.add(new ListItem("Item 1"));
        list.add(new ListItem("Item 2"));
        list.add(new ListItem("Item 3"));

        Paragraph phrase = new Paragraph("A list without list symbol");
        doc.add(phrase);

        Table phraseTable = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        phraseTable.setMarginTop(5);
        phraseTable.addCell(new Cell().add(new Paragraph("List:")));
        phraseTable.addCell(list);

        doc.add(phraseTable);

        doc.close();
    }
}
