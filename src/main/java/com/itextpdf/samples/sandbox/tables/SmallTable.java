package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;

public class SmallTable {
    public static final String DEST = "./target/sandbox/tables/small_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SmallTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(290, 112));
        doc.setMargins(5, 5, 5, 5);

        Table table = new Table(new float[] {160, 120});

        // first row
        Cell cell = new Cell(1, 2).add(new Paragraph("Some text here"));
        cell.setHeight(30);
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);

        // second row
        cell = new Cell().add(new Paragraph("Some more text").setFontSize(10));
        cell.setHeight(30);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);

        Barcode128 code128 = new Barcode128(pdfDoc);
        code128.setCode("14785236987541");
        code128.setCodeType(Barcode128.CODE128);

        Image code128Image = new Image(code128.createFormXObject(pdfDoc));

        cell = new Cell().add(code128Image.setAutoScale(true));
        cell.setBorder(Border.NO_BORDER);
        cell.setHeight(30);
        table.addCell(cell);

        // third row
        table.addCell(cell.clone(true));
        cell = new Cell().add(new Paragraph("and something else here").setFontSize(10));
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.RIGHT);
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }
}
