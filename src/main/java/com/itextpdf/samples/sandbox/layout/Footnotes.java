package com.itextpdf.samples.sandbox.layout;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.margins.Footnote;
import com.itextpdf.layout.properties.margins.FootnoteAnchor;
import com.itextpdf.layout.properties.margins.FootnoteNumberingConfig;
import com.itextpdf.layout.properties.margins.FootnoteNumberingType;
import com.itextpdf.layout.properties.margins.FootnotesProperties;

import java.io.File;
import java.io.IOException;

/*
 * Footnotes.java
 *
 * Example showing how to add footnotes to a document.
 * Demonstrates linking inline text to a footnote at the bottom of the page
 * using FootnoteAnchor, styling the footnote content itself, and configuring
 * footnote numbering and container style via Document#setFootnotesProperties.
 */

public class Footnotes {

    public static final String DEST = "./target/sandbox/layout/footnotes.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Footnotes().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        // Optional: enable tagging.
        pdfDoc.setTagged();

        // Configure footnote numbering and the look of the footnotes container.
        // ROMAN_LOWER numbering will be used for the anchor markers (i, ii, iii, ...),
        // numbering restarts on every page, and the footnotes container gets a top border
        // and light background to visually separate it from the rest of the page.
        Style footnotesContainerStyle = new Style()
                .setBorderTop(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                .setBackgroundColor(new DeviceRgb(250, 250, 250))
                .setPaddingTop(8);

        FootnotesProperties footnotesProperties = new FootnotesProperties()
                .setFootnoteNumberingType(FootnoteNumberingType.ROMAN_LOWER)
                .setFootnoteNumberingConfig(FootnoteNumberingConfig.PER_PAGE)
                .setFootnotesContainerStyle(footnotesContainerStyle);

        doc.setFootnotesProperties(footnotesProperties);

        doc.add(new Paragraph("Footnotes")
                .setFontSize(20)
                .setFontColor(new DeviceRgb(60, 60, 150))
                .setMarginBottom(20));

        // With FootnotesProperties configured, FootnoteAnchor no longer needs an explicit
        // marker string: the marker is generated automatically based on the numbering type.
        Footnote firstFootnote = new Footnote(
                "Coffee was first cultivated in Ethiopia and later spread through the Arab world.");
        firstFootnote.setBorder(new SolidBorder(new DeviceRgb(140, 196, 255), 1));
        firstFootnote.setBackgroundColor(new DeviceRgb(235, 245, 255));

        FootnoteAnchor firstAnchor = new FootnoteAnchor(firstFootnote);
        firstAnchor.setFontColor(new DeviceRgb(20, 110, 200));

        Paragraph p1 = new Paragraph()
                .add("Coffee")
                .add(firstAnchor)
                .add(" is one of the most widely consumed beverages in the world.")
                .setMarginBottom(15);
        doc.add(p1);

        // A Footnote can also be built from a Paragraph, allowing the footnote
        // content itself to be styled independently from the surrounding text.
        Footnote secondFootnote = new Footnote(
                new Paragraph("Decaffeination removes at least 97% of the caffeine content.")
                        .setFontColor(ColorConstants.WHITE)
                        .setMargin(0));
        secondFootnote.setBackgroundColor(new DeviceRgb(255, 150, 90));

        FootnoteAnchor secondAnchor = new FootnoteAnchor(secondFootnote);
        secondAnchor.setFontColor(new DeviceRgb(220, 100, 40));

        Paragraph p2 = new Paragraph()
                .add("Some people prefer decaffeinated coffee")
                .add(secondAnchor)
                .add(" for the taste without the stimulant effect.")
                .setMarginBottom(15);
        doc.add(p2);

        // Multiple anchors can point to footnotes within the same paragraph;
        // each footnote is rendered independently at the bottom of the page.
        Footnote thirdFootnote = new Footnote(
                "Espresso is brewed by forcing hot water through finely-ground coffee under pressure.");
        thirdFootnote.setBorder(new SolidBorder(new DeviceRgb(140, 220, 160), 1));
        thirdFootnote.setBackgroundColor(new DeviceRgb(235, 250, 238));

        FootnoteAnchor thirdAnchor = new FootnoteAnchor(thirdFootnote);
        thirdAnchor.setFontColor(new DeviceRgb(40, 150, 80));

        Paragraph p3 = new Paragraph()
                .add("Popular brewing methods include drip filtering, French press, and espresso")
                .add(thirdAnchor)
                .add(".");
        doc.add(p3);

        // Because PER_PAGE numbering was configured, footnote markers restart from "i"
        // on every new page, instead of continuing as "iv" here.
        doc.add(new AreaBreak());
        doc.add(new Paragraph("Page 2: numbering restarts")
                .setFontSize(20)
                .setFontColor(new DeviceRgb(60, 60, 150))
                .setMarginBottom(20));

        Footnote fourthFootnote = new Footnote(
                "Arabica and Robusta are the two most commercially important coffee species.");
        fourthFootnote.setBorder(new SolidBorder(new DeviceRgb(140, 196, 255), 1));
        fourthFootnote.setBackgroundColor(new DeviceRgb(235, 245, 255));

        FootnoteAnchor fourthAnchor = new FootnoteAnchor(fourthFootnote);
        fourthAnchor.setFontColor(new DeviceRgb(20, 110, 200));

        Paragraph p4 = new Paragraph()
                .add("There are dozens of coffee species")
                .add(fourthAnchor)
                .add(", but only a few are grown at commercial scale.")
                .setMarginBottom(15);
        doc.add(p4);

        Footnote fifthFootnote = new Footnote(
                "Robusta beans generally contain more caffeine than Arabica beans.");
        fifthFootnote.setBackgroundColor(new DeviceRgb(255, 150, 90));

        FootnoteAnchor fifthAnchor = new FootnoteAnchor(fifthFootnote);
        fifthAnchor.setFontColor(new DeviceRgb(220, 100, 40));

        Paragraph p5 = new Paragraph()
                .add("Robusta is often considered to have a harsher taste")
                .add(fifthAnchor)
                .add(" than Arabica.");
        doc.add(p5);

        // A third page shows the marker sequence continuing correctly within the page
        // (here: "i", "ii") before resetting again on the next page.
        doc.add(new AreaBreak());
        doc.add(new Paragraph("Page 3: numbering restarts again")
                .setFontSize(20)
                .setFontColor(new DeviceRgb(60, 60, 150))
                .setMarginBottom(20));

        Footnote sixthFootnote = new Footnote(
                "The word 'espresso' comes from the Italian for 'pressed out' or 'expressed'.");
        sixthFootnote.setBorder(new SolidBorder(new DeviceRgb(140, 220, 160), 1));
        sixthFootnote.setBackgroundColor(new DeviceRgb(235, 250, 238));

        FootnoteAnchor sixthAnchor = new FootnoteAnchor(sixthFootnote);
        sixthAnchor.setFontColor(new DeviceRgb(40, 150, 80));

        Paragraph p6 = new Paragraph()
                .add("Espresso")
                .add(sixthAnchor)
                .add(" is the base for many other popular coffee drinks, like cappuccino and latte.");
        doc.add(p6);

        doc.close();
    }
}