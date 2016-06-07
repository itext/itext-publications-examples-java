/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/34439850
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class CheckboxCell2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/checkbox_cell2.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CheckboxCell2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(6);
        Cell cell;
        for (int i = 0; i < 6; i++) {
            cell = new Cell();
            cell.setNextRenderer(new CheckboxCellRenderer(cell, "cb" + i, i));
            cell.setHeight(50);
            table.addCell(cell);
        }

        doc.add(table);
        doc.close();
    }


    private class CheckboxCellRenderer extends CellRenderer {
        // The name of the check box field
        protected String name;
        protected int i;

        public CheckboxCellRenderer(Cell modelElement, String name, int i) {
            super(modelElement);
            this.name = name;
            this.i = i;
        }

        @Override
        public void draw(DrawContext drawContext) {
            Rectangle position = getOccupiedAreaBBox();
            // define the coordinates of the middle
            float x = (position.getLeft() + position.getRight()) / 2;
            float y = (position.getTop() + position.getBottom()) / 2;
            // define the position of a check box that measures 20 by 20
            Rectangle rect = new Rectangle(x - 10, y - 10, 20, 20);
            // define the check box
            PdfButtonFormField checkBox = PdfFormField.createCheckBox(drawContext.getDocument(), rect, name, "Yes");
            switch (i) {
                case 0:
                    checkBox.setCheckType(PdfFormField.TYPE_CHECK);
                    // Use this method if you changed any field parameters and didn't use setValue
                    checkBox.regenerateField();
                    break;
                case 1:
                    checkBox.setCheckType(PdfFormField.TYPE_CIRCLE);
                    checkBox.regenerateField();
                    break;
                case 2:
                    checkBox.setCheckType(PdfFormField.TYPE_CROSS);
                    checkBox.regenerateField();
                    break;
                case 3:
                    checkBox.setCheckType(PdfFormField.TYPE_DIAMOND);
                    checkBox.regenerateField();
                    break;
                case 4:
                    checkBox.setCheckType(PdfFormField.TYPE_SQUARE);
                    checkBox.regenerateField();
                    break;
                case 5:
                    checkBox.setCheckType(PdfFormField.TYPE_STAR);
                    checkBox.regenerateField();
                    break;
            }
            // add the check box as a field
            PdfAcroForm.getAcroForm(drawContext.getDocument(), true).addField(checkBox);
        }
    }
}
