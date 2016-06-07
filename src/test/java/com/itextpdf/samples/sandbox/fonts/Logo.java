/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie.
 */
package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfType3Font;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class Logo extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/fonts/logo.pdf";
    public static final String FONT = "./src/test/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Logo().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        float linewidth = 125;

        PdfType3Font t3 = PdfFontFactory.createType3Font(pdfDoc, true);
        PdfCanvas i = t3.addGlyph('I', 700, 0, 0, 1200, 600);
        i.setLineWidth(10);
        i.setStrokeColor(new DeviceRgb(0xf9, 0x9d, 0x25));
        i.setLineWidth(linewidth);
        i.setLineCapStyle(PdfCanvasConstants.LineCapStyle.ROUND);
        i.moveTo(600, 36);
        i.lineTo(600, 564);
        i.stroke();

        PdfCanvas t = t3.addGlyph('T', 1170, 0, 0, 1200, 600);
        t.setLineWidth(10);
        t.setStrokeColor(new DeviceRgb(0x08, 0x49, 0x75));
        t.setLineWidth(linewidth);
        t.setLineCapStyle(PdfCanvasConstants.LineCapStyle.ROUND);
        t.moveTo(144, 564);
        t.lineTo(1056, 564);
        t.moveTo(600, 36);
        t.lineTo(600, 564);
        t.stroke();

        PdfCanvas e = t3.addGlyph('E', 1150, 0, 0, 1200, 600);
        e.setLineWidth(10);
        e.setStrokeColor(new DeviceRgb(0xf8, 0x9b, 0x22));
        e.setLineWidth(linewidth);
        e.setLineCapStyle(PdfCanvasConstants.LineCapStyle.ROUND);
        e.moveTo(144, 36);
        e.lineTo(1056, 36);
        e.moveTo(144, 300);
        e.lineTo(1056, 300);
        e.moveTo(144, 564);
        e.lineTo(1056, 564);
        e.stroke();

        PdfCanvas x = t3.addGlyph('X', 1160, 0, 0, 1200, 600);
        x.setStrokeColor(new DeviceRgb(0x10, 0x46, 0x75));
        x.setLineWidth(10);
        x.setLineWidth(linewidth);
        x.setLineCapStyle(PdfCanvasConstants.LineCapStyle.ROUND);
        x.moveTo(144, 36);
        x.lineTo(1056, 564);
        x.moveTo(144, 564);
        x.lineTo(1056, 36);
        x.stroke();

        Paragraph p = new Paragraph("ITEXT").setFont(t3).setFontSize(20);
        doc.add(p);

        p = new Paragraph("I\nT\nE\nX\nT").setFixedLeading(20).setFont(t3).setFontSize(20);
        doc.add(p);

        doc.close();
    }
}
