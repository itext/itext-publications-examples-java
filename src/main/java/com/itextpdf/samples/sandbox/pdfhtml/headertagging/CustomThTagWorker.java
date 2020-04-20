package com.itextpdf.samples.sandbox.pdfhtml.headertagging;

import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.tags.TdTagWorker;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.styledxmlparser.node.IElementNode;

public class CustomThTagWorker extends TdTagWorker {
    public CustomThTagWorker(IElementNode element, ProcessorContext context) {
        super(element, context);
    }

    @Override
    public IPropertyContainer getElementResult() {
        Cell cell = (Cell) super.getElementResult();
        cell.getAccessibilityProperties().setRole(StandardRoles.TH);
        return super.getElementResult();
    }
}
