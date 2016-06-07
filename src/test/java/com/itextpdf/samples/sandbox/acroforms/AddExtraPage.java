/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/26853894/continue-field-output-on-second-page-with-itextsharp
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.IOException;

@Category(SampleTest.class)
public class AddExtraPage extends GenericTest {
    public static String DEST = "./target/test/resources/sandbox/acroforms/add_extra_page.pdf";
    public static String SRC = "./src/test/resources/pdfs/stationery.pdf";

    public static void main(String[] args) throws Exception {
        new AddExtraPage().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        PdfAcroForm form = PdfAcroForm.getAcroForm(srcDoc, false);

        Rectangle rect = form.getField("body").getWidgets().get(0).getRectangle().toRectangle();

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE,
                new PaginationEventHandler(srcDoc.getFirstPage().copyAsFormXObject(pdfDoc)));
        srcDoc.close();

        doc.setRenderer(new ColumnDocumentRenderer(doc, new Rectangle[]{rect}));

        Paragraph p = new Paragraph();
        // the easiest way to add a Text object to Paragraph
        p.add("Hello ");
        // use add(Text) if you want to specify some Text characteristics, for example, font size
        p.add(new Text("World").setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)));

        for (int i = 1; i < 101; i++) {
            doc.add(new Paragraph("Hello " + i));
            doc.add(p);
        }

        doc.close();
    }


    protected class PaginationEventHandler implements IEventHandler {
        protected PdfFormXObject background;

        public PaginationEventHandler(PdfFormXObject background) throws IOException {
            this.background = background;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocument pdfDoc = ((PdfDocumentEvent) event).getDocument();
            // Add the background
            new PdfCanvas(((PdfDocumentEvent) event).getPage().newContentStreamBefore(), ((PdfDocumentEvent) event).getPage().getResources(), pdfDoc)
                    .addXObject(background, 0, 0);
        }
    }
}
