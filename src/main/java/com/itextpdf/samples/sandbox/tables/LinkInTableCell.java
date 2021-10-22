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
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class LinkInTableCell {
    public static final String DEST = "./target/sandbox/tables/link_in_table_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new LinkInTableCell().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();

        // Part of the content is a link:
        Paragraph paragraph = new Paragraph();
        paragraph.add("iText at the ");
        Link chunk = new Link("European Business Awards",
                PdfAction.createURI("https://itextpdf.com/en/events/itext-european-business-awards-gala-milan"));
        paragraph.add(chunk);
        paragraph.add(" gala in Milan");
        table.addCell(paragraph);

        // The complete cell is a link:
        Cell cell = new Cell().add(new Paragraph("Help us win a European Business Award!"));
        cell.setNextRenderer(new LinkInCellRenderer(cell, "http://itextpdf.com/blog/help-us-win-european-business-award"));
        table.addCell(cell);

        // The complete cell is a link (using SetAction() directly on cell):
        cell = new Cell().add(new Paragraph(
                "IText becomes Belgium’s National Public Champion in the 2016 European Business Awards"));
        cell.setAction(PdfAction.createURI(
                "http://itextpdf.com/en/blog/itext-becomes-belgiums-national-public-champion-2016-european-business-awards"));
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }


    private static class LinkInCellRenderer extends CellRenderer {
        protected String url;

        public LinkInCellRenderer(Cell modelElement, String url) {
            super(modelElement);
            this.url = url;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new LinkInCellRenderer((Cell) modelElement, url);
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
