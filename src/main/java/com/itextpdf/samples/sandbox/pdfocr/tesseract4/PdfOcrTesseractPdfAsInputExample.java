package com.itextpdf.samples.sandbox.pdfocr.tesseract4;

import com.itextpdf.pdfocr.OcrPdfCreator;
import com.itextpdf.pdfocr.tesseract4.AbstractTesseract4OcrEngine;
import com.itextpdf.pdfocr.tesseract4.Tesseract4ExecutableOcrEngine;
import com.itextpdf.pdfocr.tesseract4.Tesseract4LibOcrEngine;
import com.itextpdf.pdfocr.tesseract4.Tesseract4OcrEngineProperties;
import com.itextpdf.pdfocr.tesseract4.TextPositioning;

import java.io.File;

/**
 * PdfOcrTesseractPdfAsInputExample.java
 *
 * <p>
 * This example demonstrates how to perform OCR of all images in an input PDF file
 * and generate searchable PDF using provided {@link AbstractTesseract4OcrEngine}.
 *
 * <p>
 * Required software: iText 9.3.0, pdfOCR-Tesseract4 4.1.0. Requires Tesseract installation,
 * see <a href="https://tesseract-ocr.github.io/tessdoc/Home.html">Tesseract User Manual</a>,
 * Leptonica library is required on Linux operating systems.
 */
public class PdfOcrTesseractPdfAsInputExample {
    public static final String DEST = "./target/sandbox/pdfocr/tesseract4/PdfOcrTesseractPdfAsInputExample/result.pdf";

    // Directory with trained data for tests
    protected static final String LANG_TESS_DATA_DIRECTORY = "./src/main/resources/tessdata";

    private static final String PDF = "./src/main/resources/pdfs/numbers.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfOcrTesseractPdfAsInputExample().manipulate();
    }

    protected void manipulate() throws Exception {
        AbstractTesseract4OcrEngine ocrEngine = new Tesseract4ExecutableOcrEngine(new Tesseract4OcrEngineProperties()
                .setPathToTessData(getTessDataDirectory())
                .setTextPositioning(TextPositioning.BY_WORDS_AND_LINES));
        OcrPdfCreator pdfCreator = new OcrPdfCreator(ocrEngine);
        pdfCreator.makePdfSearchable(new File(PDF), new File(DEST));
    }

    protected static File getTessDataDirectory() {
        return new File(LANG_TESS_DATA_DIRECTORY);
    }
}
