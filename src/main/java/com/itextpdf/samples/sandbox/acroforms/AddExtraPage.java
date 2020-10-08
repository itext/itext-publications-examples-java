package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.File;

public class AddExtraPage {
    public static String DEST = "./target/sandbox/acroforms/add_extra_page.pdf";

    public static String SRC = "./src/main/resources/pdfs/stationery.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddExtraPage().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        PdfAcroForm form = PdfAcroForm.getAcroForm(srcDoc, false);

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        // Event handler copies content of the source pdf file on every page
        // of the result pdf file
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE,
                new PaginationEventHandler(srcDoc.getFirstPage().copyAsFormXObject(pdfDoc)));
        srcDoc.close();

        Document doc = new Document(pdfDoc);
        Rectangle rect = form.getField("body").getWidgets().get(0).getRectangle().toRectangle();

        // The renderer will place content in columns specified with the rectangles
        doc.setRenderer(new ColumnDocumentRenderer(doc, new Rectangle[] {rect}));

        Paragraph p = new Paragraph();

        // The easiest way to add a Text object to Paragraph
        p.add("Hello ");

        // Use add(Text) if you want to specify some Text characteristics, for example, font size
        p.add(new Text("World").setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));

        for (int i = 1; i < 101; i++) {
            doc.add(new Paragraph("Hello " + i));
            doc.add(p);
        }

        doc.close();
    }


    protected class PaginationEventHandler implements IEventHandler {
        protected PdfFormXObject background;

        public PaginationEventHandler(PdfFormXObject background) {
            this.background = background;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocument pdfDoc = ((PdfDocumentEvent) event).getDocument();
            PdfPage currentPage = ((PdfDocumentEvent) event).getPage();

            // Add the background
            new PdfCanvas(currentPage.newContentStreamBefore(), currentPage.getResources(), pdfDoc)
                    .addXObjectAt(background, 0, 0);
        }
    }
}
