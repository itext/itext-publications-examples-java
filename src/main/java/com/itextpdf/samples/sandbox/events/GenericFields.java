package com.itextpdf.samples.sandbox.events;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.forms.fields.TextFormFieldBuilder;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TextRenderer;

import java.io.File;

public class GenericFields {
    public static final String DEST = "./target/sandbox/events/generic_fields.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new GenericFields().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph p = new Paragraph();
        p.add("The Effective Date is ");

        Text day = new Text("     ");
        day.setNextRenderer(new FieldTextRenderer(day, "day"));
        p.add(day);
        p.add(" day of ");

        Text month = new Text("     ");
        month.setNextRenderer(new FieldTextRenderer(month, "month"));
        p.add(month);
        p.add(", ");

        Text year = new Text("            ");
        year.setNextRenderer(new FieldTextRenderer(year, "year"));
        p.add(year);
        p.add(" that this will begin.");

        doc.add(p);

        doc.close();
    }


    private static class FieldTextRenderer extends TextRenderer {
        protected String fieldName;

        public FieldTextRenderer(Text textElement, String fieldName) {
            super(textElement);
            this.fieldName = fieldName;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new FieldTextRenderer((Text) modelElement, fieldName);
        }

        @Override
        public void draw(DrawContext drawContext) {
            PdfTextFormField field = new TextFormFieldBuilder(drawContext.getDocument(), fieldName)
                    .setWidgetRectangle(getOccupiedAreaBBox()).createText();
            PdfAcroForm.getAcroForm(drawContext.getDocument(), true)
                    .addField(field);
        }
    }
}
