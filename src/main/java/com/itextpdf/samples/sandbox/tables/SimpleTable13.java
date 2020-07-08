package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class SimpleTable13 {
    public static final String DEST = "./target/sandbox/tables/simple_table13.pdf";
    public static final String[][] DATA = {
            {"John Edward Jr.", "AAA"},
            {"Pascal Einstein W. Alfi", "BBB"},
            {"St. John", "CCC"}
    };

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SimpleTable13().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(new float[] {5, 1}));
        table.setWidth(UnitValue.createPercentValue(50));
        table.setTextAlignment(TextAlignment.LEFT);

        table.addCell(new Cell().add(new Paragraph("Name: " + DATA[0][0])).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(DATA[0][1])).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Surname: " + DATA[1][0])).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(DATA[1][1])).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("School: " + DATA[2][0])).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(DATA[1][1])).setBorder(Border.NO_BORDER));

        doc.add(table);

        doc.close();
    }
}
