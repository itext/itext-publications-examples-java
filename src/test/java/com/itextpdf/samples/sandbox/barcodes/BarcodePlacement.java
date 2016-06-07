/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.barcodes;

import com.itextpdf.barcodes.BarcodePDF417;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class BarcodePlacement extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/barcodes/barcode_placement.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new BarcodePlacement().manipulatePdf(DEST);
    }

    // We've changed the order of arguments (in comparison with itext5 example) to make it more clear
    public Image createBarcode(float mw, float mh, PdfDocument pdfDoc) {
        BarcodePDF417 barcode = new BarcodePDF417();
        barcode.setCode("BarcodePDF417 barcode");
        return new Image(barcode.createFormXObject(Color.BLACK, pdfDoc)).scale(mw, mh);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Image img = createBarcode(1, 1, pdfDoc);
        doc.add(new Paragraph(String.format("This barcode measures %s by %s user units",
                img.getImageScaledWidth(), img.getImageScaledHeight())));
        doc.add(img);
        img = createBarcode(3, 3, pdfDoc);
        doc.add(new Paragraph(String.format("This barcode measures %s by %s user units",
                img.getImageScaledWidth(), img.getImageScaledHeight())));
        doc.add(img);
        img = createBarcode(3, 1, pdfDoc);
        doc.add(new Paragraph(String.format("This barcode measures %s by %s user units",
                img.getImageScaledWidth(), img.getImageScaledHeight())));
        doc.add(img);

        doc.close();
    }
}
