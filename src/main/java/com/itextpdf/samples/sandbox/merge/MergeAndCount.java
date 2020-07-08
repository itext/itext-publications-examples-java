package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PageRange;
import com.itextpdf.kernel.utils.PdfSplitter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class MergeAndCount {
    public static final String DEST = "./target/sandbox/merge/splitDocument1_%s.pdf";

    public static final String RESOURCE = "./src/main/resources/pdfs/Wrong.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new MergeAndCount().manipulatePdf(DEST);
    }

    protected void manipulatePdf(final String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(RESOURCE));

        List<PdfDocument> splitDocuments = new PdfSplitter(pdfDoc) {
            int partNumber = 1;

            @Override
            protected PdfWriter getNextPdfWriter(PageRange documentPageRange) {
                try {
                    return new PdfWriter(String.format(dest, partNumber++));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }.splitBySize(200000);

        for (PdfDocument doc : splitDocuments) {
            doc.close();
        }

        pdfDoc.close();
    }
}
