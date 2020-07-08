package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class LeadingInCell {
    public static final String DEST = "./target/sandbox/tables/leading_in_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new LeadingInCell().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(1));
        table.setWidth(UnitValue.createPercentValue(60));

        Cell cell = new Cell();

        Paragraph p = new Paragraph("paragraph 1: leading 16. Text to force a wrap and check the leading. Ha-ha")
                .setFixedLeading(16);
        cell.add(p);

        p = new Paragraph("paragraph 2: leading 32. Text to force a wrap and check the leading. Ha-ha")
                .setFixedLeading(32);
        cell.add(p);

        p = new Paragraph("paragraph 3: leading 10. Text to force a wrap and check the leading. Ha-ha")
                .setFixedLeading(10);
        cell.add(p);

        p = new Paragraph("paragraph 4: leading 18. Text to force a wrap and check the leading. Ha-ha")
                .setFixedLeading(18);
        cell.add(p);

        p = new Paragraph("paragraph 5: leading 40. Text to force a wrap and check the leading. Ha-ha")
                .setFixedLeading(40);
        cell.add(p);

        table.addCell(cell);

        doc.add(table);

        doc.close();
    }
}
