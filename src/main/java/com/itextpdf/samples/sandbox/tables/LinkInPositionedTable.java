package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class LinkInPositionedTable {
    public static final String DEST = "./target/sandbox/tables/link_in_positioned_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new LinkInPositionedTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(1));
        table.setWidth(500);

        Cell cell = new Cell();
        Paragraph p = new Paragraph();

        Link link = new Link("link to top of next page", PdfAction.createGoTo("top"));
        p.add(link);
        cell.add(p);
        table.addCell(cell);

        doc.add(table);
        doc.add(new AreaBreak());

        // Creates a target that the link leads to
        Paragraph target = new Paragraph("top");
        target.setDestination("top");
        doc.add(target);

        doc.close();
    }
}
