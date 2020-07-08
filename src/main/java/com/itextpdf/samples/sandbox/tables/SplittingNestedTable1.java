package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class SplittingNestedTable1 {
    public static final String DEST = "./target/sandbox/tables/splitting_nested_table1.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SplittingNestedTable1().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(300, 210));

        doc.add(new Paragraph("Table with setKeepTogether(true):"));
        Table table = createTable(true);
        doc.add(table);

        doc.add(new AreaBreak());

        doc.add(new Paragraph("Table with setKeepTogether(false):"));
        table = createTable(false);
        doc.add(table);

        doc.close();
    }

    /**
     * Creates a table with two cells, the second of which consists of an inner table.
     *
     * @param keepTableTogether defines whether to keep the table together or not
     * @return a {@link Table table} with the format specified above
     */
    private static Table createTable(boolean keepTableTogether) {
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.setMarginTop(10);

        // If true, iText will do its best trying not to split the table and process it on a single area
        table.setKeepTogether(keepTableTogether);

        Cell cell = new Cell();
        cell.add(new Paragraph("G"));
        cell.add(new Paragraph("R"));
        cell.add(new Paragraph("O"));
        cell.add(new Paragraph("U"));
        cell.add(new Paragraph("P"));


        table.addCell(cell);

        Table inner = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        inner.addCell("row 1");
        inner.addCell("row 2");
        inner.addCell("row 3");
        inner.addCell("row 4");
        inner.addCell("row 5");

        cell = new Cell().add(inner);
        cell.setPadding(0);
        table.addCell(cell);

        return table;
    }
}
