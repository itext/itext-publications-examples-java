package com.itextpdf.samples.sandbox.pdfocr.onnx;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.pdfocr.OcrPdfCreator;
import com.itextpdf.pdfocr.OcrPdfCreatorProperties;
import com.itextpdf.pdfocr.onnx.OnnxTrEngineProperties;
import com.itextpdf.pdfocr.onnx.OnnxTrOcrEngine;
import com.itextpdf.pdfocr.onnx.detection.IDetectionPredictor;
import com.itextpdf.pdfocr.onnx.detection.OnnxDetectionPredictor;
import com.itextpdf.pdfocr.onnx.recognition.IRecognitionPredictor;
import com.itextpdf.pdfocr.onnx.recognition.OnnxRecognitionPredictor;
import com.itextpdf.pdfocr.onnx.text.TextPositioning;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * PdfOcrOnnxTextPositioningExample.java
 *
 * <p>
 * This example demonstrates how to define the way text is retrieved from ocr engine output
 * specifying {@link TextPositioning} in {@link OnnxTrEngineProperties} in order to perform OCR
 * using provided {@link OnnxTrOcrEngine} for the given images and save output to a PDF file.
 *
 * <p>
 * Also, this example demonstrates how to show the recognition result using {@link OcrPdfCreatorProperties}
 * to set color for recognized text.
 *
 * <p>
 * Required software: iText 9.3.0, pdfOCR-Onnx 5.0.0.
 */
public class PdfOcrOnnxTextPositioningExample {
    public static final String DEST = "./target/sandbox/pdfocr/onnx/PdfOcrOnnxTextPositioningExample/result.pdf";

    private static final String IMAGE = "./src/main/resources/img/scanned.png";

    private static final String MODELS = "./src/main/resources/models/";
    private static final String FAST = MODELS + "rep_fast_tiny-28867779.onnx";
    private static final String CRNNVGG16 = MODELS + "crnn_vgg16_bn-662979cc.onnx";


    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfOcrOnnxTextPositioningExample().manipulate();
    }

    protected void manipulate() throws Exception {
        List<File> images = Collections.singletonList(new File(IMAGE));

        IDetectionPredictor detectionPredictor = OnnxDetectionPredictor.fast(FAST);
        IRecognitionPredictor recognitionPredictor = OnnxRecognitionPredictor.crnnVgg16(CRNNVGG16);

        // It is possible to specify text positioning mode through OnnxTrEngineProperties.
        // Default value is BY_WORDS_AND_LINES.
        try (OnnxTrOcrEngine ocrEngine = new OnnxTrOcrEngine(detectionPredictor, null, recognitionPredictor,
                new OnnxTrEngineProperties().setTextPositioning(TextPositioning.BY_WORDS))) {

            // Set green text color to show the recognition result. Skip that step for real usages.
            OcrPdfCreatorProperties ocrPdfCreatorProperties = new OcrPdfCreatorProperties()
                    .setTextLayerName("OnnxTR by lines example")
                    .setTextColor(ColorConstants.GREEN);

            OcrPdfCreator pdfCreator = new OcrPdfCreator(ocrEngine, ocrPdfCreatorProperties);
            pdfCreator.createPdf(images, new PdfWriter(DEST)).close();
        }
    }
}
