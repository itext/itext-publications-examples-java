/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.pdfhtml.colorblindness;

import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.html2pdf.html.TagConstants;
import com.itextpdf.styledxmlparser.node.IElementNode;

public class ColorBlindnessCssApplierFactory extends DefaultCssApplierFactory {

    // Color blindness type
    private String colorType;

    public ColorBlindnessCssApplierFactory(String colorType) {
        this.colorType = colorType;
    }

    @Override
    public ICssApplier getCustomCssApplier(IElementNode tag) {
        if (tag.name().equals(TagConstants.DIV)) {
            ColorBlindBlockCssApplier applier = new ColorBlindBlockCssApplier();
            applier.setColorBlindness(colorType);
            return applier;
        }

        if (tag.name().equals(TagConstants.SPAN)) {
            ColorBlindSpanTagCssApplier applier = new ColorBlindSpanTagCssApplier();
            applier.setColorBlindness(colorType);
            return applier;
        }

        return null;
    }
}
