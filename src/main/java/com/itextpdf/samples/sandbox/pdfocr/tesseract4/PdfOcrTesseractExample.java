package com.itextpdf.samples.sandbox.pdfocr.tesseract4;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.pdfocr.OcrPdfCreator;
import com.itextpdf.pdfocr.tesseract4.Tesseract4ExecutableOcrEngine;
import com.itextpdf.pdfocr.tesseract4.Tesseract4LibOcrEngine;
import com.itextpdf.pdfocr.tesseract4.Tesseract4OcrEngineProperties;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * PdfOcrTesseractExample.java
 *
 * <p>
 * This example demonstrates how to perform OCR using provided {@link Tesseract4ExecutableOcrEngine} or
 * {@link Tesseract4LibOcrEngine} for the given list of input images and save output to a PDF file using provided path.
 *
 * <p>
 * Software: iText 9.0.0, pdfOCR-Tesseract4 4.0.0. Requires {@code tess4j} dependency or Tesseract installation,
 * see <a href="https://tesseract-ocr.github.io/tessdoc/Home.html">Tesseract User Manual</a>,
 * Leptonica library is required on Linux operating systems.
 */
public class PdfOcrTesseractExample {
    public static final String DEST = "./target/sandbox/pdfocr/tesseract4/PdfOcrTesseractExample/result.pdf";

    // Directory with trained data for tests
    protected static final String LANG_TESS_DATA_DIRECTORY = "./src/main/resources/tessdata";

    private static final String LIB_DEST = "./target/sandbox/pdfocr/tesseract4/PdfOcrTesseractExample/libResult.pdf";
    private static final String EXE_DEST = "./target/sandbox/pdfocr/tesseract4/PdfOcrTesseractExample/exeResult.pdf";

    private static final String BASIC_IMAGE = "./src/main/resources/img/ocrExample.png";
    private static final String ROTATED_IMAGE = "./src/main/resources/img/rotated.png";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfOcrTesseractExample().manipulate();

        // Merge both PDFs to compare the result.
        createFinalPDF();
    }

    protected void manipulate() throws Exception {
        List<File> images = Arrays.asList(new File(BASIC_IMAGE), new File(ROTATED_IMAGE));

        // Create PDF with tess4j library.
        Tesseract4LibOcrEngine libOcrEngine = new Tesseract4LibOcrEngine(new Tesseract4OcrEngineProperties()
                .setPathToTessData(getTessDataDirectory()));
        OcrPdfCreator pdfCreator = new OcrPdfCreator(libOcrEngine);
        pdfCreator.createPdf(images, new PdfWriter(LIB_DEST)).close();

        // Create PDF with Tesseract executable.
        Tesseract4ExecutableOcrEngine exeOcrEngine = new Tesseract4ExecutableOcrEngine(getTesseractExecutableCommand(),
                new Tesseract4OcrEngineProperties().setPathToTessData(getTessDataDirectory()));
        pdfCreator = new OcrPdfCreator(exeOcrEngine);
        pdfCreator.createPdf(images, new PdfWriter(EXE_DEST)).close();
    }

    protected static String getTesseractExecutableCommand() {
        String tesseractDir = System.getProperty("tesseractDir");
        String os = System.getProperty("os.name") == null ? System.getProperty("OS") : System.getProperty("os.name");
        return os.toLowerCase().contains("win") && tesseractDir != null && !tesseractDir.isEmpty() ?
                tesseractDir + "\\tesseract.exe" : "tesseract";
    }

    protected static File getTessDataDirectory() {
        return new File(LANG_TESS_DATA_DIRECTORY);
    }

    private static void createFinalPDF() throws IOException {
        try (PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
             PdfDocument lib = new PdfDocument(new PdfReader(LIB_DEST));
             PdfDocument exe = new PdfDocument(new PdfReader(EXE_DEST))) {

            PdfMerger merger = new PdfMerger(pdfDoc);
            merger.merge(lib, 1, lib.getNumberOfPages());
            merger.merge(exe, 1, exe.getNumberOfPages());
        }
    }
}
