package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;

public class AddLinkAnnotation4 {
    public static final String DEST = "./target/sandbox/annotations/add_link_annotation4.pdf";

    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddLinkAnnotation4().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        Link link = new Link("The Best iText Questions on StackOverflow",
                PdfAction.createURI("https://pages.itextpdf.com/ebook-stackoverflow-questions.html"));
        link.setFont(bold);
        Paragraph p = new Paragraph("Download ")
                .add(link)
                .add(" and discover more than 200 questions and answers.");

        // Rotate the paragraph on 90 degrees and add it to the document.
        doc.showTextAligned(p, 30, 100, 1, TextAlignment.LEFT,
                VerticalAlignment.TOP, (float) Math.PI / 2);

        doc.close();
    }
}
