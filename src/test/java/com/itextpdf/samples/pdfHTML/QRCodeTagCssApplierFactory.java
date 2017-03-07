package com.itextpdf.samples.pdfHTML;



import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.impl.BlockCssApplier;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.html2pdf.html.node.IElementNode;

public class QRCodeTagCssApplierFactory extends DefaultCssApplierFactory {

    @Override
    public ICssApplier getCustomCssApplier(IElementNode tag) {
        if (tag.name().equals("qr")) {
            return new BlockCssApplier();
        }
        return null;
    }
}
