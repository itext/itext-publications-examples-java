package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class TableTemplate {
    public static final String DEST = "./target/sandbox/tables/table_template.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TableTemplate().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Table table = new Table(UnitValue.createPercentArray(15));

        table.setWidth(1500);

        for (int r = 'A'; r <= 'Z'; r++) {
            for (int c = 1; c <= 15; c++) {
                Cell cell = new Cell();
                cell.setMinHeight(45);
                cell.add(new Paragraph(String.valueOf((char) r) + c));
                table.addCell(cell);
            }
        }

        PdfFormXObject tableTemplate = new PdfFormXObject(new Rectangle(1500, 1300));
        Canvas canvas = new Canvas(tableTemplate, pdfDoc);
        canvas.add(table);

        for (int j = 0; j < 1500; j += 500) {
            for (int i = 1300; i > 0; i -= 650) {
                PdfFormXObject clip = new PdfFormXObject(new Rectangle(500, 650));

                // add xObject to another xObject of shorter sizes
                new PdfCanvas(clip, pdfDoc).addXObjectAt(tableTemplate, -j, 650 - i);

                // add xObject to the document
                new PdfCanvas(pdfDoc.addNewPage()).addXObjectAt(clip, 36, 156);
            }
        }

        pdfDoc.close();
    }
}
