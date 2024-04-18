package com.itextpdf.samples.sandbox.pdfhtml.colorblindness;

public class ColorBlindnessTransforms {
    public static final String PROTANOPIA = "Protanopia";
    private static float[][] PROTANOPIA_TRANSFORM = {{0.5667f, 0.43333f, 0}, {0.55833f, 0.44167f, 0}, {0, 0.24167f, 0.75833f}};

    public static final String PROTANOMALY = "Protanomaly";
    private static float[][] PROTANOMALY_TRANSFORM = {{0.81667f, 0.18333f, 0}, {0.33333f, 0.66667f, 0}, {0, 0.125f, 0.875f}};

    public static final String DEUTERANOPIA = "Deuteranopia";
    private static float[][] DEUTERANOPIA_TRANSFORM = {{0.625f, 0.375f, 0}, {0.70f, 0.30f, 0}, {0, 0.30f, 0.70f}};

    public static final String DEUTERANOMALY = "Deuteranomaly";
    private static float[][] DEUTERANOMALY_TRANSFORM = {{0.80f, 0.20f, 0}, {0.25833f, 0.74167f, 0}, {0, 0.14167f, 0.85833f}};

    public static final String TRITANOPIA = "Tritanopia";
    private static float[][] TRITANOPIA_TRANSFORM = {{0.95f, 0.05f, 0}, {0, 0.43333f, 0.56667f}, {0, 0.475f, 0.525f}};

    public static final String TRITANOMALY = "Tritanomaly";
    private static float[][] TRITANOMALY_TRANSFORM = {{0.96667f, 0.0333f, 0}, {0, 0.73333f, 0.26667f}, {0, 0.18333f, 0.81667f}};

    public static final String ACHROMATOPSIA = "Achromatopsia";
    private static float[][] ACHROMATOPSIA_TRANSFORM = {{0.299f, 0.587f, 0.114f}, {0.299f, 0.587f, 0.114f}, {0.299f, 0.587f, 0.114f}};

    public static final String ACHROMATOMALY = "Achromatomaly";
    private static float[][] ACHROMATOMALY_TRANSFORM = {{0.618f, 0.32f, 0.062f}, {0.163f, 0.775f, 0.062f}, {0.299f, 0.587f, 0.114f}};

    public static float[] simulateColorBlindness(String code, float[] originalRgb) {
        switch (code) {
            case PROTANOPIA:
                return simulate(originalRgb, PROTANOPIA_TRANSFORM);
            case PROTANOMALY:
                return simulate(originalRgb, PROTANOMALY_TRANSFORM);
            case DEUTERANOPIA:
                return simulate(originalRgb, DEUTERANOPIA_TRANSFORM);
            case DEUTERANOMALY:
                return simulate(originalRgb, DEUTERANOMALY_TRANSFORM);
            case TRITANOPIA:
                return simulate(originalRgb, TRITANOPIA_TRANSFORM);
            case TRITANOMALY:
                return simulate(originalRgb, TRITANOMALY_TRANSFORM);
            case ACHROMATOPSIA:
                return simulate(originalRgb, ACHROMATOPSIA_TRANSFORM);
            case ACHROMATOMALY:
                return simulate(originalRgb, ACHROMATOMALY_TRANSFORM);
            default:
                return originalRgb;
        }
    }

    private static float[] simulate(float[] originalRgb, float[][] transformValues) {

        // Number of RGB colors
        int nrOfChannels = 3;
        float[] result = new float[nrOfChannels];

        for (int i = 0; i < nrOfChannels; i++) {
            result[i] = 0;
            for (int j = 0; j < nrOfChannels; j++) {
                result[i] += originalRgb[j] * transformValues[i][j];
            }
        }
        return result;
    }
}
