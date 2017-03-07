package com.itextpdf.samples.pdfHTML;

import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.html.node.IElementNode;

/**
 * @author Michael Demey
 */
public class CustomTagWorkerFactory extends DefaultTagWorkerFactory {

    @Override
    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
        if ( "qr".equalsIgnoreCase(tag.name()) ) {
            return new QRCodeTagWorker(tag, context);
        }

        return null;
    }
}