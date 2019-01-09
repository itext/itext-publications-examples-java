/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/*
 * This example was written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/30369587/how-to-write-bulleted-list-in-pdf-using-itext-jar-but-item-should-not-in-next-li
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class Bullets extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/bullets.pdf";
    public static final String[] ITEMS = {
            "Insurance system", "Agent", "Agency", "Agent Enrollment", "Agent Settings",
            "Appointment", "Continuing Education", "Hierarchy", "Recruiting", "Contract",
            "Message", "Correspondence", "Licensing", "Party"
    };

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddPointer().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        // License file is loaded because open type font is used and typography module is in classpath:
        // typography module is utilized and requires license.
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont zapfdingbats = PdfFontFactory.createFont(StandardFonts.ZAPFDINGBATS);
        Text bullet = new Text(String.valueOf((char) 108)).setFont(zapfdingbats);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        Paragraph p = new Paragraph("Items can be split if they don't fit at the end: ").setFont(font);
        for (String item : ITEMS) {
            p.add(bullet);
            p.add(new Text(" " + item + " ")).setFont(font);
        }
        doc.add(p);
        doc.add(new Paragraph("\n"));

        p = new Paragraph("Items can't be split if they don't fit at the end: ").setFont(font);
        for (String item : ITEMS) {
            p.add(bullet);
            p.add(new Text("\u00a0" + item.replace(' ', '\u00a0') + " ")).setFont(font);
        }
        doc.add(p);
        doc.add(new Paragraph("\n"));

        PdfFont f = PdfFontFactory.createFont("./src/test/resources/font/FreeSans.ttf", PdfEncodings.IDENTITY_H);
        p = new Paragraph("Items can't be split if they don't fit at the end: ").setFont(f);
        for (String item : ITEMS) {
            p.add(new Text("\u2022\u00a0" + item.replace(' ', '\u00a0') + " "));
        }
        doc.add(p);

        doc.close();
    }
}
