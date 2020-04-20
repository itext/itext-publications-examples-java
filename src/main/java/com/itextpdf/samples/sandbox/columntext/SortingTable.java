package com.itextpdf.samples.sandbox.columntext;

import com.itextpdf.kernel.colors.ColorConstants;
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
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SortingTable {
    public static final String DEST = "./target/sandbox/columntext/sorting_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SortingTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        ArrayList<PdfLayer> options = new ArrayList<PdfLayer>();
        PdfLayer radiogroup = PdfLayer.createTitle("Table", pdfDoc);

        PdfLayer radio1 = new PdfLayer("column1", pdfDoc);

        // To follow a "radio button" paradigm,
        // make sure, that no more that one column is selected.
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
        for (int i = 0; i < 3; i++) {
            pdfCanvas.beginLayer(options.get(i));
            Table table = createTable(i + 1, options);
            table.setFixedPosition(1, 36, 700, 523);
            doc.add(table);
            pdfCanvas.endLayer();
        }

        doc.close();
    }

    private static Table createTable(int c, List<PdfLayer> options) {
        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        for (int j = 0; j < 3; j++) {
            table.addCell(createHeaderCell(j, options));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                table.addCell(createCell(i + 1, j + 1, c));
            }
        }

        return table;
    }

    private static Cell createHeaderCell(int c, List<PdfLayer> options) {
        ArrayList<PdfDictionary> dictList = new ArrayList<>();
        dictList.add(options.get(c).getPdfObject());
        ArrayList<PdfActionOcgState> list = new ArrayList<PdfActionOcgState>();
        list.add(new PdfActionOcgState(PdfName.ON, dictList));

        // Create an action to set option content group state.
        PdfAction action = PdfAction.createSetOcgState(list, true);
        Link link = new Link("Column " + (c + 1), action);
        Cell cell = new Cell().add(new Paragraph(link));
        return cell;
    }

    private static Cell createCell(int i, int j, int c) {
        Cell cell = new Cell();
        cell.add(new Paragraph(String.format("row %s; column %s", i, j)));

        // If the current column is selected, then set different background color
        if (j == c) {
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        }

        return cell;
    }
}
