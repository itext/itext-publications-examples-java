package com.itextpdf.samples.htmlsamples.chapter06;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.styledxmlparser.resolver.font.BasicFontProvider;

public class C06E11_Internationalization {

    /**
     * An array with the paths to extra fonts.
     */
    public static final String[] FONTS = {
            "./src/main/resources/htmlsamples/fonts/noto/NotoSans-Regular.ttf",
            "./src/main/resources/htmlsamples/fonts/noto/NotoSans-Bold.ttf",
            "./src/main/resources/htmlsamples/fonts/noto/NotoSansCJKsc-Regular.otf",
            "./src/main/resources/htmlsamples/fonts/noto/NotoSansCJKjp-Regular.otf",
            "./src/main/resources/htmlsamples/fonts/noto/NotoSansCJKkr-Regular.otf",
            "./src/main/resources/htmlsamples/fonts/noto/NotoNaskhArabic-Regular.ttf",
            "./src/main/resources/htmlsamples/fonts/noto/NotoSansHebrew-Regular.ttf"
    };

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch06/fonts_i18n.pdf";

    /**
     * The path to the source HTML file.
     */
    public static final String SRC = "./src/main/resources/htmlsamples/html/fonts_i18n.html";

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
				+ "/itextkey-html2pdf_typography.json")) {
			LicenseKey.loadLicenseFile(license);
		}
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        C06E11_Internationalization app = new C06E11_Internationalization();
        app.createPdf(SRC, FONTS, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param src   the path to the source HTML file
     * @param fonts an array containing paths to extra fonts
     * @param dest  the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(String src, String[] fonts, String dest) throws IOException {
        ConverterProperties properties = new ConverterProperties();
        FontProvider fontProvider = new BasicFontProvider(false, false, false);
        for (String font : fonts) {
            FontProgram fontProgram = FontProgramFactory.createFont(font);
            fontProvider.addFont(fontProgram);
        }
        properties.setFontProvider(fontProvider);
        HtmlConverter.convertToPdf(new File(src), new File(dest), properties);
    }
}
