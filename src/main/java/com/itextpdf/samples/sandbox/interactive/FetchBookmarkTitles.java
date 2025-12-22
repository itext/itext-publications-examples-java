package com.itextpdf.samples.sandbox.interactive;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfReader;

import java.io.File;
import java.io.Writer;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/*
 * FetchBookmarkTitles.java
 *
 * Example showing how to extract bookmark titles from a PDF document.
 * Demonstrates recursive traversal of outline tree and output to file.
 */

public class FetchBookmarkTitles {
    public static final String DEST = "./target/txt/bookmarks.txt";

    public static final String SRC = "./src/main/resources/pdfs/bookmarks.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FetchBookmarkTitles().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));

        // This method returns a complete outline tree of the whole document.
        // If the flag is false, the method gets cached outline tree (if it was cached
        // via calling getOutlines method before).
        PdfOutline outlines = pdfDoc.getOutlines(false);
        List<PdfOutline> bookmarks = outlines.getAllChildren();

        pdfDoc.close();

        List<String> titles = new ArrayList<>();
        for (PdfOutline bookmark : bookmarks) {
            addTitle(bookmark, titles);
        }

        // See title's names in the console
        for (String title : titles) {
            System.out.println(title);
        }

        createResultTxt(dest, titles);
    }

    /*
     * This recursive method calls itself if an examined bookmark entry has kids.
     * The method writes bookmark title to the passed list
     */
    private void addTitle(PdfOutline outline, List<String> result) {
        String bookmarkTitle = outline.getTitle();
        result.add(bookmarkTitle);

        List<PdfOutline> kids = outline.getAllChildren();
        if (kids != null) {
            for (PdfOutline kid : kids) {
                addTitle(kid, result);
            }
        }
    }

    private void createResultTxt(String dest, List<String> titles) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest)))) {
            for (int i = 0; i < titles.size(); i++) {
                writer.write("Title " + i + ": " + titles.get(i) + "\n");
            }
        }
    }
}
