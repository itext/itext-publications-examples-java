package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.property.ListNumberingType;

import java.io.File;
import java.io.IOException;

public class ListInColumn {
    public static final String DEST = "./target/sandbox/objects/list_in_column.pdf";
    public static final String SRC = "./src/main/resources/pdfs/pages.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ListInColumn().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        while (pdfDoc.getNumberOfPages() > 2) {
            pdfDoc.removePage(pdfDoc.getLastPage());
        }
        Document doc = new Document(pdfDoc);
        doc.setRenderer(new ColumnDocumentRenderer(doc, new Rectangle[]{new Rectangle(250, 400, 250, 406)}));

        List list = new List(ListNumberingType.DECIMAL);
        for (int i = 0; i < 10; i++) {
            list.add("This is a list item. It will be repeated a number of times. "
                    + "This is done only for test purposes. "
                    + "I want a list item that is distributed over several lines.");
        }
        doc.add(list);

        doc.close();
    }
}
