/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2018 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.pdfHTML.AccessiblePDF.HeaderTagging;

import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.tags.DivTagWorker;
import com.itextpdf.html2pdf.html.node.IElementNode;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.element.Div;


public class HeaderTagWorker extends DivTagWorker {
    private int i;
    public HeaderTagWorker(IElementNode element, ProcessorContext context, int i) {
        super(element, context);
        this.i = i;
    }

    @Override
    public IPropertyContainer getElementResult() {
        Div div =(Div) super.getElementResult();
        div.getAccessibilityProperties().setRole("H"+i);
        return super.getElementResult();
    }
}

