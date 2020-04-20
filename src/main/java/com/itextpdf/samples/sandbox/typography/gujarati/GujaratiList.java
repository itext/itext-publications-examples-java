package com.itextpdf.samples.sandbox.typography.gujarati;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class GujaratiList {

    public static final String DEST = "./target/sandbox/typography/GujaratiList.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new GujaratiList().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansGujarati-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Overwrite some default document font-related properties. From now on they will be used for all the elements
        // added to the document unless they are overwritten inside these elements
        document
                .setFont(font)
                .setFontSize(10);

        // રાજ્યમાં વસતા લોકો દ્વારા લેખનકાર્યમાં વપરાતી
        String text = "\u0AB0\u0ABE\u0A9C\u0ACD\u0AAF\u0AAE\u0ABE\u0A82\u0020\u0AB5\u0AB8\u0AA4\u0ABE\u0020\u0AB2\u0ACB" +
                "\u0A95\u0ACB\u0020\u0AA6\u0ACD\u0AB5\u0ABE\u0AB0\u0ABE\u0020\u0AB2\u0AC7\u0A96\u0AA8\u0A95\u0ABE\u0AB0" +
                "\u0ACD\u0AAF\u0AAE\u0ABE\u0A82\u0020\u0AB5\u0AAA\u0AB0\u0ABE\u0AA4\u0AC0";

        List list = new List();

        list
                .add(text)
                .add(text)
                .add(text);

        document.add(list);

        document.close();
    }
}

