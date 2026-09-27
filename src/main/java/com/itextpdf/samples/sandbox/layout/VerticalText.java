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
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.VerticalParagraph;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.renderer.FlexContainerRenderer;

import java.io.File;
import java.io.IOException;

/*
 * VerticalText.java
 *
 * Example showing how to render text vertically using VerticalParagraph.
 * Demonstrates both VERTICAL_LR (left-to-right columns) and VERTICAL_RL
 * (right-to-left columns), mixing English and CJK characters.
 */

public class VerticalText {

    public static final String DEST = "./target/sandbox/layout/verticalText.pdf";
    public static final String FONT = "./src/main/resources/font/NotoSansCJKjp-Regular.otf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new VerticalText().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        FontProvider fontProvider = new FontProvider();
        fontProvider.addFont(FONT, PdfEncodings.IDENTITY_H);
        doc.setFontProvider(fontProvider);
        doc.setFont(font);

        doc.add(new Paragraph("Vertical Text Demo")
                .setFontSize(20)
                .setFontColor(new DeviceRgb(60, 60, 150))
                .setMarginBottom(20));

        doc.add(new Paragraph(
                "VerticalParagraph supports two column directions: "
                        + "VERTICAL_LR (false) adds new columns left-to-right, "
                        + "VERTICAL_RL (true) adds new columns right-to-left — standard for CJK.")
                .setMarginBottom(20));

        // VERTICAL_LR (false): columns flow left-to-right.
        VerticalParagraph ltr = new VerticalParagraph(false);
        ltr.add("VERTICAL_LR\n");
        ltr.add("左から右へ\n");
        ltr.add("Columns: left to right.\n");
        ltr.add("日本語・中文・한국어\n");
        ltr.setFontSize(14);
        ltr.setBackgroundColor(new DeviceRgb(235, 245, 255));
        ltr.setFontColor(new DeviceRgb(40, 60, 120));
        ltr.setHeight(400);

        // VERTICAL_RL (true): columns flow right-to-left — standard for CJK vertical typography.
        VerticalParagraph rtl = new VerticalParagraph(true);
        rtl.add("VERTICAL_RL\n");
        rtl.add("右から左へ\n");
        rtl.add("Columns: right to left.\n");
        rtl.add("日本語・中文・한국어\n");
        rtl.setFontSize(14);
        rtl.setBackgroundColor(new DeviceRgb(235, 250, 238));
        rtl.setFontColor(new DeviceRgb(20, 100, 60));
        rtl.setHeight(400);

        Div columns = new Div()
                .add(new Div()
                        .add(new Paragraph("VERTICAL_LR (false)")
                                .setFontSize(10).setFontColor(ColorConstants.GRAY))
                        .add(ltr)
                        .setMarginRight(30))
                .add(new Div()
                        .add(new Paragraph("VERTICAL_RL (true)")
                                .setFontSize(10).setFontColor(ColorConstants.GRAY))
                        .add(rtl))
                .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                .setPadding(10);
        columns.setNextRenderer(new FlexContainerRenderer(columns));

        doc.add(columns);

        doc.close();
    }
}