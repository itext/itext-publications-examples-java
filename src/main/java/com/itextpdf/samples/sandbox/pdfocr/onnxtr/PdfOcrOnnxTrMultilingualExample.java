package com.itextpdf.samples.sandbox.pdfocr.onnxtr;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.pdfocr.OcrPdfCreator;
import com.itextpdf.pdfocr.OcrPdfCreatorProperties;
import com.itextpdf.pdfocr.onnxtr.OnnxTrOcrEngine;
import com.itextpdf.pdfocr.onnxtr.detection.IDetectionPredictor;
import com.itextpdf.pdfocr.onnxtr.detection.OnnxDetectionPredictor;
import com.itextpdf.pdfocr.onnxtr.orientation.IOrientationPredictor;
import com.itextpdf.pdfocr.onnxtr.orientation.OnnxOrientationPredictor;
import com.itextpdf.pdfocr.onnxtr.recognition.IRecognitionPredictor;
import com.itextpdf.pdfocr.onnxtr.recognition.OnnxRecognitionPredictor;
import com.itextpdf.pdfocr.onnxtr.recognition.Vocabulary;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * PdfOcrOnnxTrMultilingualExample.java
 *
 * <p>
 * This example demonstrates how to perform OCR using {@code onnxtr-parseq-multilingual-v1.onnx}
 * recognition model for the given list of input images with different latin languages.
 *
 * <p>
 * Also, this example demonstrates how to show the recognition result using {@link OcrPdfCreatorProperties}
 * to set color for recognized text.
 *
 * <p>
 * Required software: iText 9.3.0, pdfOCR-OnnxTR 4.1.0.
 */
public class PdfOcrOnnxTrMultilingualExample {
    public static final String DEST = "./target/sandbox/pdfocr/onnxtr/PdfOcrOnnxTrMultilingualExample/result.pdf";

    private static final String FRENCH = "./src/main/resources/img/french.png";
    private static final String GERMAN = "./src/main/resources/img/german.jpg";
    private static final String SPANISH = "./src/main/resources/img/spanish.jpg";

    private static final String MODELS = "./src/main/resources/models/";
    private static final String FAST = MODELS + "rep_fast_tiny-28867779.onnx";
    private static final String MOBILENETV3 = MODELS + "mobilenet_v3_small_crop_orientation-5620cf7e.onnx";

    private static final String MULTILANG = MODELS + "onnxtr-parseq-multilingual-v1.onnx";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfOcrOnnxTrMultilingualExample().manipulate();
    }

    protected void manipulate() throws Exception {
        List<File> images = Arrays.asList(new File(FRENCH), new File(GERMAN), new File(SPANISH));

        IDetectionPredictor detectionPredictor = OnnxDetectionPredictor.fast(FAST);
        IOrientationPredictor orientationPredictor = OnnxOrientationPredictor.mobileNetV3(MOBILENETV3);

        // This PARSeq model supports latin languages/symbols collected into Vocabulary.LATIN_EXTENDED.
        IRecognitionPredictor recognitionPredictor =
                OnnxRecognitionPredictor.parSeq(MULTILANG, Vocabulary.LATIN_EXTENDED, 0);

        try (OnnxTrOcrEngine ocrEngine =
                     new OnnxTrOcrEngine(detectionPredictor, orientationPredictor, recognitionPredictor)) {

            // Set green text color to show the recognition result. Skip that step for real usages.
            OcrPdfCreatorProperties ocrPdfCreatorProperties = new OcrPdfCreatorProperties()
                    .setTextLayerName("OnnxTR multilingual example")
                    .setTextColor(ColorConstants.GREEN);

            OcrPdfCreator pdfCreator = new OcrPdfCreator(ocrEngine, ocrPdfCreatorProperties);
            try (PdfWriter writer = new PdfWriter(DEST)) {
                pdfCreator.createPdf(images, writer).close();
            }
        }
    }
}
