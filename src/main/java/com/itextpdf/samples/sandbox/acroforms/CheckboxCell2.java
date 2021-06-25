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
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class CheckboxCell2 {
    public static final String DEST = "./target/sandbox/acroforms/checkbox_cell2.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CheckboxCell2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(6)).useAllAvailableWidth();
        for (int i = 0; i < 6; i++) {
            Cell cell = new Cell();

            // Custom renderer creates checkbox in the current cell
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
        protected int checkboxTypeIndex;

        public CheckboxCellRenderer(Cell modelElement, String name, int checkboxTypeIndex) {
            super(modelElement);
            this.name = name;
            this.checkboxTypeIndex = checkboxTypeIndex;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new CheckboxCellRenderer((Cell) modelElement, name, checkboxTypeIndex);
        }

        @Override
        public void draw(DrawContext drawContext) {
            Rectangle position = getOccupiedAreaBBox();
            PdfAcroForm form = PdfAcroForm.getAcroForm(drawContext.getDocument(), true);

            // Define the coordinates of the middle
            float x = (position.getLeft() + position.getRight()) / 2;
            float y = (position.getTop() + position.getBottom()) / 2;

            // Define the position of a check box that measures 20 by 20
            Rectangle rect = new Rectangle(x - 10, y - 10, 20, 20);

            // The 4th parameter is the initial value of checkbox: 'Yes' - checked, 'Off' - unchecked
            // By default, checkbox value type is cross.
            PdfButtonFormField checkBox = PdfFormField.createCheckBox(drawContext.getDocument(), rect, name, "Yes");
            switch (checkboxTypeIndex) {
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

            form.addField(checkBox);
        }
    }
}
