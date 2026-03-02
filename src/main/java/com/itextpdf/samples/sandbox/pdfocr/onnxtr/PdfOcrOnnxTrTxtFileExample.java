package com.itextpdf.samples.sandbox.pdfocr.onnxtr;

import com.itextpdf.pdfocr.onnx.OnnxTrOcrEngine;
import com.itextpdf.pdfocr.onnx.detection.IDetectionPredictor;
import com.itextpdf.pdfocr.onnx.detection.OnnxDetectionPredictor;
import com.itextpdf.pdfocr.onnx.recognition.IRecognitionPredictor;
import com.itextpdf.pdfocr.onnx.recognition.OnnxRecognitionPredictor;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * PdfOcrOnnxTrTxtFileExample.java
 *
 * <p>
 * This example demonstrates how to perform OCR using provided {@link OnnxTrOcrEngine}
 * for the given list of input images and save output to a text file using provided path.
 *
 * <p>
 * Required software: iText 9.3.0, pdfOCR-OnnxTR 4.1.0.
 */
public class PdfOcrOnnxTrTxtFileExample {
    public static final String DEST = "./target/sandbox/pdfocr/onnxtr/PdfOcrOnnxTrTxtFileExample/ocr_result.txt";

    private static final String BASIC_IMAGE = "./src/main/resources/img/ocrExample.png";

    private static final String MODELS = "./src/main/resources/models/";
    private static final String FAST = MODELS + "rep_fast_tiny-28867779.onnx";
    private static final String CRNNVGG16 = MODELS + "crnn_vgg16_bn-662979cc.onnx";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfOcrOnnxTrTxtFileExample().manipulate();
    }

    protected void manipulate() throws Exception {
        List<File> images = Arrays.asList(new File(BASIC_IMAGE));

        IDetectionPredictor detectionPredictor = OnnxDetectionPredictor.fast(FAST);
        IRecognitionPredictor recognitionPredictor = OnnxRecognitionPredictor.crnnVgg16(CRNNVGG16);

        // OnnxTrOcrEngine shall be closed after usage to avoid native allocations leak.
        // It will also close all predictors used for its creation.
        try (OnnxTrOcrEngine ocrEngine = new OnnxTrOcrEngine(detectionPredictor, recognitionPredictor)) {
            ocrEngine.createTxtFile(images, new File(DEST));
        }
    }
}
