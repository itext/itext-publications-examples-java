/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.fields.TextFormFieldBuilder;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class CreateFormInTable {
    public static final String DEST = "./target/sandbox/acroforms/create_form_in_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CreateFormInTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        Cell cell = new Cell().add(new Paragraph("Name:"));
        table.addCell(cell);

        cell = new Cell();
        cell.setNextRenderer(new CreateFormFieldRenderer(cell, "name"));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("Address"));
        table.addCell(cell);

        cell = new Cell();
        cell.setNextRenderer(new CreateFormFieldRenderer(cell, "address"));
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }


    private class CreateFormFieldRenderer extends CellRenderer {
        protected String fieldName;

        public CreateFormFieldRenderer(Cell modelElement, String fieldName) {
            super(modelElement);
            this.fieldName = fieldName;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new CreateFormFieldRenderer((Cell) modelElement, fieldName);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);

            PdfTextFormField field = new TextFormFieldBuilder(drawContext.getDocument(), fieldName)
                    .setWidgetRectangle(getOccupiedAreaBBox()).createText();
            field.setValue("");
            PdfAcroForm form = PdfFormCreator.getAcroForm(drawContext.getDocument(), true);
            form.addField(field);
        }
    }
}
