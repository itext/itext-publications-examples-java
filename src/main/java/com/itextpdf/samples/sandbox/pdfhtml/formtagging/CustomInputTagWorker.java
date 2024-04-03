package com.itextpdf.samples.sandbox.pdfhtml.formtagging;

import com.itextpdf.forms.form.element.FormField;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.tags.InputTagWorker;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.styledxmlparser.node.IElementNode;

public class CustomInputTagWorker extends InputTagWorker {
    public CustomInputTagWorker(IElementNode element,
            ProcessorContext context) {
        super(element, context);
    }

    @Override
    public IPropertyContainer getElementResult() {
        FormField formField = (FormField) super.getElementResult();
        formField.setInteractive(false);
        formField.setBackgroundColor(ColorConstants.YELLOW);
        formField.getAccessibilityProperties().setRole(StandardRoles.LBL);
        return formField;
    }
}
