package com.itextpdf.samples.sandbox.pdfhtml.colorblindness;

import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.css.CssConstants;
import com.itextpdf.html2pdf.css.apply.impl.SpanTagCssApplier;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.styledxmlparser.node.IStylesContainer;

import java.util.Map;

public class ColorBlindSpanTagCssApplier extends SpanTagCssApplier {

    private static final double RGB_MAX_VAL = 255.0;

    private String colorBlindness = ColorBlindnessTransforms.PROTANOPIA;

    /**
     * Set the from of color blindness to simulate.
     * Accepted values are Protanopia, Protanomaly, Deuteranopia, Deuteranomaly, Tritanopia, Tritanomaly, Achromatopsia, Achromatomaly.
     * Default value is Protanopia
     *
     * @param colorBlindness
     */
    public void setColorBlindness(String colorBlindness) {
        this.colorBlindness = colorBlindness;
    }

    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
        Map<String, String> cssStyles = stylesContainer.getStyles();
        if (cssStyles.containsKey(CssConstants.COLOR)) {
            String newColor = TransformColor(cssStyles.get(CssConstants.COLOR));
            cssStyles.put(CssConstants.COLOR, newColor);
            stylesContainer.setStyles(cssStyles);
        }
        if (cssStyles.containsKey(CssConstants.BACKGROUND_COLOR)) {
            String newColor = TransformColor(cssStyles.get(CssConstants.BACKGROUND_COLOR));
            cssStyles.put(CssConstants.BACKGROUND_COLOR, newColor);
            stylesContainer.setStyles(cssStyles);
        }
        super.apply(context, stylesContainer, tagWorker);

    }

    private String TransformColor(String originalColor) {
        float[] rgbaColor = WebColors.getRGBAColor(originalColor);
        float[] rgbColor = {rgbaColor[0], rgbaColor[1], rgbaColor[2]};
        float[] newColorRgb = ColorBlindnessTransforms.simulateColorBlindness(colorBlindness, rgbColor);
        float[] newColorRgba = {newColorRgb[0], newColorRgb[1], newColorRgb[2], rgbaColor[3]};
        double[] newColorArray = scaleColorFloatArray(newColorRgba);
        String newColorString = "rgba(" + (int) newColorArray[0] + "," + (int) newColorArray[1] + ","
                + (int) newColorArray[2] + "," + newColorArray[3] + ")";
        return newColorString;
    }

    private double[] scaleColorFloatArray(float[] colors) {
        double red = (colors[0] * RGB_MAX_VAL);
        double green = (colors[1] * RGB_MAX_VAL);
        double blue = (colors[2] * RGB_MAX_VAL);
        double[] res = {red, green, blue, (double) colors[3]};
        return res;
    }
}
