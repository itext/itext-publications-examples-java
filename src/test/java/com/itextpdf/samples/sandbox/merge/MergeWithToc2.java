/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21548552/create-index-filetoc-for-merged-pdf-using-itext-library-in-java
 * <p>
 * When concatenating documents, we add a named destination every time
 * a new document is started. After we've finished merging, we add an extra
 * page with the table of contents and links to the named destinations.
 */

package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.draw.DashedLine;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

@Category(SampleTest.class)
public class MergeWithToc2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/merge/merge_with_toc2.pdf";
    public static final String SRC1 = "./src/test/resources/pdfs/united_states.pdf";
    public static final String SRC2 = "./src/test/resources/pdfs/hello.pdf";
    public static final String SRC3 = "./src/test/resources/pdfs/toc.pdf";

    public Map<String, PdfDocument> filesToMerge;

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MergeWithToc().manipulatePdf(DEST);
    }

    public MergeWithToc2() throws IOException {
        filesToMerge = new TreeMap<String, PdfDocument>();
        filesToMerge.put("01 Hello World", new PdfDocument(new PdfReader(SRC1)));
        filesToMerge.put("02 Movies / Countries", new PdfDocument(new PdfReader(SRC2)));
    }

    @Override
    protected void manipulatePdf(String dest) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
        Document doc = new Document(pdfDoc);
        PdfOutline rootOutLine = pdfDoc.getOutlines(false);

        Map<Integer, String> toc = new TreeMap<Integer, String>();
        int n;
        int pageNo = 0;
        for (Map.Entry<String, PdfDocument> entry : filesToMerge.entrySet()) {
            n = entry.getValue().getNumberOfPages();
            toc.put(pageNo + 1, entry.getKey());
            for (int i = 1; i <= n; i++) {
                pageNo++;
                Text text = new Text(String.format("Page %d", pageNo));
                entry.getValue().copyPagesTo(i, i, pdfDoc);
                // Put the destination at the very first page of each merged document
                if (i == 1) {
                    text.setDestination("p" + pageNo);
                    PdfOutline outline = rootOutLine.addOutline("p" + pageNo);
                    outline.addDestination(PdfDestination.makeDestination(new PdfString("p" + pageNo)));
                }
                doc.add(new Paragraph(text).setFixedPosition(pageNo, 549, 810, 40));
            }
        }

        PdfDocument tocDoc = new PdfDocument(new PdfReader(SRC3));
        tocDoc.copyPagesTo(1, 1, pdfDoc);
        tocDoc.close();

        float y = 770;
        for (Map.Entry<Integer, String> entry : toc.entrySet()) {
            Paragraph p = new Paragraph();
            p.addTabStops(new TabStop(500, TabAlignment.LEFT, new DashedLine()));
            p.add(entry.getValue());
            p.add(new Tab());
            p.add(String.valueOf(entry.getKey()));
            p.setAction(PdfAction.createGoTo("p" + entry.getKey()));
            doc.add(p.setFixedPosition(pdfDoc.getNumberOfPages(), 36, y, 595 - 72));
            y -= 20;
        }

        for (PdfDocument srcDoc : filesToMerge.values()) {
            srcDoc.close();
        }
        doc.close();

        PdfDocument resultDoc = new PdfDocument(new PdfWriter(dest));
        PdfDocument srcDoc = new PdfDocument(new PdfReader(new RandomAccessSourceFactory().createSource(baos.toByteArray()), new ReaderProperties()));
        srcDoc.initializeOutlines();
        srcDoc.copyPagesTo(srcDoc.getNumberOfPages(), srcDoc.getNumberOfPages(), resultDoc);
        srcDoc.copyPagesTo(1, srcDoc.getNumberOfPages() - 1, resultDoc);
        srcDoc.close();
        resultDoc.close();
    }
}
