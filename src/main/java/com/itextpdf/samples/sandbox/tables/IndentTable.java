package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class IndentTable {
    public static final String DEST = "./target/sandbox/tables/indent_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new IndentTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfCanvas cb = new PdfCanvas(pdfDoc.addNewPage());
        cb.moveTo(36, 842);
        cb.lineTo(36, 0);
        cb.stroke();

        Table table = new Table(UnitValue.createPercentArray(8));
        table.setHorizontalAlignment(HorizontalAlignment.LEFT);
        table.setWidth(150);

        for (int aw = 0; aw < 16; aw++) {
            table.addCell(new Cell().add(new Paragraph("hi")));
        }

        table.setMarginLeft(25);

        doc.add(table);

        doc.close();
    }
}
