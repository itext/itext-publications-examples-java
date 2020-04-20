package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.BorderCollapsePropertyValue;
import com.itextpdf.layout.property.BorderRadius;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class RoundedCorners {
    public static final String DEST = "./target/sandbox/tables/rounded_corners.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RoundedCorners().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();

        // By default iText collapses borders and draws them on table level.
        // In this sample, however, we want each cell to draw its borders separately,
        // that's why we need to override border collapse.
        table.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);

        // Sets horizontal spacing between all the table's cells. See css's border-spacing for more information.
        table.setHorizontalBorderSpacing(5);

        Cell cell = getCell("These cells have rounded borders at the top.");
        table.addCell(cell);

        cell = getCell("These cells aren't rounded at the bottom.");
        table.addCell(cell);

        cell = getCell("A custom cell event was used to achieve this.");
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }

    private static Cell getCell(String content) {
        Cell cell = new Cell().add(new Paragraph(content));
        cell.setBorderTopRightRadius(new BorderRadius(4));
        cell.setBorderTopLeftRadius(new BorderRadius(4));
        return cell;
    }
}
