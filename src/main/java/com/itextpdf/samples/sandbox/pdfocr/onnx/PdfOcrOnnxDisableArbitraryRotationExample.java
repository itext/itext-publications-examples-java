package com.itextpdf.samples.sandbox.pdfocr.onnx;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.pdfocr.OcrPdfCreator;
import com.itextpdf.pdfocr.OcrPdfCreatorProperties;
import com.itextpdf.pdfocr.OcrProcessContext;
import com.itextpdf.pdfocr.TextInfo;
import com.itextpdf.pdfocr.onnx.OnnxEngineProperties;
import com.itextpdf.pdfocr.onnx.OnnxOcrEngine;
import com.itextpdf.pdfocr.onnx.detection.IDetectionPredictor;
import com.itextpdf.pdfocr.onnx.detection.OnnxDetectionPredictor;
import com.itextpdf.pdfocr.onnx.orientation.IOrientationPredictor;
import com.itextpdf.pdfocr.onnx.recognition.IRecognitionPredictor;
import com.itextpdf.pdfocr.onnx.recognition.OnnxRecognitionPredictor;
import com.itextpdf.pdfocr.onnx.text.TextPositioning;
import com.itextpdf.pdfocr.util.PdfOcrTextBuilder;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * PdfOcrOnnxDisableArbitraryRotationExample.java
 *
 * <p>
 * This example demonstrates how to disable arbitrary rotation for OCR result for the given list of input images.
 * As a result of that particular example, only 0, 90, 180 and 270 degrees text rotation will be used.
 *
 * <p>
 * Required software: iText 9.6.0, pdfOCR-Onnx 5.0.0
 * (pdfocr-onnx-cpu dependency to execute ONNX models on CPU or
 * pdfocr-onnx-abstract and onnxruntime_gpu dependencies to execute ONNX models on GPU).
 */
public class PdfOcrOnnxDisableArbitraryRotationExample {
    public static final String DEST = "./target/sandbox/pdfocr/onnx/PdfOcrOnnxDisableArbitraryRotationExample/result.pdf";

    private static final String BASIC_IMAGE = "./src/main/resources/img/ocrExample.png";

    private static final String MODELS = "./src/main/resources/models/";
    private static final String FAST = MODELS + "rep_fast_tiny-28867779.onnx";
    private static final String CRNNVGG16 = MODELS + "crnn_vgg16_bn-662979cc.onnx";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfOcrOnnxDisableArbitraryRotationExample().manipulate(DEST);
    }

    protected void manipulate(String destination) throws Exception {
        IDetectionPredictor detectionPredictor = OnnxDetectionPredictor.fast(FAST);
        IRecognitionPredictor recognitionPredictor = OnnxRecognitionPredictor.crnnVgg16(CRNNVGG16);

        // OnnxOcrEngine shall be closed after usage to avoid native allocations leak.
        // It will also close all predictors used for its creation.
        try (OnnxOcrEngine ocrEngine = new RotationAgnosticOnnxOcrEngine(detectionPredictor, null, recognitionPredictor,
                new OnnxEngineProperties().setTextPositioning(TextPositioning.BY_WORDS))) {
            OcrPdfCreator pdfCreator = new OcrPdfCreator(ocrEngine, new OcrPdfCreatorProperties()
                    .setTextColor(DeviceCmyk.CYAN)
                    .setTextBBoxColor(DeviceCmyk.CYAN));
            pdfCreator.createPdf(Collections.singletonList(new File(BASIC_IMAGE)), new PdfWriter(destination)).close();
        }
    }

    /**
     * Implementation of the {@link OnnxOcrEngine} supporting only 0, 90, 180 and 270 degrees text rotation.
     */
    public static class RotationAgnosticOnnxOcrEngine extends OnnxOcrEngine {

        /**
         * Create a new OCR engine with the provided predictors.
         *
         * @param detectionPredictor text detector. For an input image it outputs a list of text boxes
         * @param orientationPredictor text orientation predictor. For an input image, which is a tight crop of text,
         * it outputs its orientation in 90 degrees steps. Can be null, in that case all text
         * is assumed to be upright
         * @param recognitionPredictor text recognizer. For an input image, which is a tight crop of text, it outputs
         * the displayed string
         * @param properties set of properties
         */
        public RotationAgnosticOnnxOcrEngine(IDetectionPredictor detectionPredictor,
                                             IOrientationPredictor orientationPredictor,
                                             IRecognitionPredictor recognitionPredictor,
                                             OnnxEngineProperties properties) {
            super(detectionPredictor, orientationPredictor, recognitionPredictor, properties);
        }

        @Override
        public Map<Integer, List<TextInfo>> doImageOcr(File input, OcrProcessContext ocrProcessContext) {
            return PdfOcrTextBuilder.correctRotationAngle(super.doImageOcr(input, ocrProcessContext));
        }

        @Override
        public Map<Integer, List<TextInfo>> doImageOcr(List<File> inputs, OcrProcessContext ocrProcessContext) {
            return PdfOcrTextBuilder.correctRotationAngle(super.doImageOcr(inputs, ocrProcessContext));
        }
    }
}
