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
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;
import java.util.Map;

public class AddExtraTable {
    public static String DEST = "./target/sandbox/acroforms/add_extra_table.pdf";

    public static String SRC = "./src/main/resources/pdfs/form.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddExtraTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Map<String, PdfFormField> fields = form.getFormFields();
        fields.get("Name").setValue("Jeniffer");
        fields.get("Company").setValue("iText's next customer");
        fields.get("Country").setValue("No Man's Land");
        form.flattenFields();

        Table table = new Table(UnitValue.createPercentArray(new float[] {1, 15}));
        table.setWidth(UnitValue.createPercentValue(80));
        table.addHeaderCell("#");
        table.addHeaderCell("description");
        for (int i = 1; i <= 150; i++) {
            table.addCell(String.valueOf(i));
            table.addCell("test " + i);
        }

        // The custom renderer decreases the first page's area.
        // As a result, there is not overlapping between the table from acroform and the new one.
        doc.setRenderer(new ExtraTableRenderer(doc));
        doc.add(table);

        doc.close();
    }

    protected static class ExtraTableRenderer extends DocumentRenderer {

        public ExtraTableRenderer(Document document) {
            super(document);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new ExtraTableRenderer(document);
        }

        @Override
        protected LayoutArea updateCurrentArea(LayoutResult overflowResult) {
            LayoutArea area = super.updateCurrentArea(overflowResult);
            if (area.getPageNumber() == 1) {
                area.getBBox().decreaseHeight(266);
            }

            return area;
        }

    }
}
