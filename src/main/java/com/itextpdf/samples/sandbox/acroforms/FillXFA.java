package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.xfa.XfaForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.io.FileInputStream;

public class FillXFA {
    public static final String DEST = "./target/sandbox/acroforms/purchase_order_filled.pdf";

    public static final String SRC = "./src/main/resources/pdfs/purchase_order.pdf";
    public static final String XML = "./src/main/resources/xml/data.xml";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FillXFA().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfdoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfdoc, true);

        XfaForm xfa = form.getXfaForm();

        // Method fills this object with XFA data under datasets/data.
        xfa.fillXfaForm(new FileInputStream(XML));
        xfa.write(pdfdoc);

        pdfdoc.close();
    }
}
