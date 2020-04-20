package com.itextpdf.samples.sandbox.layout;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.File;
import java.io.IOException;

public class ParagraphTextWithStyle {
    public static final String DEST = "./target/sandbox/layout/paragraphTextWithStyle.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ParagraphTextWithStyle().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        PdfFont code = PdfFontFactory.createFont(StandardFonts.COURIER);

        Style style = new Style()
                .setFont(code)
                .setFontSize(14)
                .setFontColor(ColorConstants.RED)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY);

        Paragraph paragraph = new Paragraph()
                .add("In this example, named ")
                .add(new Text("HelloWorldStyles").addStyle(style))
                .add(", we experiment with some text in ")
                .add(new Text("code style").addStyle(style))
                .add(".");

        try (Document document = new Document(pdf)) {
            document.add(paragraph);
        }
    }
}
