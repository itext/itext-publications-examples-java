package com.itextpdf.samples.sandbox.signatures.signaturetag;

import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.styledxmlparser.node.IElementNode;

public class SignatureTagWorkerFactory extends DefaultTagWorkerFactory {
    @Override
    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
        if (tag.name().equals("signature-field")) {
            return new CustomSignatureTagWorker(tag, context);
        }
        return super.getCustomTagWorker(tag, context);
    }
}
