/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/24678640/itext-pdfdocument-page-size-inaccurate
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;


@Category(SampleTest.class)
public class StampHeader2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/stamp_header2.pdf";
    public static final String SRC = "./src/test/resources/pdfs/Wrong.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new StampHeader2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        Paragraph header = new Paragraph("Copy")
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                .setFontSize(14);
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            float x = pdfDoc.getPage(i).getPageSize().getWidth() / 2;
            float y = pdfDoc.getPage(i).getPageSize().getTop() - 20;

            doc.showTextAligned(header, x, y, i,
                    TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
        }
        doc.close();
    }
}
