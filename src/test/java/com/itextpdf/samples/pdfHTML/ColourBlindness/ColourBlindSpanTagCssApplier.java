/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.pdfHTML.ColourBlindness;

import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.css.CssConstants;
import com.itextpdf.html2pdf.css.apply.impl.SpanTagCssApplier;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.styledxmlparser.node.IStylesContainer;

import java.util.Map;

public class ColourBlindSpanTagCssApplier extends SpanTagCssApplier {

    private static final double RGB_MAX_VAL = 255.0;
    private String colourBlindness = ColourBlindnessTransforms.PROTANOPIA;

    /**
     * Set the from of colour blindness to simulate.
     * Accepted values are Protanopia, Protanomaly, Deuteranopia, Deuteranomaly, Tritanopia, Tritanomaly, Achromatopsia, Achromatomaly.
     * Default value is Protanopia
     * @param colourBlindness
     */
    public void setColourBlindness(String colourBlindness){
        this.colourBlindness = colourBlindness;
    }

    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker){
        Map<String, String> cssStyles = stylesContainer.getStyles();
        if(cssStyles.containsKey(CssConstants.COLOR)){
            String newColor = TransformColor(cssStyles.get(CssConstants.COLOR));
            cssStyles.put(CssConstants.COLOR,newColor);
            stylesContainer.setStyles(cssStyles);
        }
        if(cssStyles.containsKey(CssConstants.BACKGROUND_COLOR)){
            String newColor = TransformColor(cssStyles.get(CssConstants.BACKGROUND_COLOR));
            cssStyles.put(CssConstants.BACKGROUND_COLOR,newColor);
            stylesContainer.setStyles(cssStyles);
        }
        super.apply(context, stylesContainer,tagWorker);

    }

    private String TransformColor(String originalColor){
        //Array creation is a bit clumsy, could do with some cleaning
        float[] rgbaColor = WebColors.getRGBAColor(originalColor);
        float[] rgbColor = {rgbaColor[0],rgbaColor[1],rgbaColor[2]};
        float[] newColourRgb = ColourBlindnessTransforms.simulateColourBlindness(colourBlindness,rgbColor);
        float[] newColourRgba = {newColourRgb[0],newColourRgb[1],newColourRgb[2],rgbaColor[3]};
        double[] newColorArray = scaleColorFloatArray(newColourRgba);
        String newColorString = "rgba("+(int) newColorArray[0]+","+(int)newColorArray[1]+","+(int)newColorArray[2]+","+newColorArray[3]+")";
        return newColorString;
    }

    private double[] scaleColorFloatArray(float[] colors){
        double red = (colors[0]*RGB_MAX_VAL);
        double green =  (colors[1]*RGB_MAX_VAL);
        double blue =  (colors[2]*RGB_MAX_VAL);
        double[] res = {red,green, blue, (double) colors[3]};
        return res;
    }
}
