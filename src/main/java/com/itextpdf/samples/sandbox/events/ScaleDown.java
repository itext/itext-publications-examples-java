package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;

public class ScaleDown {
    public static final String DEST = "./target/sandbox/events/scale_down.pdf";

    public static final String SRC = "./src/main/resources/pdfs/orientations.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ScaleDown().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        float scale = 0.5f;
        ScaleDownEventHandler eventHandler = new ScaleDownEventHandler(scale);
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, eventHandler);

        int numberOfPages = srcDoc.getNumberOfPages();
        for (int p = 1; p <= numberOfPages; p++) {
            eventHandler.setPageDict(srcDoc.getPage(p).getPdfObject());

            // Copy and paste scaled page content as formXObject
            PdfFormXObject page = srcDoc.getPage(p).copyAsFormXObject(pdfDoc);
            PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
            canvas.addXObjectWithTransformationMatrix(page, scale, 0f, 0f, scale, 0f, 0f);
        }

        pdfDoc.close();
        srcDoc.close();
    }


    private static class ScaleDownEventHandler implements IEventHandler {
        protected float scale = 1;
        protected PdfDictionary pageDict;

        public ScaleDownEventHandler(float scale) {
            this.scale = scale;
        }

        public void setPageDict(PdfDictionary pageDict) {
            this.pageDict = pageDict;
        }

        @Override
        public void handleEvent(Event currentEvent) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
            PdfPage page = docEvent.getPage();

            page.put(PdfName.Rotate, pageDict.getAsNumber(PdfName.Rotate));

            // The MediaBox value defines the full size of the page.
            scaleDown(page, pageDict, PdfName.MediaBox, scale);

            // The CropBox value defines the visible size of the page.
            scaleDown(page, pageDict, PdfName.CropBox, scale);
        }

        protected void scaleDown(PdfPage destPage, PdfDictionary pageDictSrc, PdfName box, float scale) {
            PdfArray original = pageDictSrc.getAsArray(box);
            if (original != null) {
                float width = original.getAsNumber(2).floatValue() - original.getAsNumber(0).floatValue();
                float height = original.getAsNumber(3).floatValue() - original.getAsNumber(1).floatValue();

                PdfArray result = new PdfArray();
                result.add(new PdfNumber(0));
                result.add(new PdfNumber(0));
                result.add(new PdfNumber(width * scale));
                result.add(new PdfNumber(height * scale));
                destPage.put(box, result);
            }
        }
    }
}
