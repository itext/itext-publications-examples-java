package com.itextpdf.samples.sandbox.acroforms.reporting;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class FillFlattenMerge3 {
    public static final String DEST = "./target/sandbox/acroforms/reporting/fill_flatten_merge3.pdf";

    public static final String DATA = "./src/main/resources/data/united_states.csv";
    public static final String SRC = "./src/main/resources/pdfs/state.pdf";

    public static final String[] FIELDS = {
            "name", "abbr", "capital", "city", "population", "surface", "timezone1", "timezone2", "dst"
    };

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FillFlattenMerge3().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        PdfAcroForm form = PdfAcroForm.getAcroForm(srcDoc, true);

        // Create a map with fields from the acroform and their names
        Map<String, Rectangle> positions = new HashMap<>();
        Map<String, PdfFormField> fields = form.getFormFields();
        for (PdfFormField field : fields.values()) {
            positions.put(field.getFieldName().getValue(), field.getWidgets().get(0).getRectangle().toRectangle());
        }

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // Event handler copies content of the source pdf file on every page
        // of the result pdf file as template to fill in.
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE,
                new PaginationEventHandler(srcDoc.getFirstPage().copyAsFormXObject(pdfDoc)));
        srcDoc.close();

        try (BufferedReader br = new BufferedReader(new FileReader(DATA))) {

            // Read first line with headers,
            // do nothing with current text line, because headers are already filled in form
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                int i = 0;
                StringTokenizer tokenizer = new StringTokenizer(line, ";");

                pdfDoc.addNewPage();

                while (tokenizer.hasMoreTokens()) {

                    // Fill in current form field, got by the name from FIELDS[],
                    // with content, read from the current token
                    process(doc, FIELDS[i++], tokenizer.nextToken(), font, positions);
                }
            }
        }

        doc.close();
    }

    protected void process(Document doc, String name, String value, PdfFont font, Map<String, Rectangle> positions) {
        Rectangle rect = positions.get(name);
        Paragraph p = new Paragraph(value).setFont(font).setFontSize(10);

        doc.showTextAligned(p, rect.getLeft() + 2, rect.getBottom() + 2, doc.getPdfDocument().getNumberOfPages(),
                TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
    }


    protected class PaginationEventHandler implements IEventHandler {
        PdfFormXObject background;

        public PaginationEventHandler(PdfFormXObject background) throws IOException {
            this.background = background;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocument pdfDoc = ((PdfDocumentEvent) event).getDocument();
            int pageNum = pdfDoc.getPageNumber(((PdfDocumentEvent) event).getPage());

            // Add the background
            PdfCanvas canvas = new PdfCanvas(pdfDoc.getPage(pageNum).newContentStreamBefore(),
                    ((PdfDocumentEvent) event).getPage().getResources(), pdfDoc)
                    .addXObjectAt(background, 0, 0);

            // Add the page number
            new Canvas(canvas, ((PdfDocumentEvent) event).getPage().getPageSize())
                    .showTextAligned("page " + pageNum, 550, 800, TextAlignment.RIGHT);
        }
    }
}
