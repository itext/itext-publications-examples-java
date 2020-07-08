package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;

public class StampHeader3 {
    public static final String DEST = "./target/sandbox/stamper/stamp_header3.pdf";
    public static final String SRC = "./src/main/resources/pdfs/Wrong.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new StampHeader3().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Paragraph header = new Paragraph("Copy")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)).setFontSize(6);

        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            PdfPage pdfPage = pdfDoc.getPage(i);

            // When "true": in case the page has a rotation, then new content will be automatically rotated in the
            // opposite direction. On the rotated page this would look as if new content ignores page rotation.
            pdfPage.setIgnorePageRotationForContent(true);

            Rectangle pageSize = pdfPage.getPageSize();
            float x, y;
            if (pdfPage.getRotation() % 180 == 0) {
                x = pageSize.getWidth() / 2;
                y = pageSize.getTop() - 20;
            } else {
                x = pageSize.getHeight() / 2;
                y = pageSize.getRight() - 20;
            }

            doc.showTextAligned(header, x, y, i, TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
        }

        doc.close();
    }
}
