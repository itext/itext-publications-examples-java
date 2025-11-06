package com.itextpdf.samples.sandbox.pdfocr.tesseract4;

import com.itextpdf.pdfocr.tesseract4.AbstractTesseract4OcrEngine;
import com.itextpdf.pdfocr.tesseract4.Tesseract4ExecutableOcrEngine;
import com.itextpdf.pdfocr.tesseract4.Tesseract4OcrEngineProperties;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * PdfOcrTesseractTxtFileExample.java
 *
 * <p>
 * This example demonstrates how to perform OCR using provided {@link AbstractTesseract4OcrEngine}
 * for the given list of input images and save output to a text file using provided path.
 *
 * <p>
 * Software: iText 9.0.0, pdfOCR-Tesseract4 4.0.0. Requires Tesseract installation,
 * see <a href="https://tesseract-ocr.github.io/tessdoc/Home.html">Tesseract User Manual</a>,
 * Leptonica library is required on Linux operating systems.
 */
public class PdfOcrTesseractTxtFileExample {
    public static final String DEST = "./target/sandbox/pdfocr/tesseract4/PdfOcrTesseractTxtFileExample/ocr_result.txt";

    // Directory with trained data for tests
    protected static final String LANG_TESS_DATA_DIRECTORY = "./src/main/resources/tessdata";

    private static final String BASIC_IMAGE = "./src/main/resources/img/ocrExample.png";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfOcrTesseractTxtFileExample().manipulate();
    }

    protected void manipulate() throws Exception {
        List<File> images = Arrays.asList(new File(BASIC_IMAGE));

        AbstractTesseract4OcrEngine ocrEngine = new Tesseract4ExecutableOcrEngine(new Tesseract4OcrEngineProperties()
                .setPathToTessData(getTessDataDirectory()));
        ocrEngine.createTxtFile(images, new File(DEST));
    }

    protected static File getTessDataDirectory() {
        return new File(LANG_TESS_DATA_DIRECTORY);
    }
}
