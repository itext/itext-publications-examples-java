/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.pdfhtml.headertagging;

import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.styledxmlparser.node.IElementNode;

public class AccessibilityTagWorkerFactory extends DefaultTagWorkerFactory {

    @Override
    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
        switch (tag.name()) {
            case "h1":
                return new HeaderTagWorker(tag, context, 1);
            case "h2":
                return new HeaderTagWorker(tag, context, 2);
            case "h3":
                return new HeaderTagWorker(tag, context, 3);
            case "h4":
                return new HeaderTagWorker(tag, context, 4);
            case "h5":
                return new HeaderTagWorker(tag, context, 5);
            case "h6":
                return new HeaderTagWorker(tag, context, 6);
            case "th":
                return new TableHeaderTagWorker(tag, context);
            default:
                return null;
        }
    }
}

