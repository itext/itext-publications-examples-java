/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/24270195/wrapping-of-arabic-text-using-acrofields-in-itext-5-5
 */
package com.itextpdf.samples.sandbox.acroforms.reporting;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.FontConstants;
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
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@Category(SampleTest.class)
public class FillFlattenMerge3 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/reporting/fill_flatten_merge3.pdf";
    public static final String DATA = "./src/test/resources/data/united_states.csv";
    public static final String[] FIELDS = {
            "name", "abbr", "capital", "city", "population", "surface", "timezone1", "timezone2", "dst"
    };
    public static final String SRC = "./src/test/resources/pdfs/state.pdf";

    protected Map<String, Rectangle> positions;

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FillFlattenMerge3().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        PdfAcroForm form = PdfAcroForm.getAcroForm(srcDoc, true);
        positions = new HashMap<>();
        Map<String, PdfFormField> fields = form.getFormFields();
        for (PdfFormField field : fields.values()) {
            positions.put(field.getFieldName().getValue(), field.getWidgets().get(0).getRectangle().toRectangle());
        }

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new PaginationEventHandler(srcDoc.getFirstPage().copyAsFormXObject(pdfDoc)));
        srcDoc.close();

        StringTokenizer tokenizer;
        BufferedReader br = new BufferedReader(new FileReader(DATA));
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            pdfDoc.addNewPage();
            int i = 0;
            tokenizer = new StringTokenizer(line, ";");
            while (tokenizer.hasMoreTokens()) {
                process(doc, FIELDS[i++], tokenizer.nextToken(), font);
            }
        }
        br.close();

        doc.close();
    }

    protected void process(Document doc, String name, String value, PdfFont font) {
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
                    .addXObject(background, 0, 0);
            // Add the page number
            new Canvas(canvas, pdfDoc, ((PdfDocumentEvent) event).getPage().getPageSize())
                    .showTextAligned("page " + pageNum, 550, 800, TextAlignment.RIGHT);
        }
    }
}
