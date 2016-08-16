/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example is written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/32593183/itextsharp-is-it-possible-to-set-a-different-alignment-in-the-same-cell-for-te
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;


@Category(SampleTest.class)
public class TableWithTab extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/table_with_tab.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TableWithTab().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(1);
        Paragraph p = new Paragraph();
        p.add("Left");
        p.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
        p.add(new Tab());
        p.add("Right");
        table.addCell(p);
        doc.add(table);
        doc.close();
    }
}
