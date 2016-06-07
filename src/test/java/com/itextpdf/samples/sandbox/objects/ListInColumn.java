/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/29277611/itextsharp-continuing-ordered-list-on-second-page-with-a-number-other-than-1
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class ListInColumn extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/list_in_column.pdf";
    public static final String SRC = "./src/test/resources/pdfs/pages.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ListInColumn().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
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
