package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class CreateRadioInTable {
    public static final String DEST = "./target/sandbox/acroforms/create_radio_in_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CreateRadioInTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        // Radio buttons will be added to this radio group
        PdfButtonFormField radioGroup = PdfFormField.createRadioGroup(pdfDoc, "Language", "");

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        Cell cell = new Cell().add(new Paragraph("English"));
        table.addCell(cell);

        cell = new Cell();

        // The renderer creates radio button for the current radio group in the current cell
        cell.setNextRenderer(new AddRadioButtonRenderer(cell, radioGroup, "english"));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("French"));
        table.addCell(cell);

        cell = new Cell();
        cell.setNextRenderer(new AddRadioButtonRenderer(cell, radioGroup, "french"));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("Dutch"));
        table.addCell(cell);

        cell = new Cell();
        cell.setNextRenderer(new AddRadioButtonRenderer(cell, radioGroup, "dutch"));
        table.addCell(cell);

        doc.add(table);

        form.addField(radioGroup);

        doc.close();
    }


    private class AddRadioButtonRenderer extends CellRenderer {
        protected String value;
        protected PdfButtonFormField radioGroup;

        public AddRadioButtonRenderer(Cell modelElement, PdfButtonFormField radioGroup, String fieldName) {
            super(modelElement);
            this.radioGroup = radioGroup;
            this.value = fieldName;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new AddRadioButtonRenderer((Cell) modelElement, radioGroup, value);
        }

        @Override
        public void draw(DrawContext drawContext) {

            // Create a radio button that is added to a radio group.
            PdfFormField.createRadioButton(drawContext.getDocument(), getOccupiedAreaBBox(), radioGroup, value);
        }
    }
}
