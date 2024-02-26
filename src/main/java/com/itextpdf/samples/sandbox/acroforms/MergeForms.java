package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;

public class MergeForms {
    public static final String DEST = "./target/sandbox/acroforms/merge_forms.pdf";

    public static final String SRC1 = "./src/main/resources/pdfs/subscribe.pdf";
    public static final String SRC2 = "./src/main/resources/pdfs/state.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new MergeForms().manipulatePdf(DEST);
    }

    public String getFile1() {
        return SRC1;
    }

    public String getFile2() {
        return SRC2;
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfReader[] readers = {
                new PdfReader(getFile1()),
                new PdfReader(getFile2())
        };

        // Method copies the content of all read files to the created resultant pdf
        mergePdfForms(dest, readers);
    }

    private void mergePdfForms(String dest, PdfReader[] readers) throws FileNotFoundException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        // This method initializes an outline tree of the document and sets outline mode to true.
        pdfDoc.initializeOutlines();

        // Copier contains the logic to copy only acroform fields to a new page.
        // PdfPageFormCopier uses some caching logic which can potentially improve performance
        // in case of the reusing of the same instance.
        PdfPageFormCopier formCopier = new PdfPageFormCopier();

        for (PdfReader reader : readers) {
            PdfDocument readerDoc = new PdfDocument(reader);
            readerDoc.copyPagesTo(1, readerDoc.getNumberOfPages(), pdfDoc, formCopier);
            readerDoc.close();
        }

        pdfDoc.close();
    }
}
