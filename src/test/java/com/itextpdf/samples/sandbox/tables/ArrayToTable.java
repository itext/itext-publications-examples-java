/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/24404686/i-need-to-create-a-table-and-assign-the-values-into-the-table-in-pdf-using-javaf
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Category(SampleTest.class)
public class ArrayToTable extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/array_to_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ArrayToTable().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(8);
        List<List<String>> dataset = getData();
        for (List<String> record : dataset) {
            for (String field : record) {
                table.addCell(new Cell().add(new Paragraph(field)));
            }
        }
        doc.add(table);

        doc.close();
    }

    public List<List<String>> getData() {
        List<List<String>> data = new ArrayList<>();
        String[] tableTitleList = {" Title", " (Re)set", " Obs", " Mean", " Std.Dev", " Min", " Max", "Unit"};
        data.add(Arrays.asList(tableTitleList));
        for (int i = 0; i < 10; ) {
            List<String> dataLine = new ArrayList<>();
            i++;
            for (int j = 0; j < tableTitleList.length; j++) {
                dataLine.add(tableTitleList[j] + " " + i);
            }
            data.add(dataLine);
        }
        return data;
    }
}
