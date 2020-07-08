package com.itextpdf.samples.sandbox.parse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ParseCzech {
    public static final String DEST = "./target/txt/czech.txt";

    public static final String SRC = "./src/main/resources/pdfs/czech.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ParseCzech().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));

        // Create a text extraction renderer
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        // Note: if you want to re-use the PdfCanvasProcessor, you must call PdfCanvasProcessor.reset()
        PdfCanvasProcessor parser = new PdfCanvasProcessor(strategy);
        parser.processPageContent(pdfDoc.getFirstPage());

        byte[] content = strategy.getResultantText().getBytes("UTF-8");
        try (FileOutputStream stream = new FileOutputStream(dest)) {
            stream.write(content);
        }

        pdfDoc.close();
    }
}
