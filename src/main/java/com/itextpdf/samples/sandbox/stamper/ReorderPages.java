package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReorderPages {
    public static final String DEST = "./target/sandbox/stamper/reorder_pages.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ReorderPages().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(new RandomAccessSourceFactory()
                .createSource(createBaos().toByteArray()), new ReaderProperties()));

        PdfDocument resultDoc = new PdfDocument(new PdfWriter(dest));

        // One should call this method to preserve the outlines of the source pdf file, otherwise they
        // will be absent in the resultant document to which we copy pages. In this particular sample,
        // however, this line doesn't make sense, since the source pdf lacks outlines
        resultDoc.initializeOutlines();

        List<Integer> pages = new ArrayList<>();
        pages.add(1);
        for (int i = 13; i <= 15; i++) {
            pages.add(i);
        }
        for (int i = 2; i <= 12; i++) {
            pages.add(i);
        }
        pages.add(16);
        srcDoc.copyPagesTo(pages, resultDoc);

        resultDoc.close();
        srcDoc.close();
    }

    // Create a temporary document in memory. Then we will reopen it and change the order of its pages
    private static ByteArrayOutputStream createBaos() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
        Document doc = new Document(pdfDoc);

        for (int i = 1; i < 17; i++) {
            doc.add(new Paragraph(String.format("Page %s", i)));
            if (16 != i) {
                doc.add(new AreaBreak());
            }
        }
        doc.close();

        return baos;
    }
}
