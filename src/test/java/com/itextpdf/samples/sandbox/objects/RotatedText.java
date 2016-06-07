/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/34310018/changing-font-on-pdf-rotated-text
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.barcodes.BarcodeEAN;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class RotatedText extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/rotated_text.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RotatedText().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(60, 140));
        doc.setMargins(5, 5, 5, 5);

        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        PdfFont regular = PdfFontFactory.createFont(FontConstants.HELVETICA);

        Paragraph p1 = new Paragraph();
        p1.add(new Text("23").setFont(bold).setFontSize(12));
        p1.add(new Text("000").setFont(bold).setFontSize(6));
        doc.add(p1);

        Paragraph p2 = new Paragraph("T.T.C.").setFont(regular).setFontSize(6);
        p2.setTextAlignment(TextAlignment.RIGHT);
        doc.add(p2);

        BarcodeEAN barcode = new BarcodeEAN(pdfDoc);
        barcode.setCodeType(BarcodeEAN.EAN8);
        barcode.setCode("12345678");
        Rectangle rect = barcode.getBarcodeSize();
        PdfFormXObject template = new PdfFormXObject(new Rectangle(rect.getWidth(), rect.getHeight() + 10));
        PdfCanvas templateCanvas = new PdfCanvas(template, pdfDoc);
        new Canvas(templateCanvas, pdfDoc, new Rectangle(rect.getWidth(), rect.getHeight() + 10))
                .showTextAligned(new Paragraph("DARK GRAY").setFont(regular).setFontSize(6), 0, rect.getHeight() + 2, TextAlignment.LEFT);
        barcode.placeBarcode(templateCanvas, Color.BLACK, Color.BLACK);
        Image image = new Image(template);
        image.setRotationAngle(Math.toRadians(90));
        image.setAutoScale(true);
        doc.add(image);

        Paragraph p3 = new Paragraph("SMALL").setFont(regular).setFontSize(6);
        p3.setTextAlignment(TextAlignment.CENTER);
        doc.add(p3);

        doc.close();
    }
}
