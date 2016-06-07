/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/33733432/how-do-make-itext-to-accept-unicode-having-sequence-like-below/33733782
 */
package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class HindiExample extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/fonts/hindi_example.pdf";
    public static final String FONT
            = "./src/test/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new HindiExample().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont f = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

        Paragraph p1 = new Paragraph("\u0915\u093e\u0930 \u092a\u093e\u0930\u094d\u0915\u093f\u0902\u0917").setFont(f);
        doc.add(p1);

        Paragraph p2 = new Paragraph("\\u0915 \u0915 \\u093e \u093e \\0930 \u0930\n"
                + "\\u092a \u092a \\u093e \u093e \\u0930 \u0930 \\u094d \u094d"
                + "\\u0915 \u0915 \\u093f \\u093f \u093f \\u0902 \u0902"
                + "\\u0917 \u0917").setFont(f);
        doc.add(p2);

        Table table = new Table(new float[]{10, 60, 30});
        table.setWidthPercent(100);
        Cell customerLblCell = new Cell().add("CUSTOMERS");
        Cell balanceLblCell = new Cell().add(new Paragraph("\u0915\u093e\u0930\u092a\u093e\u0930\u094d\u0915\u093f\u0902\u0917")
                .setFont(f)
                .setFontColor(new DeviceRgb(50, 205, 50)));
        table.addCell(customerLblCell);
        table.addCell(balanceLblCell);
        table.setMarginTop(10);
        doc.add(table);

        doc.close();
    }
}
