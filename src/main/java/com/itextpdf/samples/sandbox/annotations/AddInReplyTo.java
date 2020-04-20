package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfTextAnnotation;

import java.io.File;
import java.util.List;

public class AddInReplyTo {
    public static final String DEST = "./target/sandbox/annotations/add_in_reply_to.pdf";

    public static final String SRC = "./src/main/resources/pdfs/hello_sticky_note.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddInReplyTo().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfPage firstPage = pdfDoc.getFirstPage();
        List<PdfAnnotation> annots = firstPage.getAnnotations();

        Rectangle stickyRectangle = annots.get(0).getRectangle().toRectangle();
        PdfAnnotation replySticky = new PdfTextAnnotation(stickyRectangle)

                // This method specifies whether the annotation will initially be displayed open.
                .setOpen(true)
                .setIconName(new PdfName("Comment"))

                // This method sets an annotation to which the current annotation is "in reply".
                // Both annotations shall be on the same page of the document.
                .setInReplyTo(annots.get(0))

                // This method sets the text label that will be displayed in the title bar of the annotation's pop-up window
                // when open and active. This entry shall identify the user who added the annotation.
                .setText(new PdfString("Reply"))

                // This method sets the text that will be displayed for the annotation or the alternate description,
                // if this type of annotation does not display text.
                .setContents("Hello PDF");
        firstPage.addAnnotation(replySticky);

        pdfDoc.close();
    }
}
