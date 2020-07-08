package com.itextpdf.samples.sandbox.pdfa;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.pdfa.PdfADocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class HelloPdfA2a {
    public static final String DEST = "./target/sandbox/pdfa/hello_pdf_a_2a.pdf";

    public static final String FONT = "./src/main/resources/font/OpenSans-Regular.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new HelloPdfA2a().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, XMPException {
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

        FileInputStream fileStream = new FileInputStream("./src/main/resources/data/sRGB_CS_profile.icm");

        PdfADocument pdfDoc = new PdfADocument(new PdfWriter(dest), PdfAConformanceLevel.PDF_A_2A,
                new PdfOutputIntent("Custom", "",
                        null, "sRGB IEC61966-2.1", fileStream));

        Document document = new Document(pdfDoc);

        // Specifies that document should contain tag structure
        pdfDoc.setTagged();
        pdfDoc.getCatalog().setLang(new PdfString("en-us"));

        Paragraph p = new Paragraph("Hello World!").setFont(font).setFontSize(10);
        document.add(p);
        document.close();
    }
}
