package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.Property;

import java.io.File;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashSet;
import java.util.Set;

public class FontTest {
    public static final String DEST = "./target/sandbox/fonts/font_test.pdf";

    public static final String TEXT = "Quick brown fox jumps over the lazy dog; 0123456789";

    // "Nikogaršnja zemlja"
    public static final String CP1250 = "Nikogar\u0161nja zemlja";

    // "Я люблю тебя"
    public static final String CP1251 = "\u042f \u043b\u044e\u0431\u043b\u044e \u0442\u0435\u0431\u044f";

    // "Un long dimanche de fiançailles"
    public static final String CP1252 = "Un long dimanche de fian\u00e7ailles";

    // "Νύφες"
    public static final String CP1253 = "\u039d\u03cd\u03c6\u03b5\u03c2";

    // "十锊埋伏"
    public static final String CHINESE = "\u5341\u950a\u57cb\u4f0f";

    // "誰も知らない"
    public static final String JAPANESE = "\u8ab0\u3082\u77e5\u3089\u306a\u3044";

    // "빈집"
    public static final String KOREAN = "\ube48\uc9d1";

    public static final String[] FONTS = {
            "./src/main/resources/font/cmr10.afm",
            "./src/main/resources/font/cmr10.pfb",
            "./src/main/resources/font/cmr10.pfm",
            "./src/main/resources/font/EBGaramond12-Italic.ttf",
            "./src/main/resources/font/EBGaramond12-Regular.ttf",
            "./src/main/resources/font/FreeSans.ttf",
            "./src/main/resources/font/FreeSansBold.ttf",
            "./src/main/resources/font/NotoSans-Bold.ttf",
            "./src/main/resources/font/NotoSans-BoldItalic.ttf",
            "./src/main/resources/font/NotoSansCJKjp-Regular.otf",
            "./src/main/resources/font/NotoSansCJKkr-Regular.otf",
            "./src/main/resources/font/NotoSansCJKsc-Regular.otf"
    };

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FontTest().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        for (String font : FONTS) {
            PdfFontFactory.register(font);
        }

        PdfFont defaultFont = doc.getDefaultProperty(Property.FONT);

        Set<String> fonts = new HashSet<String>(FontProgramFactory.getRegisteredFonts());
        for (String fontname : fonts) {
            System.out.println(fontname);
            PdfFont font;
            try {
                font = PdfFontFactory.createRegisteredFont(fontname, PdfEncodings.IDENTITY_H);
            } catch (UnsupportedCharsetException e) {
                doc.add(new Paragraph(String.format("The font %s doesn't have unicode support: %s",
                        fontname, e.getMessage())));
                continue;
            }

            doc.add(new Paragraph(String.format("Postscript name for %s: %s", fontname,
                    font.getFontProgram().getFontNames().getFontName())));

            doc.setFont(font);
            showFontInfo(doc);

            // Restore the default document font
            doc.setFont(defaultFont);
        }

        doc.close();
    }

    private static void showFontInfo(Document doc) {
        doc.add(new Paragraph(TEXT));
        doc.add(new Paragraph(String.format("CP1250: %s", CP1250)));
        doc.add(new Paragraph(String.format("CP1251: %s", CP1251)));
        doc.add(new Paragraph(String.format("CP1252: %s", CP1252)));
        doc.add(new Paragraph(String.format("CP1253: %s", CP1253)));
        doc.add(new Paragraph(String.format("CHINESE: %s", CHINESE)));
        doc.add(new Paragraph(String.format("JAPANESE: %s", JAPANESE)));
        doc.add(new Paragraph(String.format("KOREAN: %s", KOREAN)));
    }
}
