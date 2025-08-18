package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.layout.Document;

import java.io.File;

/*
 * MovePopup.java
 * 
 * This class demonstrates how to reposition existing annotations in a PDF document.
 * The code opens a PDF file containing a sticky note annotation with an associated
 * popup window, then modifies the position of both by changing their rectangle 
 * coordinates. The sticky note is moved up and left, while its popup window is 
 * repositioned to maintain a logical relationship with the note. This example shows
 * how to manipulate the physical location of annotations within a document.
 */

public class MovePopup {
    public static final String DEST = "./target/sandbox/annotations/move_popup.pdf";

    public static final String SRC = "./src/main/resources/pdfs/hello_sticky_note.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new MovePopup().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfDictionary page = pdfDoc.getFirstPage().getPdfObject();
        PdfArray annots = page.getAsArray(PdfName.Annots);

        // Get sticky notes annotation and change the rectangle of that annotation
        PdfDictionary sticky = annots.getAsDictionary(0);
        PdfArray stickyRect = sticky.getAsArray(PdfName.Rect);

        PdfArray stickyRectangle = new PdfArray(new float[] {
                stickyRect.getAsNumber(0).floatValue() - 120, stickyRect.getAsNumber(1).floatValue() - 70,
                stickyRect.getAsNumber(2).floatValue(), stickyRect.getAsNumber(3).floatValue() - 30
        });
        sticky.put(PdfName.Rect, stickyRectangle);

        // Get pop-up window annotation and change the rectangle of that annotation
        PdfDictionary popup = annots.getAsDictionary(1);
        PdfArray popupRect = popup.getAsArray(PdfName.Rect);

        PdfArray popupRectangle = new PdfArray(new float[] {
                popupRect.getAsNumber(0).floatValue() - 250, popupRect.getAsNumber(1).floatValue(),
                popupRect.getAsNumber(2).floatValue(), popupRect.getAsNumber(3).floatValue() - 250
        });
        popup.put(PdfName.Rect, popupRectangle);

        doc.close();
    }
}
