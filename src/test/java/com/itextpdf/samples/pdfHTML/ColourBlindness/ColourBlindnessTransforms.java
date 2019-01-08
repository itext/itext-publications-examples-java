/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.pdfHTML.ColourBlindness;

public class ColourBlindnessTransforms {
/*
        Normal:{ R:[100, 0, 0], G:[0, 100, 0], B:[0, 100, 0]},
        Protanopia:{ R:[56.667, 43.333, 0], G:[55.833, 44.167, 0], B:[0, 24.167, 75.833]},
        Protanomaly:{ R:[81.667, 18.333, 0], G:[33.333, 66.667, 0], B:[0, 12.5, 87.5]},
        Deuteranopia:{ R:[62.5, 37.5, 0], G:[70, 30, 0], B:[0, 30, 70]},
        Deuteranomaly:{ R:[80, 20, 0], G:[25.833, 74.167, 0], B:[0, 14.167, 85.833]},
        Tritanopia:{ R:[95, 5, 0], G:[0, 43.333, 56.667], B:[0, 47.5, 52.5]},
        Tritanomaly:{ R:[96.667, 3.333, 0], G:[0, 73.333, 26.667], B:[0, 18.333, 81.667]},
        Achromatopsia:{ R:[29.9, 58.7, 11.4], G:[29.9, 58.7, 11.4], B:[29.9, 58.7, 11.4]},
        Achromatomaly:{ R:[61.8, 32, 6.2], G:[16.3, 77.5, 6.2], B:[16.3, 32.0, 51.6]}
*/
    public static final String PROTANOPIA = "Protanopia";
    private static float[][] PROTANOPIA_TRANSFORM = {{0.5667f,0.43333f,0},{0.55833f,0.44167f,0},{0,0.24167f,0.75833f}};

    public static final String PROTANOMALY = "Protanomaly";
    private static float[][] PROTANOMALY_TRANSFORM = {{0.81667f,0.18333f,0},{0.33333f,0.66667f,0},{0,0.125f,0.875f}};

    public static final String DEUTERANOPIA = "Deuteranopia";
    private static float[][] DEUTERANOPIA_TRANSFORM = {{0.625f,0.375f,0},{0.70f,0.30f,0},{0,0.30f,0.70f}};

    public static final String DEUTERANOMALY = "Deuteranomaly";
    private static float[][] DEUTERANOMALY_TRANSFORM = {{0.80f,0.20f,0},{0.25833f,0.74167f,0},{0,0.14167f,0.85833f}};

    public static final String TRITANOPIA = "Tritanopia";
    private static float[][] TRITANOPIA_TRANSFORM = {{0.95f,0.05f,0},{0,0.43333f,0.56667f},{0,0.475f,0.525f}};

    public static final String TRITANOMALY = "Tritanomaly";
    private static float[][] TRITANOMALY_TRANSFORM = {{0.96667f,0.0333f,0},{0,0.73333f,0.26667f},{0,0.18333f,0.81667f}};

    public static final String ACHROMATOPSIA = "Achromatopsia";
    private static float[][] ACHROMATOPSIA_TRANSFORM = {{0.299f,0.587f,0.114f},{0.299f,0.587f,0.114f},{0.299f,0.587f,0.114f}};

    public static final String ACHROMATOMALY = "Achromatomaly";
    private static float[][] ACHROMATOMALY_TRANSFORM = {{0.618f,0.32f,0.062f},{0.163f,0.775f,0.062f},{0.299f,0.587f,0.114f}};


