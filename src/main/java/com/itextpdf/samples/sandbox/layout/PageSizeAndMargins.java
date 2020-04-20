package com.itextpdf.samples.sandbox.layout;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PageSizeAndMargins {

    public static final String DEST = "./target/sandbox/layout/pageSizeAndMargins.pdf";


    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PageSizeAndMargins().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(200, 200));

        String marginsText = doc.getTopMargin() + ", " + doc.getRightMargin() + ", "
                + doc.getBottomMargin() + ", " + doc.getLeftMargin();
        Paragraph p = new Paragraph("Margins: [" + marginsText + "]\nPage size: 150*150");
        doc.add(p);

        // The margins will be applied on the pages,
        // which have been added after the call of this method.
        doc.setMargins(10, 10, 10, 10);

        doc.add(new AreaBreak());
        p = new Paragraph("Margins: [10.0, 10.0, 10.0, 10.0]\nPage size: 150*150");
        doc.add(p);

        // Add a new A4 page.
        doc.add(new AreaBreak(PageSize.A4));
        p = new Paragraph("Margins: [10.0, 10.0, 10.0, 10.0]\nPage size: A4");
        doc.add(p);

        // Add a new page.
        // The page size will be the same as it is set in the Document.
        doc.add(new AreaBreak());
        p = new Paragraph("Margins: [10.0, 10.0, 10.0, 10.0]\nPage size: 150*150");
        doc.add(p);

        doc.close();
    }


}
