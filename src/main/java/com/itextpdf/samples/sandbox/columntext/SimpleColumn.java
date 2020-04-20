package com.itextpdf.samples.sandbox.columntext;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;

public class SimpleColumn {
    public static final String DEST = "./target/sandbox/columntext/simple_column.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SimpleColumn().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(100, 120));

        Paragraph paragraph = new Paragraph("REALLLLLLLLLLY LONGGGGGGGGGG text").setFontSize(4.5f);

        paragraph.setWidth(61);
        doc.showTextAligned(paragraph, 9,85, TextAlignment.LEFT);

        doc.close();
    }
}
