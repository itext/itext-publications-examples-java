package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.renderer.DivRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class AddFieldAfterParagraph {

    public static final String DEST = "./target/sandbox/acroforms/add_field_after_paragraph.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddFieldAfterParagraph().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("This is a paragraph.\nForm field will be inserted after it"));

        float fieldHeight = 20;
        float fieldWidth = 100;

        // 1st method: calculate position and create form field, using document's root layout area
        Rectangle freeBBox = doc.getRenderer().getCurrentArea().getBBox();
        float top = freeBBox.getTop();
        PdfTextFormField field = PdfFormField.createText(pdfDoc,
                new Rectangle(freeBBox.getLeft(), top - fieldHeight, fieldWidth, fieldHeight), "myField", "Value");
        form.addField(field);

        doc.add(new AreaBreak());

        // 2nd method: create field using custom renderer
        doc.add(new Paragraph("This is another paragraph.\nForm field will be inserted right after it."));
        doc.add(new TextFieldLayoutElement().setWidth(fieldWidth).setHeight(fieldHeight));
        doc.add(new Paragraph("This paragraph follows the form field"));

        pdfDoc.close();
    }

    private static class TextFieldRenderer extends DivRenderer {
        public TextFieldRenderer(TextFieldLayoutElement modelElement) {
            super(modelElement);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new TextFieldRenderer((TextFieldLayoutElement) modelElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);

            PdfAcroForm form = PdfAcroForm.getAcroForm(drawContext.getDocument(), true);
            PdfTextFormField field = PdfFormField.createText(drawContext.getDocument(),
                    occupiedArea.getBBox(), "myField2", "Another Value");
            form.addField(field);
        }
    }

    private static class TextFieldLayoutElement extends Div {
        @Override
        public IRenderer getRenderer() {
            return new TextFieldRenderer(this);
        }
    }

}
