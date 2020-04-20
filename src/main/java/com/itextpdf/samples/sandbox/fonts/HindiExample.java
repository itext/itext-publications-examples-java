package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class HindiExample {
    public static final String DEST = "./target/sandbox/fonts/hindi_example.pdf";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new HindiExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont f = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

        // "कार पार्किंग"
        Paragraph p1 = new Paragraph("\u0915\u093e\u0930 \u092a\u093e\u0930\u094d\u0915\u093f\u0902\u0917")
                .setFont(f);
        doc.add(p1);

        // \u0915 क \u093e ा \0930 र
        // \u092a प \u093e ा \u0930 र \u094d ्\u0915 क \u093f \u093f ि \u0902 ं\u0917 ग
        Paragraph p2 = new Paragraph("\\u0915 \u0915 \\u093e \u093e \\0930 \u0930\n"
                + "\\u092a \u092a \\u093e \u093e \\u0930 \u0930 \\u094d \u094d"
                + "\\u0915 \u0915 \\u093f \\u093f \u093f \\u0902 \u0902"
                + "\\u0917 \u0917");
        p2.setFont(f);
        doc.add(p2);

        Table table = new Table(UnitValue.createPercentArray(new float[] {10, 60, 30})).useAllAvailableWidth();
        Cell customerLblCell = new Cell().add(new Paragraph("CUSTOMERS"));
        table.addCell(customerLblCell);

        // "कारपार्किंग"
        p2 = new Paragraph("\u0915\u093e\u0930\u092a\u093e\u0930\u094d\u0915\u093f\u0902\u0917")
                .setFont(f)
                .setFontColor(new DeviceRgb(50, 205, 50));
        Cell balanceLblCell = new Cell().add(p2);
        table.addCell(balanceLblCell);

        table.setMarginTop(10);
        doc.add(table);

        doc.close();
    }
}
