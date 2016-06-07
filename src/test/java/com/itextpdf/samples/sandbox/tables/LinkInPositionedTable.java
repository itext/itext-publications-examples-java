/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written in answer to:
 * http://stackoverflow.com/questions/33633363/itextpdf-cannot-use-writeselectedrows-on-a-table-where-an-anchor-has-been-in
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class LinkInPositionedTable extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/link_in_positioned_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new LinkInPositionedTable().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(1);
        table.setWidth(500);
        Cell cell = new Cell();
        Paragraph p = new Paragraph();
        Link link = new Link("link to top of next page", PdfAction.createGoTo("top"));
        p.add(link);
        cell.add(p);
        table.addCell(cell);
        doc.add(table);
        doc.add(new AreaBreak());

        Paragraph target = new Paragraph("top");
        target.setProperty(Property.DESTINATION, "top");
        doc.add(target);

        doc.close();
    }
}
