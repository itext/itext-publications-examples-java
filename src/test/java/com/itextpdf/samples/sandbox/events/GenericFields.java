/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/33247348/add-pdfpcell-to-paragraph
 */
package com.itextpdf.samples.sandbox.events;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TextRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class GenericFields extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/events/generic_fields.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new GenericFields().manipulatePdf(DEST);
    }

    @Override
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


    protected class FieldTextRenderer extends TextRenderer {
        protected String fieldName;

        public FieldTextRenderer(Text textElement, String fieldName) {
            super(textElement);
            this.fieldName = fieldName;
        }

        @Override
        public void draw(DrawContext drawContext) {
            PdfTextFormField field = PdfTextFormField.createText(drawContext.getDocument(), getOccupiedAreaBBox(), fieldName);
            PdfAcroForm.getAcroForm(drawContext.getDocument(), true).addField(field);
        }
    }
}
