package com.itextpdf.samples.sandbox.pdfhtml.qrcodetag;

import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.impl.BlockCssApplier;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.styledxmlparser.node.IElementNode;

/**
 * Example of a custom CssApplier factory for pdfHTML
 * The tag <qr> is mapped on a BlockCssApplier. Every other tag is mapped to the default.
 */
public class QRCodeTagCssApplierFactory extends DefaultCssApplierFactory {

    @Override
    public ICssApplier getCustomCssApplier(IElementNode tag) {
        if (tag.name().equals("qr")) {
            return new BlockCssApplier();
        }
        return null;
    }
}
