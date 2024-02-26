package com.itextpdf.samples.sandbox.pdfhtml.qrcodetag;

import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.styledxmlparser.node.IElementNode;

/**
 * Example of a custom tagworkerfactory for pdfHTML
 * The tag <qr> is mapped on a QRCode tagworker. Every other tag is mapped to the default.
 */
public class QRCodeTagWorkerFactory extends DefaultTagWorkerFactory {

    @Override
    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
        if (tag.name().equals("qr")) {
            return new QRCodeTagWorker(tag, context);
        }
        return null;
    }
}
