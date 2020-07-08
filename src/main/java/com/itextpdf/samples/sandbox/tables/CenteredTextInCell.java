package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;

public class CenteredTextInCell {
    public static final String DEST = "./target/sandbox/tables/centered_text_in_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CenteredTextInCell().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        Paragraph para = new Paragraph("Test").setFont(font);
        para.setFixedLeading(0);
        para.setMultipliedLeading(1);

        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        Cell cell = new Cell();
        cell.setMinHeight(50);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.add(para);
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }
}
