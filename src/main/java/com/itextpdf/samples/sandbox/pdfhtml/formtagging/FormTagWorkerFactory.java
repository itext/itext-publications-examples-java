package com.itextpdf.samples.sandbox.pdfhtml.formtagging;

import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.styledxmlparser.node.IElementNode;

public class FormTagWorkerFactory extends DefaultTagWorkerFactory {
    @Override
    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
        switch (tag.name()) {
            case "button":
                return new CustomButtonTagWorker(tag, context);
            case "input":
                return new CustomInputTagWorker(tag, context);
            case "select":
                return new CustomSelectTagWorker(tag, context);
            case "textarea":
                return new CustomTextAreaTagWorker(tag, context);
            default:
                return null;
        }
    }
}
