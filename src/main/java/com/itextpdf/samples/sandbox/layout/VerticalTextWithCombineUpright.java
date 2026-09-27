package com.itextpdf.samples.sandbox.layout;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.element.VerticalParagraph;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.TextCombineUpright;

import java.io.File;
import java.io.IOException;

/*
 * VerticalTextWithCombineUpright.java
 *
 * Example showing vertical text combined with CombineUpright (tate-chu-yoko).
 * CombineUpright rotates short runs of Latin/numeric characters to display
 * upright within a vertical text flow, which is standard in CJK typography
 * for inline numbers, abbreviations and short Latin words.
 */

public class VerticalTextWithCombineUpright {

    public static final String DEST = "./target/sandbox/layout/verticalTextWithCombineUpright.pdf";
    public static final String FONT = "./src/main/resources/font/NotoSansCJKjp-Regular.otf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new VerticalTextWithCombineUpright().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        FontProvider fontProvider = new FontProvider();
        fontProvider.addFont(FONT, PdfEncodings.IDENTITY_H);
        doc.setFontProvider(fontProvider);
        doc.setFont(font);

        doc.add(new Paragraph("Vertical Text with Combine Upright (縦中横)")
                .setFontSize(20)
                .setFontColor(new DeviceRgb(60, 60, 150))
                .setMarginBottom(20));

        doc.add(new Paragraph(
                "CombineUpright (tate-chu-yoko / 縦中横) keeps short Latin or numeric runs "
                        + "upright within a vertical text flow. "
                        + "Compare the two columns below: first page uses plain vertical text, "
                        + "second page wraps numbers and abbreviations in CombineUpright.")
                .setMarginBottom(20));

        // Left column: plain vertical text — numbers and Latin rotate sideways.
        VerticalParagraph plainVertical = new VerticalParagraph(true);
        plainVertical.add("発売日：");
        plainVertical.add("2024");
        plainVertical.add("年");
        plainVertical.add("10");
        plainVertical.add("月");
        plainVertical.add("PDF\n");
        plainVertical.add("ページ数：");
        plainVertical.add("128");
        plainVertical.add("ページ");
        plainVertical.setFontSize(14);
        plainVertical.setBackgroundColor(new DeviceRgb(255, 240, 240));
        plainVertical.setFontColor(new DeviceRgb(120, 40, 40));
        plainVertical.setWidth(40);
        plainVertical.setHeight(400);

        // Right column: same content but short runs have TEXT_COMBINE_UPRIGHT applied —
        // numbers and Latin abbreviations display upright within the vertical flow.
        VerticalParagraph uprightVertical = new VerticalParagraph(true);
        uprightVertical.add("発売日：");
        Text year = new Text("2024");
        year.setProperty(Property.TEXT_COMBINE_UPRIGHT, TextCombineUpright.ALL);
        uprightVertical.add(year);
        uprightVertical.add("年");
        Text month = new Text("10");
        month.setProperty(Property.TEXT_COMBINE_UPRIGHT, TextCombineUpright.ALL);
        uprightVertical.add(month);
        uprightVertical.add("月");
        Text version = new Text("PDF");
        version.setProperty(Property.TEXT_COMBINE_UPRIGHT, TextCombineUpright.ALL);
        uprightVertical.add(version);
        uprightVertical.add("\nページ数：");
        Text pages = new Text("128");
        pages.setProperty(Property.TEXT_COMBINE_UPRIGHT, TextCombineUpright.ALL);
        uprightVertical.add(pages);
        uprightVertical.add("ページ");
        uprightVertical.setFontSize(14);
        uprightVertical.setBackgroundColor(new DeviceRgb(235, 245, 255));
        uprightVertical.setFontColor(new DeviceRgb(40, 60, 120));
        uprightVertical.setWidth(40);
        uprightVertical.setHeight(400);

        doc.add(new Div()
                .add(new Paragraph("Without CombineUpright")
                        .setFontSize(10).setFontColor(ColorConstants.GRAY))
                .add(plainVertical)
                .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                .setPadding(10));

        // Page 2: same content with TEXT_COMBINE_UPRIGHT applied — numbers and
        // Latin abbreviations now display upright within the vertical flow.
        doc.add(new AreaBreak());

        doc.add(new Paragraph("With CombineUpright (縦中横)")
                .setFontSize(20)
                .setFontColor(new DeviceRgb(60, 60, 150))
                .setMarginBottom(20));

        doc.add(new Div()
                .add(new Paragraph("With CombineUpright")
                        .setFontSize(10).setFontColor(ColorConstants.GRAY))
                .add(uprightVertical)
                .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                .setPadding(10));
        doc.close();
    }
}