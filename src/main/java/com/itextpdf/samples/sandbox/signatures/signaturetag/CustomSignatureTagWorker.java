package com.itextpdf.samples.sandbox.signatures.signaturetag;

import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.styledxmlparser.node.IElementNode;

public class CustomSignatureTagWorker implements ITagWorker {

    private SignatureFieldAppearance signatureFieldAppearance;

    public CustomSignatureTagWorker(IElementNode tag, ProcessorContext context) {
        String signatureFieldId = tag.getAttribute("id");
        signatureFieldAppearance = new SignatureFieldAppearance(signatureFieldId);
        signatureFieldAppearance.setContent("Signature field");
        signatureFieldAppearance.setBorder(new SolidBorder(ColorConstants.GREEN, 1));
        String width = tag.getAttribute("width");
        signatureFieldAppearance.setWidth(Float.parseFloat(width));
        String height = tag.getAttribute("height");
        signatureFieldAppearance.setHeight(Float.parseFloat(height));
        signatureFieldAppearance.setInteractive(true);
    }

    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {

    }

    @Override
    public boolean processContent(String content, ProcessorContext context) {
        return false;
    }

    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        return false;
    }

    @Override
    public IPropertyContainer getElementResult() {
        return signatureFieldAppearance;
    }
}
