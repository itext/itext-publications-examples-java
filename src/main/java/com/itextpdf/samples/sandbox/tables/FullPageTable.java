package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;

public class FullPageTable {
    public static final String DEST = "./target/sandbox/tables/full_page_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FullPageTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(595, 842));
        doc.setMargins(0, 0, 0, 0);

        Table table = new Table(new float[10]).useAllAvailableWidth();
        table.setMarginTop(0);
        table.setMarginBottom(0);

        // first row
        Cell cell = new Cell(1, 10).add(new Paragraph("DateRange"));
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setPadding(5);
        cell.setBackgroundColor(new DeviceRgb(140, 221, 8));
        table.addCell(cell);

        table.addCell("Calldate");
        table.addCell("Calltime");
        table.addCell("Source");
        table.addCell("DialedNo");
        table.addCell("Extension");
        table.addCell("Trunk");
        table.addCell("Duration");
        table.addCell("Calltype");
        table.addCell("Callcost");
        table.addCell("Site");

        for (int i = 0; i < 100; i++) {
            table.addCell("date" + i);
            table.addCell("time" + i);
            table.addCell("source" + i);
            table.addCell("destination" + i);
            table.addCell("extension" + i);
            table.addCell("trunk" + i);
            table.addCell("dur" + i);
            table.addCell("toc" + i);
            table.addCell("callcost" + i);
            table.addCell("Site" + i);
        }

        doc.add(table);

        doc.close();
    }
}
