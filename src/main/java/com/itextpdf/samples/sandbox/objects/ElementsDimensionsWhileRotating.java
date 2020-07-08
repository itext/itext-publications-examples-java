package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class ElementsDimensionsWhileRotating {
    public static final String DEST = "./target/sandbox/objects/elementsDimensionsWhileRotating.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ElementsDimensionsWhileRotating().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDocument);

        String bigText = "Hello. I am a fairly long paragraph. I really want you to process me correctly."
                + " You heard that? Correctly!!! Even if you will have to wrap me.";

        Style rotatedStyle = new Style()
                .setPadding(0)
                .setMargin(0)
                .setBorder(new SolidBorder(ColorConstants.BLUE, 2))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY);

        Style contentStyle = new Style()
                .setPadding(0)
                .setMargin(0)
                .setBorder(new SolidBorder(ColorConstants.GREEN, 2))
                .setWidth(400);

        LineSeparator line = new LineSeparator(new SolidLine(1));

        //width of rotated element isn't set, so it's width will be set automatically
        Div contentDiv = new Div().addStyle(contentStyle);
        contentDiv.add(new Paragraph("Short paragraph").addStyle(rotatedStyle).setRotationAngle(Math.PI * 3 / 8));
        contentDiv.add(line);

        contentDiv.add(new Paragraph(bigText).addStyle(rotatedStyle).setRotationAngle(Math.PI * 3 / 8));
        contentDiv.add(line);

        contentDiv.add(new Paragraph(bigText).addStyle(rotatedStyle).setRotationAngle(Math.PI / 30));
        doc.add(contentDiv);

        doc.add(new AreaBreak());

        //fixed width of rotated elements, so the content inside will be located according set width
        contentDiv = new Div().addStyle(contentStyle).setWidth(200);

        contentDiv.add(new Paragraph(bigText).addStyle(rotatedStyle).setWidth(400).setRotationAngle(Math.PI / 2));
        doc.add(contentDiv);

        doc.add(new Paragraph(bigText).addStyle(rotatedStyle).setWidth(800).setRotationAngle(Math.PI / 30));
        doc.close();
    }
}
