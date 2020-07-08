package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.sandbox.merge.densemerger.PdfDenseMerger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfDenseMergeExample {
    public static final String DEST = "./target/sandbox/merge/denseMergeExample.pdf";

    public static final String SRC1 = "./src/main/resources/pdfs/merge1.pdf";
    public static final String SRC2 = "./src/main/resources/pdfs/merge2.pdf";
    public static final String SRC3 = "./src/main/resources/pdfs/merge3.pdf";
    public static final String SRC4 = "./src/main/resources/pdfs/merge4.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfDenseMergeExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        List<PdfDocument> mergeList = initSourceDocuments();

        PdfDenseMerger merger = new PdfDenseMerger(pdfDoc);
        merger
                .setTopMargin(doc.getTopMargin())
                .setBottomMargin(doc.getBottomMargin())
                .setGap(10);
        for (PdfDocument src : mergeList) {
            merger.addPages(src, 1, src.getNumberOfPages());
        }

        pdfDoc.close();
        for (PdfDocument src : mergeList) {
            src.close();
        }
    }

    private static List<PdfDocument> initSourceDocuments() throws IOException {
        List<PdfDocument> list = new ArrayList<>();
        list.add(new PdfDocument(new PdfReader(SRC1)));
        list.add(new PdfDocument(new PdfReader(SRC2)));
        list.add(new PdfDocument(new PdfReader(SRC3)));
        list.add(new PdfDocument(new PdfReader(SRC4)));
        return list;
    }
}
