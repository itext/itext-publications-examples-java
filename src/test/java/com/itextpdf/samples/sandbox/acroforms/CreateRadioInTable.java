/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/29393050/itext-radiogroup-radiobuttons-across-multiple-pdfpcells
 */
package com.itextpdf.samples.sandbox.acroforms;

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
public class CreateRadioInTable extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/create_radio_in_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CreateRadioInTable().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfButtonFormField group = PdfFormField.createRadioGroup(pdfDoc, "Language", "English");
        Table table = new Table(2);
        Cell cell;
        cell = new Cell().add("English");
        table.addCell(cell);
        cell = new Cell();
        cell.setNextRenderer(new MyCellRenderer(cell, group, "english"));
        table.addCell(cell);
        cell = new Cell().add("French");
        table.addCell(cell);
        cell = new Cell();
        cell.setNextRenderer(new MyCellRenderer(cell, group, "french"));
        table.addCell(cell);
        cell = new Cell().add("Dutch");
        table.addCell(cell);
        cell = new Cell();
        cell.setNextRenderer(new MyCellRenderer(cell, group, "dutch"));
        table.addCell(cell);
        doc.add(table);

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        form.addField(group);

        doc.close();
    }


    private class MyCellRenderer extends CellRenderer {
        protected String value;
        protected PdfButtonFormField radioGroup;

        public MyCellRenderer(Cell modelElement, PdfButtonFormField radioGroup, String fieldName) {
            super(modelElement);
            this.radioGroup = radioGroup;
            this.value = fieldName;
        }

        @Override
        public void draw(DrawContext drawContext) {
            PdfFormField.createRadioButton(drawContext.getDocument(), getOccupiedAreaBBox(), radioGroup, value);
        }
    }
}
