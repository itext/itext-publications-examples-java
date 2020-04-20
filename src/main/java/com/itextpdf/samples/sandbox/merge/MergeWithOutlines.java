/**
 * <p>
 * When concatenating documents, we add a named destination every time
 * a new document is started. After we've finished merging, we add an extra
 * page with the table of contents and links to the named destinations.
 */
package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.kernel.utils.PdfMerger;

import java.io.File;
import java.io.IOException;

public class MergeWithOutlines {
    public static final String DEST = "./target/sandbox/merge/merge_with_outlines.pdf";

    public static final String SRC1 = "./src/main/resources/pdfs/hello.pdf";
    public static final String SRC2 = "./src/main/resources/pdfs/links1.pdf";
    public static final String SRC3 = "./src/main/resources/pdfs/links2.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new MergeWithOutlines().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfDocument srcDoc1 = new PdfDocument(new PdfReader(SRC1));
        PdfDocument srcDoc2 = new PdfDocument(new PdfReader(SRC2));
        PdfDocument srcDoc3 = new PdfDocument(new PdfReader(SRC3));

        int numberOfPages1 = srcDoc1.getNumberOfPages();
        int numberOfPages2 = srcDoc2.getNumberOfPages();
        int numberOfPages3 = srcDoc3.getNumberOfPages();

        PdfMerger merger = new PdfMerger(pdfDoc);
        merger.setCloseSourceDocuments(true)
                .merge(srcDoc1, 1, numberOfPages1)
                .merge(srcDoc2, 1, numberOfPages2)
                .merge(srcDoc3, 1, numberOfPages3);

        PdfOutline rootOutline = pdfDoc.getOutlines(false);

        int page = 1;
        PdfOutline helloWorld = rootOutline.addOutline("Hello World");
        helloWorld.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(page)));
        page += numberOfPages1;

        PdfOutline link1 = helloWorld.addOutline("link1");
        link1.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(page)));
        page += numberOfPages2;

        PdfOutline link2 = rootOutline.addOutline("Link 2");
        link2.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(page)));

        pdfDoc.close();
    }
}
