package com.itextpdf.samples.sandbox.pdfocr.onnx;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.pdfocr.OcrPdfCreator;
import com.itextpdf.pdfocr.onnx.OnnxOcrEngine;
import com.itextpdf.pdfocr.onnx.detection.IDetectionPredictor;
import com.itextpdf.pdfocr.onnx.detection.OnnxDetectionPredictor;
import com.itextpdf.pdfocr.onnx.recognition.IRecognitionPredictor;
import com.itextpdf.pdfocr.onnx.recognition.OnnxRecognitionPredictor;

import java.io.File;
import java.util.Collections;

/**
 * PdfOcrOnnxPaddleOcrExample.java
 *
 * <p>
 * This example demonstrates how to perform OCR using {@link OnnxOcrEngine} and PaddleOCR ML-models
 * for the given list of input images and save output to a PDF file using provided path.
 *
 * <p>
 * PaddleOCR models converted to ONNX format can be found at
 * <a href="https://huggingface.co/itextresearch">iText Research Hugging Face page</a>.
 *
 * <p>
 * Required software: iText 9.6.0, pdfOCR-Onnx 5.0.0
 * (pdfocr-onnx-cpu dependency to execute ONNX models on CPU or 
 * pdfocr-onnx-abstract and onnxruntime_gpu dependencies to execute ONNX models on GPU).
 */
public class PdfOcrOnnxPaddleOcrExample {
    public static final String DEST = "./target/sandbox/pdfocr/onnx/PdfOcrOnnxPaddleOcrExample/result.pdf";

    private static final String BASIC_IMAGE = "./src/main/resources/img/ocrExample.png";

    private static final String MODELS = "./src/main/resources/models/paddleocr/";
    private static final String DETECTION = MODELS + "PP-OCRv5_mobile_det_infer";
    private static final String RECOGNITION = MODELS + "PP-OCRv5_mobile_rec_infer";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfOcrOnnxPaddleOcrExample().manipulate(DEST);
    }

    protected void manipulate(String destination) throws Exception {
        IDetectionPredictor detectionPredictor =
                OnnxDetectionPredictor.paddleOcr(DETECTION + "/inference.onnx", DETECTION + "/inference.yml");
        IRecognitionPredictor recognitionPredictor =
                OnnxRecognitionPredictor.paddleOcr(RECOGNITION + "/inference.onnx", RECOGNITION + "/inference.yml");

        // OnnxOcrEngine shall be closed after usage to avoid native allocations leak.
        // It will also close all predictors used for its creation.
        try (OnnxOcrEngine ocrEngine = new OnnxOcrEngine(detectionPredictor, recognitionPredictor)) {
            OcrPdfCreator pdfCreator = new OcrPdfCreator(ocrEngine);
            pdfCreator.createPdf(Collections.singletonList(new File(BASIC_IMAGE)), new PdfWriter(destination)).close();
        }
    }
}
