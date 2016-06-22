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
import com.itextpdf.samples.GenericTest;

import java.io.File;

/**
 * This example is an answer to a StackOverflow question:
 * http://stackoverflow.com/questions/37969102/how-can-i-add-an-pdfformfield-using-itext-7-at-the-current-page-position
 */
public class AddFieldAfterParagraph extends GenericTest {

    public static final String DEST = "./target/test/resources/sandbox/acroforms/add_field_after_paragraph.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddField().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("This is a paragraph.\nForm field will be inserted after it"));

        Rectangle freeBBox = doc.getRenderer().getCurrentArea().getBBox();
        float top = freeBBox.getTop();
        float fieldHeight = 20;
        PdfTextFormField field = PdfFormField.createText(pdfDoc,
                new Rectangle(freeBBox.getLeft(), top - fieldHeight, 100, fieldHeight), "myField", "Value");
        form.addField(field);

        doc.add(new AreaBreak());
        doc.add(new Paragraph("This is another paragraph.\nForm field will be inserted right after it."));
        doc.add(new TextFieldLayoutElement().setWidth(100).setHeight(20));
        doc.add(new Paragraph("This paragraph follows the form field"));

        pdfDoc.close();
    }

    private static class TextFieldRenderer extends DivRenderer {
        public TextFieldRenderer(TextFieldLayoutElement modelElement) {
            super(modelElement);
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
