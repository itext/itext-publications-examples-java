/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.pdfhtml.headertagging;

import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.tags.DivTagWorker;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.element.Div;
import com.itextpdf.styledxmlparser.node.IElementNode;


public class CustomHTagWorker extends DivTagWorker {
    private int i;

    public CustomHTagWorker(IElementNode element, ProcessorContext context, int i) {
        super(element, context);
        this.i = i;
    }

    @Override
    public IPropertyContainer getElementResult() {
        Div div = (Div) super.getElementResult();
        div.getAccessibilityProperties().setRole("H" + i);
        return super.getElementResult();
    }
}
