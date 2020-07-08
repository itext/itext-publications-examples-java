package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class SplitRowAtSpecificRow {
    public static final String DEST = "./target/sandbox/tables/split_row_at_specific_row.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SplitRowAtSpecificRow().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();

        // Notice that the width is bigger than available area (612 - 36 - 36 = 540, where 36 is the value of the left (and the right) margin
        table.setWidth(550);

        for (int i = 0; i < 6; i++) {
            Cell cell = new Cell()
                    .add(new Paragraph((i == 5) ? "Three\nLines\nHere" : Integer.toString(i)));

            table.addCell(cell);
        }

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(612, 237));

        doc.add(table);

        doc.close();
    }
}
