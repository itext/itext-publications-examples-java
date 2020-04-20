package com.itextpdf.samples.sandbox.typography.arabic;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class ArabicBookmark {

    public static final String DEST = "./target/sandbox/typography/ArabicBookmark.pdf";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ArabicBookmark().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));

        // Get the complete outline tree of the whole document
        PdfOutline root = pdfDocument.getOutlines(true);

        // المرجعية
        String bookmarkParent = "\u0627\u0644\u0645\u0631\u062C\u0639\u064A\u0629";

        // دةً انّ
        String bookmarkChild = "\u062F\u0629\u064B\u0020\u0627\u0646\u0651";

        // Add a page to the document
        pdfDocument.addNewPage();

        // Add some PdfOutline children to the root outline
        PdfOutline bookmarkTree = root.addOutline(bookmarkParent);
        bookmarkTree
                .addOutline(bookmarkChild)
                .addOutline(bookmarkChild)
                .addOutline(bookmarkChild);

        pdfDocument.close();
    }
}
