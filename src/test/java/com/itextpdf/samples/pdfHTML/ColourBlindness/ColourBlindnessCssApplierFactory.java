package com.itextpdf.samples.pdfHTML.ColourBlindness;


import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.html2pdf.html.TagConstants;
import com.itextpdf.html2pdf.html.node.IElementNode;

public class ColourBlindnessCssApplierFactory extends DefaultCssApplierFactory {

    private String toSimulate;

    public ColourBlindnessCssApplierFactory(String toSimulate){
        this.toSimulate = toSimulate;
    }
    @Override
    public ICssApplier getCustomCssApplier(IElementNode tag) {
        if(tag.name().equals(TagConstants.DIV)){
            ColourBlindBlockCssApplier applier =  new ColourBlindBlockCssApplier();
            applier.setColourBlindness(toSimulate);
            return applier;
        }

        if(tag.name().equals(TagConstants.SPAN)){
            ColourBlindSpanTagCssApplier applier =  new ColourBlindSpanTagCssApplier();
            applier.setColourBlindness(toSimulate);
            return applier;
        }

        return null;
    }
}
