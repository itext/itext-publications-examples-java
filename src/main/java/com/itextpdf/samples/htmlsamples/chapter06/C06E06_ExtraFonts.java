package com.itextpdf.samples.htmlsamples.chapter06;

import java.io.File;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.licensekey.LicenseKey;

public class C06E06_ExtraFonts {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch06/fonts_extrafonts.pdf";

    /**
     * The path to the source HTML file.
     */
    public static final String SRC = "./src/main/resources/htmlsamples/html/fonts_extra.html";

    /**
     * The path to a folder containing extra fonts.
     */
    public static final String FONTS = "./src/main/resources/htmlsamples/fonts/cardo/";

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        C06E06_ExtraFonts app = new C06E06_ExtraFonts();
        app.createPdf(SRC, FONTS, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param src   the path to the source HTML file
     * @param fonts the path to a font folder with extra fonts
     * @param dest  the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(String src, String fonts, String dest) throws IOException {
        ConverterProperties properties = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider();
        fontProvider.addDirectory(fonts);
        properties.setFontProvider(fontProvider);
        HtmlConverter.convertToPdf(new File(src), new File(dest), properties);
    }
}
