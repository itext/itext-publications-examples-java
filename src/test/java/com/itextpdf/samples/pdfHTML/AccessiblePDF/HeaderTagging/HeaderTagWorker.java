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
        div.setRole(new PdfName("H"+i));
        return super.getElementResult();
    }
}

