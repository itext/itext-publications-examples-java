package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;

public class AddExtraMargin {
    public static final String DEST = "./target/sandbox/stamper/add_extra_margin.pdf";
    public static final String SRC = "./src/main/resources/pdfs/primes.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddExtraMargin().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        // Loop over every page
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            PdfDictionary pageDict = pdfDoc.getPage(i).getPdfObject();
            PdfArray mediaBox = pageDict.getAsArray(PdfName.MediaBox);
            float llx = mediaBox.getAsNumber(0).floatValue();
            float lly = mediaBox.getAsNumber(1).floatValue();
            float ury = mediaBox.getAsNumber(3).floatValue();
            mediaBox.set(0, new PdfNumber(llx - 36));
            PdfCanvas over = new PdfCanvas(pdfDoc.getPage(i));
            over.saveState();
            over.setFillColor(new DeviceGray(0.5f));
            over.rectangle(llx - 36, lly, 36, ury - llx);
            over.fill();
            over.restoreState();
        }

        pdfDoc.close();
    }
}
