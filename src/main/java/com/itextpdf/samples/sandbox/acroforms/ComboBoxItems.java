package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.ChoiceFormFieldBuilder;
import com.itextpdf.forms.fields.PdfChoiceFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfFormAnnotation;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.exceptions.PdfException;
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
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;
import java.io.IOException;

public class ComboBoxItems {
    public static final String DEST = "./target/sandbox/acroforms/combo_box_items.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ComboBoxItems().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(612, 792));

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.addCell(new Cell().add(new Paragraph("Combobox:")));
        Cell cell = new Cell();

        // Add rows with selectors
        String[] options = {"Choose first option", "Choose second option", "Choose third option"};
        String[] exports = {"option1", "option2", "option3"};

        // The renderer creates combobox in the current cell
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

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new SelectCellRenderer((Cell) modelElement, name, exports, options);
        }

        @Override
        public void draw(DrawContext drawContext) {
            PdfFont font;
            try {
                font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
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

            // The 3rd parameter is the combobox name, the 4th parameter is the combobox's initial value
            PdfChoiceFormField choice = new ChoiceFormFieldBuilder(drawContext.getDocument(), name)
                    .setWidgetRectangle(getOccupiedAreaBBox()).setOptions(optionsArray).createComboBox();
            choice.setValue(name);
            choice.setFont(font);
            choice.getWidgets().get(0).setBorderStyle(PdfAnnotation.STYLE_BEVELED);
            choice.getFirstFormAnnotation().setVisibility(PdfFormAnnotation.VISIBLE_BUT_DOES_NOT_PRINT);
            choice.getFirstFormAnnotation().setBorderColor(ColorConstants.GRAY);
            choice.setJustification(TextAlignment.CENTER);
            form.addField(choice);
        }
    }
}
