package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.IOException;

public class LeftRight {
    public static final String DEST = "./target/sandbox/objects/left_right.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new LeftRight().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph p = new Paragraph("Text to the left");
        p.add(new Tab());
        p.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
        p.add("Text to the right");
        doc.add(p);

        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        table.addCell(getCell("Text to the left", TextAlignment.LEFT));
        table.addCell(getCell("Text in the middle", TextAlignment.CENTER));
        table.addCell(getCell("Text to the right", TextAlignment.RIGHT));
        doc.add(table);

        doc.close();
    }

    private static Cell getCell(String text, TextAlignment alignment) {
        Cell cell = new Cell().add(new Paragraph(text));
        cell.setPadding(0);
        cell.setTextAlignment(alignment);
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }
}
