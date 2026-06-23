package com.itextpdf.samples.sandbox.layout;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.SectionBreak;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.properties.margins.MarginBoxName;
import com.itextpdf.layout.properties.margins.PageMarginBoxes;
import com.itextpdf.layout.properties.margins.PageMarginContent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * DynamicPageMargins.java
 *
 * Example showing how to set page margins with dynamic content.
 * Demonstrates applying different margin box content to specific pages,
 * to pages selected by a predicate, and via a SectionBreak with margin
 * boxes on all four sides.
 */

public class DynamicPageMargins {

    public static final String DEST = "./target/sandbox/layout/dynamicPageMargins.pdf";

    private static final DeviceRgb PAGE2_COLOR = new DeviceRgb(255, 196, 140);
    private static final DeviceRgb EVEN_COLOR = new DeviceRgb(140, 196, 255);

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DynamicPageMargins().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(bodyText("Page 1", "No margin box has been set yet."));

        // Margin boxes for a single, specific page number.
        // This will only affect page 2, regardless of how many pages the document ends up having.
        doc.setPageMargins(2, createTopMarginBoxes("PAGE 2 ONLY", PAGE2_COLOR));

        doc.add(new AreaBreak());
        doc.add(bodyText("Page 2", "A margin box was set for this specific page only."));

        doc.add(new AreaBreak());
        doc.add(bodyText("Page 3", "The page-2-only margin box no longer applies here."));

        // Margin boxes driven by a predicate.
        // This will affect every even page added after this call, for example pages 4, 6, 8...
        doc.setPageMargins(pageNum -> pageNum % 2 == 0, createTopMarginBoxes("EVEN PAGE", EVEN_COLOR));

        doc.add(new AreaBreak());
        doc.add(bodyText("Page 4", "An even page, so the predicate-based margin box applies."));

        doc.add(new AreaBreak());
        doc.add(bodyText("Page 5", "An odd page, so the predicate-based margin box does not apply."));

        // A SectionBreak can also carry its own margin boxes.
        // Here all four sides (top, bottom, left, right) get content,
        // and these margin boxes apply from this point on, replacing the predicate-based ones.
        doc.add(new SectionBreak(createAllSidesMarginBoxes()));
        doc.add(bodyText("Page 6", "A SectionBreak introduced margin boxes on all four sides."));

        doc.add(new AreaBreak());
        doc.add(bodyText("Page 7", "The all-four-sides margin boxes from the SectionBreak persist."));

        doc.close();
    }

    // Builds a single, centered, color-coded top margin box with a bold label.
    private PageMarginBoxes createTopMarginBoxes(String label, DeviceRgb color) {
        Div header = marginBoxContent(label, color);

        List<PageMarginContent> elements = new ArrayList<>();
        elements.add(new PageMarginContent(MarginBoxName.TOP, header));
        return new PageMarginBoxes(elements);
    }

    // Builds a basic set of margin boxes for all four sides: top, bottom, left and right.
    // Each side has a distinct shape and style so they're easy to tell apart visually.
    private PageMarginBoxes createAllSidesMarginBoxes() {
        List<PageMarginContent> elements = new ArrayList<>();
        elements.add(new PageMarginContent(MarginBoxName.TOP, topMarginContent()));
        elements.add(new PageMarginContent(MarginBoxName.BOTTOM, bottomMarginContent()));
        elements.add(new PageMarginContent(MarginBoxName.LEFT, leftMarginContent()));
        elements.add(new PageMarginContent(MarginBoxName.RIGHT, rightMarginContent()));
        return new PageMarginBoxes(elements);
    }

    // Top margin: a tall, bold banner with a thick bottom border, like a page header.
    private Div topMarginContent() {
        return new Div()
                .add(new Paragraph("TOP MARGIN")
                        .setFontColor(ColorConstants.WHITE)
                        .setFontSize(16)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMargin(0))
                .setBackgroundColor(new DeviceRgb(90, 60, 160))
                .setHeight(50)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorderBottom(new SolidBorder(new DeviceRgb(60, 30, 120), 4));
    }

    // Bottom margin: a short, light strip with small, letter-spaced uppercase text,
    // like a footer caption rather than a heading.
    private Div bottomMarginContent() {
        return new Div()
                .add(new Paragraph("Bottom Margin \u2022 Page Footer")
                        .setFontColor(new DeviceRgb(255, 140, 200))
                        .setFontSize(9)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMargin(0))
                .setBackgroundColor(new DeviceRgb(255, 235, 245))
                .setHeight(20)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorderTop(new SolidBorder(new DeviceRgb(255, 140, 200), 1));
    }

    // Left margin: a narrow vertical sidebar, text aligned to the left rather than centered,
    // with a colored left edge bar.
    private Div leftMarginContent() {
        return new Div()
                .add(new Paragraph("LEFT")
                        .setFontColor(new DeviceRgb(20, 130, 100))
                        .setFontSize(11)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setMargin(0))
                .setBackgroundColor(new DeviceRgb(225, 250, 240))
                .setPaddingLeft(6)
                .setBorderLeft(new SolidBorder(new DeviceRgb(140, 255, 200), 5));
    }

    // Right margin: a narrow vertical sidebar, mirrored from the left, text aligned right,
    // with a colored right edge bar.
    private Div rightMarginContent() {
        return new Div()
                .add(new Paragraph("RIGHT")
                        .setFontColor(new DeviceRgb(180, 110, 0))
                        .setFontSize(11)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setMargin(0))
                .setBackgroundColor(new DeviceRgb(255, 245, 225))
                .setPaddingRight(6)
                .setBorderRight(new SolidBorder(new DeviceRgb(255, 220, 140), 5));
    }

    // Builds a single, centered, color-coded labelled Div used as top margin box content
    // for the page-specific and predicate-based examples.
    private Div marginBoxContent(String label, DeviceRgb color) {
        return new Div()
                .add(new Paragraph(label)
                        .setFontColor(ColorConstants.WHITE)
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMargin(0))
                .setBackgroundColor(color)
                .setHeight(40)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
    }

    // Builds the main page content: a page-number badge plus an explanatory line.
    private Div bodyText(String pageLabel, String description) {
        Paragraph badge = new Paragraph(pageLabel)
                .setFontSize(22)
                .setFontColor(ColorConstants.DARK_GRAY)
                .setMargin(0);

        Paragraph text = new Paragraph(description)
                .setFontSize(12)
                .setFontColor(ColorConstants.GRAY)
                .setMarginTop(8);

        return new Div()
                .add(badge)
                .add(text)
                .setWidth(UnitValue.createPercentValue(80))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(20)
                .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                .setMarginTop(60);
    }
}