package com.itextpdf.samples.sandbox.pdfocr.onnx;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.pdfocr.OcrPdfCreator;
import com.itextpdf.pdfocr.onnx.OnnxOcrEngine;
import com.itextpdf.pdfocr.onnx.detection.IDetectionPredictor;
import com.itextpdf.pdfocr.onnx.detection.OnnxDetectionPredictor;
import com.itextpdf.pdfocr.onnx.recognition.EasyOcrMapper;
import com.itextpdf.pdfocr.onnx.recognition.IRecognitionPredictor;
import com.itextpdf.pdfocr.onnx.recognition.OnnxRecognitionPredictor;

import java.io.File;
import java.util.Collections;

/**
 * PdfOcrOnnxEasyOcrExample.java
 *
 * <p>
 * This example demonstrates how to perform OCR using {@link OnnxOcrEngine} and EasyOCR ML-models
 * for the given list of input images and save output to a PDF file using provided path.
 *
 * <p>
 * EasyOCR models converted to ONNX format can be found at
 * <a href="https://huggingface.co/itextresearch">iText Research Hugging Face page</a>.
 *
 * <p>
 * Required software: iText 9.6.0, pdfOCR-Onnx 5.0.0
 * (pdfocr-onnx-cpu dependency to execute ONNX models on CPU or 
 * pdfocr-onnx-abstract and onnxruntime_gpu dependencies to execute ONNX models on GPU).
 */
public class PdfOcrOnnxEasyOcrExample {
    public static final String DEST = "./target/sandbox/pdfocr/onnx/PdfOcrOnnxEasyOcrExample/result.pdf";

    private static final String BASIC_IMAGE = "./src/main/resources/img/ocrExample.png";

    private static final String MODELS = "./src/main/resources/models/easyocr/";
    private static final String DETECTION = MODELS + "craft_mlt_25k.onnx";
    private static final String RECOGNITION = MODELS + "latin_g2.onnx";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfOcrOnnxEasyOcrExample().manipulate(DEST);
    }

    protected void manipulate(String destination) throws Exception {
        IDetectionPredictor detectionPredictor = OnnxDetectionPredictor.easyOcr(DETECTION);
        IRecognitionPredictor recognitionPredictor =
                OnnxRecognitionPredictor.easyOcr(RECOGNITION, EasyOcrMapper.LATIN_G2);

        // OnnxOcrEngine shall be closed after usage to avoid native allocations leak.
        // It will also close all predictors used for its creation.
        try (OnnxOcrEngine ocrEngine = new OnnxOcrEngine(detectionPredictor, recognitionPredictor)) {
            OcrPdfCreator pdfCreator = new OcrPdfCreator(ocrEngine);
            pdfCreator.createPdf(Collections.singletonList(new File(BASIC_IMAGE)), new PdfWriter(destination)).close();
        }
    }
}
