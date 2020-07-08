package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class UnequalPages {
    public static final String DEST = "./target/sandbox/objects/unequal_pages.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new UnequalPages().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PageSize pageSizeOne = new PageSize(70, 140);
        PageSize pageSizeTwo = new PageSize(700, 400);
        Paragraph p = new Paragraph("Hi");

        pdfDoc.setDefaultPageSize(pageSizeOne);
        doc.setMargins(2, 2, 2, 2);
        doc.add(p);

        pdfDoc.setDefaultPageSize(pageSizeTwo);
        doc.setMargins(20, 20, 20, 20);
        doc.add(new AreaBreak());
        doc.add(p);

        doc.close();
    }
}
