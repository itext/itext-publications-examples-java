/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/28236902/itext-combobox-width-of-selected-option-issue
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfChoiceFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Category(SampleTest.class)
public class ComboBoxItems extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/combo_box_items.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ComboBoxItems().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(612, 792));

        Table table = new Table(2);
        Cell cell;
        // Add rows with selectors
        String[] options = {"Choose first option", "Choose second option", "Choose third option"};
        String[] exports = {"option1", "option2", "option3"};
        table.addCell(new Cell().add("Combobox:"));
        cell = new Cell();
        cell.setNextRenderer(new SelectCellRenderer(cell, "Choose first option", exports, options));
        cell.setHeight(20);
        table.addCell(cell);
        doc.add(table);

        doc.close();
    }


    private class SelectCellRenderer extends CellRenderer {
        protected String name;
        protected String[] exports;
        protected String[] options;

        public SelectCellRenderer(Cell modelElement, String name, String[] exports, String[] options) {
            super(modelElement);
            this.name = name;
            this.exports = exports;
            this.options = options;
        }

        @Override
        public void draw(DrawContext drawContext) {
            PdfFont font;
            try {
                font = PdfFontFactory.createFont(FontConstants.HELVETICA);
            } catch (IOException e) {
                throw new PdfException(e);
            }
            String[][] optionsArray = new String[options.length][];
            for (int i = 0; i < options.length; i++) {
                optionsArray[i] = new String[2];
                optionsArray[i][0] = exports[i];
                optionsArray[i][1] = options[i];
            }
            PdfAcroForm form = PdfAcroForm.getAcroForm(drawContext.getDocument(), true);
            PdfChoiceFormField choice = PdfFormField.createComboBox(drawContext.getDocument(), getOccupiedAreaBBox(),
                    name, name, optionsArray);
            choice.setFont(font);
            choice.getWidgets().get(0).setBorderStyle(PdfAnnotation.STYLE_BEVELED);
            choice.setVisibility(PdfFormField.VISIBLE_BUT_DOES_NOT_PRINT);
            choice.setBorderColor(Color.GRAY);
            choice.setJustification(PdfFormField.ALIGN_CENTER);
            form.addField(choice);
        }
    }
}
