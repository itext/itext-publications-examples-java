package com.itextpdf.samples.sandbox.pdfocr.onnx;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtProvider;
import ai.onnxruntime.OrtSession;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.pdfocr.OcrPdfCreator;
import com.itextpdf.pdfocr.onnx.IOrtSessionOptionsCreator;
import com.itextpdf.pdfocr.onnx.OnnxOcrEngine;
import com.itextpdf.pdfocr.onnx.detection.IDetectionPredictor;
import com.itextpdf.pdfocr.onnx.detection.OnnxDetectionPredictor;
import com.itextpdf.pdfocr.onnx.recognition.IRecognitionPredictor;
import com.itextpdf.pdfocr.onnx.recognition.OnnxRecognitionPredictor;

import java.io.File;
import java.util.Collections;

/**
 * CustomOnnxRuntimeSessionOptionsExample.java
 *
 * <p>
 * This example demonstrates how to provide custom {@link ai.onnxruntime.OrtSession.SessionOptions}
 * used to construct {@link OrtSession} which wraps an ONNX model and allows inference calls.
 * This will allow to specify whether to run OCR on GPU or CPU, execution mode, optimization level and other options.
 *
 * <p>
 * In order to run models on GPU, add pdfocr-onnx-abstract and onnxruntime_gpu dependencies.
 * {@link com.itextpdf.pdfocr.onnx.DefaultOrtSessionOptionsCreator} supports GPU mode by default,
 * so no additional changes required unless you want to set up some custom options.
 *
 * <p>
 * Required software: iText 9.6.0, pdfOCR-Onnx 5.0.0
 * (pdfocr-onnx-cpu dependency to execute ONNX models on CPU or 
 * pdfocr-onnx-abstract and onnxruntime_gpu dependencies to execute ONNX models on GPU).
 */
public class CustomOnnxRuntimeSessionOptionsExample {
    public static final String DEST = "./target/sandbox/pdfocr/onnx/CustomOnnxRuntimeSessionOptionsExample/result.pdf";

    private static final String BASIC_IMAGE = "./src/main/resources/img/ocrExample.png";

    private static final String MODELS = "./src/main/resources/models/paddleocr/";
    private static final String DET = MODELS + "PP-OCRv5_mobile_det_infer";
    private static final String REC = MODELS + "PP-OCRv5_mobile_rec_infer";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CustomOnnxRuntimeSessionOptionsExample().manipulate(DEST);
    }

    protected void manipulate(String destination) throws Exception {
        // Create custom IOrtSessionOptionsCreator and use it to create predictors.
        IOrtSessionOptionsCreator sessionOptionsCreator = new CustomOrtSessionOptionsCreator();

        IDetectionPredictor detectionPredictor = OnnxDetectionPredictor.paddleOcr(DET, sessionOptionsCreator);
        IRecognitionPredictor recognitionPredictor = OnnxRecognitionPredictor.paddleOcr(REC, sessionOptionsCreator);

        // OnnxOcrEngine shall be closed after usage to avoid native allocations leak.
        // It will also close all predictors used for its creation.
        try (OnnxOcrEngine ocrEngine = new OnnxOcrEngine(detectionPredictor, recognitionPredictor)) {
            OcrPdfCreator pdfCreator = new OcrPdfCreator(ocrEngine);
            pdfCreator.createPdf(Collections.singletonList(new File(BASIC_IMAGE)), new PdfWriter(destination)).close();
        }
    }

    /**
     * Implementation of {@link IOrtSessionOptionsCreator}.
     *
     * <p>
     * {@code CUDA} execution provider is added if available, otherwise default {@code CPU} execution provider is used.
     */
    public static class CustomOrtSessionOptionsCreator implements IOrtSessionOptionsCreator {
        @Override
        public OrtSession.SessionOptions create() throws OrtException {
            final OrtSession.SessionOptions ortOptions = new OrtSession.SessionOptions();
            try {
                if (OrtEnvironment.getAvailableProviders().contains(OrtProvider.CUDA)) {
                    // Use CUDA provider to run OCR on GPU.
                    ortOptions.addCUDA();
                } else {
                    ortOptions.addCPU(true);
                    ortOptions.setIntraOpNumThreads(-1);
                    ortOptions.setInterOpNumThreads(-1);
                }
                ortOptions.setExecutionMode(OrtSession.SessionOptions.ExecutionMode.SEQUENTIAL);
                ortOptions.setOptimizationLevel(OrtSession.SessionOptions.OptLevel.ALL_OPT);
                return ortOptions;
            } catch (Exception e) {
                ortOptions.close();
                throw e;
            }
        }
    }
}
