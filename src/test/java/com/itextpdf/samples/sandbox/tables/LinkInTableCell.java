/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/35288194
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;


@Category(SampleTest.class)
public class LinkInTableCell extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/link_in_table_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new LinkInTableCell().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(1);
        // Part of the content is a link:
        Paragraph phrase = new Paragraph();
        phrase.add("The founders of iText are nominated for a ");
        Link chunk = new Link("European Business Award!",
                PdfAction.createURI("http://itextpdf.com/blog/european-business-award-kick-ceremony"));
        phrase.add(chunk);
        table.addCell(phrase);
        // The complete cell is a link:
        Cell cell = new Cell().add("Help us win a European Business Award!");
        cell.setNextRenderer(new LinkInCellRenderer(cell, "http://itextpdf.com/blog/help-us-win-european-business-award"));
        table.addCell(cell);
        doc.add(table);
        doc.close();
    }


    class LinkInCellRenderer extends CellRenderer {
        protected String url;

        public LinkInCellRenderer(Cell modelElement, String url) {
            super(modelElement);
            this.url = url;
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            PdfLinkAnnotation linkAnnotation = new PdfLinkAnnotation(getOccupiedAreaBBox());
            linkAnnotation.setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT);
            linkAnnotation.setAction(PdfAction.createURI(url));
            drawContext.getDocument().getLastPage().addAnnotation(linkAnnotation);
        }

    }
}
