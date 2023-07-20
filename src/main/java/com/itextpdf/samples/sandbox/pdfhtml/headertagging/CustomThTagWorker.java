/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
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