    //Protanopia:{ R:[56.667, 43.333, 0], G:[55.833, 44.167, 0], B:[0, 24.167, 75.833]}
    public static float[] simulateProtanopia(float[] originalRgb){
        int nrOfChannels = 3; //R, G, B
        float[] result = new float[nrOfChannels];
        for(int i = 0; i<nrOfChannels;i++){
            result[i]=0;
            for(int j = 0; j<nrOfChannels;j++){
                result[i] += originalRgb[j]* PROTANOPIA_TRANSFORM[i][j];
            }
        }
        return result;
    }

    
    public static float[] simulateProtanomaly(float[] originalRgb){
        int nrOfChannels = 3; //R, G, B
        float[] result = new float[nrOfChannels];
        for(int i = 0; i<nrOfChannels;i++){
            result[i]=0;
            for(int j = 0; j<nrOfChannels;j++){
                result[i] += originalRgb[j]* PROTANOMALY_TRANSFORM[i][j];
            }
        }
        return result;
    }

    public static float[] simulateDeuteranopia(float[] originalRgb){
        int nrOfChannels = 3; //R, G, B
        float[] result = new float[nrOfChannels];
        for(int i = 0; i<nrOfChannels;i++){
            result[i]=0;
            for(int j = 0; j<nrOfChannels;j++){
                result[i] += originalRgb[j]* DEUTERANOPIA_TRANSFORM[i][j];
            }
        }
        return result;
    }

    public static float[] simulateDeuteranomaly(float[] originalRgb){
        int nrOfChannels = 3; //R, G, B
        float[] result = new float[nrOfChannels];
        for(int i = 0; i<nrOfChannels;i++){
            result[i]=0;
            for(int j = 0; j<nrOfChannels;j++){
                result[i] += originalRgb[j]* DEUTERANOMALY_TRANSFORM[i][j];
            }
        }
        return result;
    }

    public static float[] simulateTritanopia(float[] originalRgb){
        int nrOfChannels = 3; //R, G, B
        float[] result = new float[nrOfChannels];
        for(int i = 0; i<nrOfChannels;i++){
            result[i]=0;
            for(int j = 0; j<nrOfChannels;j++){
                result[i] += originalRgb[j]* TRITANOPIA_TRANSFORM[i][j];
            }
        }
        return result;
    }

    public static float[] simulateTritanomaly(float[] originalRgb){
        int nrOfChannels = 3; //R, G, B
        float[] result = new float[nrOfChannels];
        for(int i = 0; i<nrOfChannels;i++){
            result[i]=0;
            for(int j = 0; j<nrOfChannels;j++){
                result[i] += originalRgb[j]* TRITANOMALY_TRANSFORM[i][j];
            }
        }
        return result;
    }

    public static float[] simulateAchromatopsia(float[] originalRgb){
        int nrOfChannels = 3; //R, G, B
        float[] result = new float[nrOfChannels];
        for(int i = 0; i<nrOfChannels;i++){
            result[i]=0;
            for(int j = 0; j<nrOfChannels;j++){
                result[i] += originalRgb[j]* ACHROMATOPSIA_TRANSFORM[i][j];
            }
        }
        return result;
    }

    public static float[] simulateAchromatomaly(float[] originalRgb){
        int nrOfChannels = 3; //R, G, B
        float[] result = new float[nrOfChannels];
        for(int i = 0; i<nrOfChannels;i++){
            result[i]=0;
            for(int j = 0; j<nrOfChannels;j++){
                result[i] += originalRgb[j]* ACHROMATOMALY_TRANSFORM[i][j];
            }
        }
        return result;
    }

    //Input: string, float[3] with RGB colours
    //output: float[3] with modified RGB colours
    public static float[] simulateColourBlindness(String code, float[] originalRgb){
        switch(code){
            case PROTANOPIA:
                return simulateProtanopia(originalRgb);
            case PROTANOMALY:
                return simulateProtanomaly(originalRgb);
            case DEUTERANOPIA:
                return simulateDeuteranopia(originalRgb);
            case DEUTERANOMALY:
                return  simulateDeuteranomaly(originalRgb);
            case TRITANOPIA:
                return simulateTritanopia(originalRgb);
            case TRITANOMALY:
                return simulateTritanomaly(originalRgb);
            case ACHROMATOPSIA:
                return  simulateAchromatopsia(originalRgb);
            case ACHROMATOMALY:
                return simulateAchromatomaly(originalRgb);
            default:
                return originalRgb;
        }
    }
}
