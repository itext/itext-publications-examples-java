/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.html.qrcode;

import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.styledxmlparser.node.IElementNode;

/**
 * Example of a custom tagworkerfactory for pdfHTML
 * The tag {@code <qr>} is mapped on a QRCode tagworker. Every other tag is mapped to the default.
 */
public class QRCodeTagWorkerFactory extends DefaultTagWorkerFactory {

    /** {@inheritDoc} */
    @Override
    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
        if(tag.name().equals("qr")){
            return new QRCodeTagWorker(tag, context);
        }
        return null;
    }
}
