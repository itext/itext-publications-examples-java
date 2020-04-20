package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class FitTableOnPage {
    public static final String DEST = "./target/sandbox/tables/fit_table_on_page.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FitTableOnPage().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();

        for (int i = 0; i < 10; i++) {
            Cell cell;

            if (i == 9) {
                cell = new Cell().add(new Paragraph("Two\nLines"));
            } else {
                cell = new Cell().add(new Paragraph(Integer.toString(i)));
            }

            table.addCell(cell);
        }

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // Fictitiously layout the element to find out, how much space it takes
        IRenderer tableRenderer = table.createRendererSubTree().setParent(doc.getRenderer());
        LayoutResult tableLayoutResult = tableRenderer.layout(new LayoutContext(
                new LayoutArea(0, new Rectangle(550 + 72, 1000))));

        pdfDoc.setDefaultPageSize(new PageSize(550 + 72,
                tableLayoutResult.getOccupiedArea().getBBox().getHeight() + 72));

        doc.add(table);

        doc.close();
    }
}
