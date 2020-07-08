package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class TableWithTab {
    public static final String DEST = "./target/sandbox/tables/table_with_tab.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TableWithTab().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
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
