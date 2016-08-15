/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/38761938
 */
package com.itextpdf.samples.sandbox.columntext;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.action.PdfActionOcgState;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.layer.PdfLayer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Category(SampleTest.class)
public class SortingTable extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/columntext/sorting_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SortingTable().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        ArrayList<PdfLayer> options = new ArrayList<PdfLayer>();
        PdfLayer radiogroup = PdfLayer.createTitle("Table", pdfDoc);
        PdfLayer radio1 = new PdfLayer("column1", pdfDoc);
        radio1.setOn(true);
        options.add(radio1);
        radiogroup.addChild(radio1);
        PdfLayer radio2 = new PdfLayer("column2", pdfDoc);
        radio2.setOn(false);
        options.add(radio2);
        radiogroup.addChild(radio2);
        PdfLayer radio3 = new PdfLayer("column3", pdfDoc);
        radio3.setOn(false);
        options.add(radio3);

        radiogroup.addChild(radio3);
        PdfLayer.addOCGRadioGroup(pdfDoc, options);

        PdfCanvas pdfCanvas = new PdfCanvas(pdfDoc.addNewPage());
        Table table;
        for (int i = 1; i < 4; i++) {
            pdfCanvas.beginLayer(options.get(i - 1));
            table = createTable(i, options);
            table.setFixedPosition(1, 36, 700, 523);
            doc.add(table);
            pdfCanvas.endLayer();
        }

        doc.close();
    }

    public Table createTable(int c, List<PdfLayer> options) {
        Table table = new Table(3);
        for (int j = 1; j < 4; j++) {
            table.addCell(createCell(j, options));
        }
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                table.addCell(createCell(i, j, c));
            }
        }
        return table;
    }

    public Cell createCell(int c, List<PdfLayer> options) {
        ArrayList<PdfActionOcgState> list = new ArrayList<PdfActionOcgState>();
        ArrayList<PdfDictionary> dictList = new ArrayList<>();
        dictList.add(options.get(c - 1).getPdfObject());
        list.add(new PdfActionOcgState(PdfName.ON, dictList));

        PdfAction action = PdfAction.createSetOcgState(list, true);
        Link link = new Link("Column " + c, action);
        return new Cell().add(new Paragraph(link));
    }

    public Cell createCell(int i, int j, int c) {
        Cell cell = new Cell();
        cell.add(new Paragraph(String.format("row %s; column %s", i, j)));
        if (j == c) {
            cell.setBackgroundColor(Color.LIGHT_GRAY);
        }
        return cell;
    }
}