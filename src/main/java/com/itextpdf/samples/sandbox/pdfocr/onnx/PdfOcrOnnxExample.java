package com.itextpdf.samples.sandbox.pdfocr.onnx;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.pdfocr.OcrPdfCreator;
import com.itextpdf.pdfocr.onnx.OnnxOcrEngine;
import com.itextpdf.pdfocr.onnx.detection.IDetectionPredictor;
import com.itextpdf.pdfocr.onnx.detection.OnnxDetectionPredictor;
import com.itextpdf.pdfocr.onnx.orientation.IOrientationPredictor;
import com.itextpdf.pdfocr.onnx.orientation.OnnxOrientationPredictor;
import com.itextpdf.pdfocr.onnx.recognition.IRecognitionPredictor;
import com.itextpdf.pdfocr.onnx.recognition.OnnxRecognitionPredictor;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * PdfOcrOnnxExample.java
 *
 * <p>
 * This example demonstrates how to perform OCR using provided {@link OnnxOcrEngine}
 * for the given list of input images and save output to a PDF file using provided path.
 *
 * <p>
 * Required software: iText 9.6.0, pdfOCR-Onnx 5.0.0
 * (pdfocr-onnx-cpu dependency to execute ONNX models on CPU or 
 * pdfocr-onnx-abstract and onnxruntime_gpu dependencies to execute ONNX models on GPU).
 */
public class PdfOcrOnnxExample {
    public static final String DEST = "./target/sandbox/pdfocr/onnx/PdfOcrOnnxExample/result.pdf";

    private static final String BASIC_IMAGE = "./src/main/resources/img/ocrExample.png";
    private static final String ROTATED_IMAGE = "./src/main/resources/img/rotated.png";

    private static final String MODELS = "./src/main/resources/models/";
    private static final String FAST = MODELS + "rep_fast_tiny-28867779.onnx";
    private static final String CRNNVGG16 = MODELS + "crnn_vgg16_bn-662979cc.onnx";
    private static final String MOBILENETV3 = MODELS + "mobilenet_v3_small_crop_orientation-5620cf7e.onnx";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfOcrOnnxExample().manipulate(DEST);
    }

    protected void manipulate(String destination) throws Exception {
        List<File> images = Arrays.asList(new File(BASIC_IMAGE), new File(ROTATED_IMAGE));

        IDetectionPredictor detectionPredictor = OnnxDetectionPredictor.fast(FAST);
        IOrientationPredictor orientationPredictor = OnnxOrientationPredictor.mobileNetV3(MOBILENETV3);
        IRecognitionPredictor recognitionPredictor = OnnxRecognitionPredictor.crnnVgg16(CRNNVGG16);

        // OnnxOcrEngine shall be closed after usage to avoid native allocations leak.
        // It will also close all predictors used for its creation.
        try (OnnxOcrEngine ocrEngine =
                     new OnnxOcrEngine(detectionPredictor, orientationPredictor, recognitionPredictor)) {
            OcrPdfCreator pdfCreator = new OcrPdfCreator(ocrEngine);
            pdfCreator.createPdf(images, new PdfWriter(destination)).close();
        }
    }
}
