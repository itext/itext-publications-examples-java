package com.itextpdf.samples.sandbox.pdfocr.onnx;

import com.itextpdf.pdfocr.OcrPdfCreator;
import com.itextpdf.pdfocr.onnx.OnnxOcrEngine;
import com.itextpdf.pdfocr.onnx.detection.IDetectionPredictor;
import com.itextpdf.pdfocr.onnx.detection.OnnxDetectionPredictor;
import com.itextpdf.pdfocr.onnx.recognition.IRecognitionPredictor;
import com.itextpdf.pdfocr.onnx.recognition.OnnxRecognitionPredictor;

import java.io.File;

/**
 * PdfOcrOnnxPdfAsInputExample.java
 *
 * <p>
 * This example demonstrates how to perform OCR of all images in an input PDF file
 * and generate searchable PDF using provided {@link OnnxOcrEngine}.
 *
 * <p>
 * Required software: iText 9.6.0, pdfOCR-Onnx 5.0.0
 * (pdfocr-onnx-cpu dependency to execute ONNX models on CPU or
 * pdfocr-onnx-abstract and onnxruntime_gpu dependencies to execute ONNX models on GPU).
 */
public class PdfOcrOnnxPdfAsInputExample {
    public static final String DEST = "./target/sandbox/pdfocr/onnx/PdfOcrOnnxPdfAsInputExample/result.pdf";

    private static final String PDF = "./src/main/resources/pdfs/numbers.pdf";

    private static final String MODELS = "./src/main/resources/models/";
    private static final String FAST = MODELS + "rep_fast_tiny-28867779.onnx";
    private static final String CRNNVGG16 = MODELS + "crnn_vgg16_bn-662979cc.onnx";


    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfOcrOnnxPdfAsInputExample().manipulate();
    }

    protected void manipulate() throws Exception {
        IDetectionPredictor detectionPredictor = OnnxDetectionPredictor.fast(FAST);
        IRecognitionPredictor recognitionPredictor = OnnxRecognitionPredictor.crnnVgg16(CRNNVGG16);

        try (OnnxOcrEngine ocrEngine = new OnnxOcrEngine(detectionPredictor, recognitionPredictor)) {
            OcrPdfCreator pdfCreator = new OcrPdfCreator(ocrEngine);
            pdfCreator.makePdfSearchable(new File(PDF), new File(DEST));
        }
    }
}
