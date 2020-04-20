package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class SimpleTable8 {
    public static final String DEST = "./target/sandbox/tables/simple_table8.pdf";

    public static final String SRC = "./src/main/resources/pdfs/";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SimpleTable8().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC + "header_test_file.pdf"));
        PdfFormXObject header = srcDoc.getFirstPage().copyAsFormXObject(pdfDoc);


        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        Cell cell = new Cell(1, 3).add(new Image(header).setWidth(UnitValue.createPercentValue(100))
                .setAutoScale(true));
        table.addCell(cell);

        for (int row = 1; row <= 50; row++) {
            for (int column = 1; column <= 3; column++) {
                table.addCell(String.format("row %s, column %s", row, column));
            }
        }

        srcDoc = new PdfDocument(new PdfReader(SRC + "footer_test_file.pdf"));
        PdfFormXObject footer = srcDoc.getFirstPage().copyAsFormXObject(pdfDoc);

        cell = new Cell(1, 3).add(new Image(footer).setWidth(UnitValue.createPercentValue(100))
                .setAutoScale(true));
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }
}
