/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/27844914/align-justified-for-itext-list-item
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class ListAlignment extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/list_alignment.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ListAlignment().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        String text = "test 1 2 3 ";
        for (int i = 0; i < 5; i++) {
            text = text + text;
        }
        List list = new List();
        ListItem item = new ListItem(text);
        item.setTextAlignment(TextAlignment.JUSTIFIED);
        list.add(item);

        text = "a b c align ";
        for (int i = 0; i < 5; i++) {
            text = text + text;
        }
        item = new ListItem(text);
        item.setTextAlignment(TextAlignment.JUSTIFIED);
        list.add(item);

        text = "supercalifragilisticexpialidociousss ";
        for (int i = 0; i < 3; i++) {
            text = text + text;
        }
        item = new ListItem(text);
        item.setTextAlignment(TextAlignment.JUSTIFIED);
        list.add(item);

        doc.add(list);

        doc.close();
    }
}
