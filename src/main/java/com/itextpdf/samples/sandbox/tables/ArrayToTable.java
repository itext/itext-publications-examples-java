package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayToTable {
    public static final String DEST = "./target/sandbox/tables/array_to_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ArrayToTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // By default column width is calculated automatically for the best fit.
        // useAllAvailableWidth() method makes table use the whole page's width while placing the content.
        Table table = new Table(UnitValue.createPercentArray(8)).useAllAvailableWidth();

        List<List<String>> dataset = getData();
        for (List<String> record : dataset) {
            for (String field : record) {
                table.addCell(new Cell().add(new Paragraph(field)));
            }
        }

        doc.add(table);

        doc.close();
    }

    private static List<List<String>> getData() {
        List<List<String>> data = new ArrayList<>();
        String[] tableTitleList = {" Title", " (Re)set", " Obs", " Mean", " Std.Dev", " Min", " Max", "Unit"};
        data.add(Arrays.asList(tableTitleList));

        for (int i = 0; i < 10; i++) {
            List<String> dataLine = new ArrayList<>();
            for (int j = 0; j < tableTitleList.length; j++) {
                dataLine.add(tableTitleList[j] + " " + (i + 1));
            }
            data.add(dataLine);
        }

        return data;
    }
}
