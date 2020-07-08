package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.BaseDirection;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class ArabicExample2 {

    public static final String DEST = "./target/sandbox/fonts/arabic_example2.pdf";
    public static final String FONT = "./src/main/resources/font/NotoNaskhArabic-Regular.ttf";

    // "السعر الاجمالي"
    public static final String ARABIC = "\u0627\u0644\u0633\u0639\u0631 \u0627\u0644\u0627\u062c\u0645\u0627\u0644\u064a";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ArabicExample2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(1).useAllAvailableWidth();
        PdfFont f = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

        Paragraph p = new Paragraph("test value");
        p.add(new Text(ARABIC).setFont(f));

        Cell cell = new Cell().add(p);
        cell
                .setBaseDirection(BaseDirection.RIGHT_TO_LEFT)
                .setTextAlignment(TextAlignment.RIGHT);
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }
}
