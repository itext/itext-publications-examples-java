package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class ChapterAndTitle {
    public static final String DEST = "./target/sandbox/objects/chapter_and_title.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChapterAndTitle().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        String titleDestination = "title";

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLDOBLIQUE);
        Paragraph title = new Paragraph("This is the title with the font HELVETICA 16")
                .setFont(font).setFontSize(16);
        title.setDestination(titleDestination);
        doc.add(title);

        // It is an alternative for iText5 Chapter class, because
        // iText5 Chapter class also creates bookmarks automatically.
        PdfOutline root = pdfDoc.getOutlines(false);
        root.addOutline("This is the title")
                .addDestination(PdfDestination.makeDestination(new PdfString(titleDestination)));

        Paragraph p = new Paragraph("This is the paragraph with the default font");
        doc.add(p);

        doc.close();
    }
}
