/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.pdfHTML.AccessiblePDF.HeaderTagging;

import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.styledxmlparser.node.IElementNode;

public class AccessibilityTagWorkerFactory extends DefaultTagWorkerFactory {

    @Override
    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
        //This can probably replaced with a regex or string pattern
        if(tag.name().equals("h1")){
            return new HeaderTagWorker(tag, context,1);
        }
        if(tag.name().equals("h2")){
            return new HeaderTagWorker(tag, context,2);
        }
        if(tag.name().equals("h3")){
            return new HeaderTagWorker(tag, context,3);
        }
        if(tag.name().equals("h4")){
            return new HeaderTagWorker(tag, context,4);
        }
        if(tag.name().equals("h5")){
            return new HeaderTagWorker(tag, context,5);
        }
        if(tag.name().equals("h6")){
            return new HeaderTagWorker(tag, context,6);
        }

        if(tag.name().equals("th")){
            return new TableHeaderTagWorker(tag,context);
        }

        return null;
    }
}

