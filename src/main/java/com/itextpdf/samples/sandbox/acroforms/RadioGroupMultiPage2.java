package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class RadioGroupMultiPage2 {
    public static final String DEST = "./target/sandbox/acroforms/radio_group_multi_page2.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RadioGroupMultiPage2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        // Radio buttons will be added to this radio group
        PdfButtonFormField radioGroup = PdfFormField.createRadioGroup(pdfDoc, "answer", "answer 1");

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        for (int i = 0; i < 25; i++) {
            Cell cell = new Cell().add(new Paragraph("Question " + i));
            table.addCell(cell);

            cell = new Cell().add(new Paragraph("Answer " + i));
            table.addCell(cell);
        }

        for (int i = 0; i < 25; i++) {
            Cell cell = new Cell().add(new Paragraph("Radio: " + i));
            table.addCell(cell);

            cell = new Cell();

            // The renderer creates radio button for the current radio group in the current cell
            cell.setNextRenderer(new AddRadioButtonRenderer(cell, radioGroup, "answer " + i));
            table.addCell(cell);
        }

        doc.add(table);

        form.addField(radioGroup);

        pdfDoc.close();
    }


    private class AddRadioButtonRenderer extends CellRenderer {
        protected PdfButtonFormField radioGroup;
        protected String value;

        public AddRadioButtonRenderer(Cell modelElement, PdfButtonFormField radioGroup, String value) {
            super(modelElement);
            this.radioGroup = radioGroup;
            this.value = value;
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
            PdfDocument document = drawContext.getDocument();
            PdfAcroForm form = PdfAcroForm.getAcroForm(document, true);

            // Create a radio button that is added to a radio group.
            PdfFormField field = PdfFormField.createRadioButton(document, getOccupiedAreaBBox(), radioGroup, value);

            // This method merges field with its annotation and place it on the given page.
            // This method won't work if the field has no or more than one widget annotations.
            form.addFieldAppearanceToPage(field, document.getPage(getOccupiedArea().getPageNumber()));
        }
    }
}
