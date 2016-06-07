/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/28590487/adding-table-to-existing-pdf-on-the-same-page-itext
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.util.Map;

@Category(SampleTest.class)
public class AddExtraTable extends GenericTest {
    public static String DEST = "./target/test/resources/sandbox/acroforms/add_extra_table.pdf";
    public static String SRC = "./src/test/resources/pdfs/form.pdf";

    public static void main(String[] args) throws Exception {
        new AddExtraTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Map<String, PdfFormField> fields = form.getFormFields();
        fields.get("Name").setValue("Jeniffer");
        fields.get("Company").setValue("iText's next customer");
        fields.get("Country").setValue("No Man's Land");
        form.flattenFields();

        Table table = new Table(new float[]{1, 15});
        table.setWidthPercent(80);
        table.addHeaderCell("#");
        table.addHeaderCell("description");
        for (int i = 1; i <= 150; i++) {
            table.addCell(String.valueOf(i));
            table.addCell("test " + i);
        }

        doc.setRenderer(new DocumentRenderer(doc) {
            @Override
            protected LayoutArea updateCurrentArea(LayoutResult overflowResult) {
                LayoutArea area = super.updateCurrentArea(overflowResult);
                if (area.getPageNumber() == 1) {
                    area.getBBox().decreaseHeight(266);
                }
                return area;
            }
        });

        doc.add(table);

        doc.close();
    }
}
