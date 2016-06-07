/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/23280083/itextsharp-change-order-of-optional-content-groups
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.layer.PdfLayer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.List;

@Category(SampleTest.class)
public class ChangeOCG extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/change_ocg.pdf";
    public static final String SRC = "./src/test/resources/pdfs/ocg.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChangeOCG().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        List<PdfLayer> layers = pdfDoc.getCatalog().getOCProperties(true).getLayers();
        for (PdfLayer layer : layers) {
            if ("Nested layer 1".equals(layer.getPdfObject().get(PdfName.Name).toString())) {
                layer.setOn(false);
                break;
            }
        }
        pdfDoc.close();
    }
}
