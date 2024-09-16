package com.itextpdf.samples.sandbox.pdfa;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfAConformance;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.pdfa.PdfADocument;
import com.itextpdf.test.pdfa.VeraPdfValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PdfA4 {

    public static final String DEST = "./target/sandbox/pdfa/pdf_a4.pdf";

    public static final String IMG = "./src/main/resources/img/hero.jpg";


    public static final String FONT = "./src/main/resources/font/OpenSans-Regular.ttf";


    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfA4().manipulatePdf(DEST);
    }


    public void manipulatePdf(String dest) throws IOException {
        //PDF/a-4 requires a PDF 2.0 document
        PdfWriter writer = new PdfWriter(dest, new WriterProperties().setPdfVersion(PdfVersion.PDF_2_0));
        //Grab the image color matching profile
        InputStream inputStream = new FileInputStream("./src/main/resources/data/sRGB_CS_profile.icm");
        //Create the PDF/a-4 document by instantiating a PdfADocument object and passing the PDF/a-4 conformance
        PdfADocument pdfDocument = new PdfADocument(writer, PdfAConformance.PDF_A_4, new PdfOutputIntent("Custom", "",
                null, "sRGB IEC61966-2.1", inputStream));

        Document document = new Document(pdfDocument);
        Image logoImage = new Image(ImageDataFactory.create(IMG));
        document.add(logoImage);


        // PDF/A spec requires font to be embedded, iText will warn you if you do something against the PDF/A4 spec
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        Paragraph element = new Paragraph("Hello World")
                .setFont(font)
                .setFontSize(10);
        document.add(element);

        pdfDocument.close();
        assert null == new VeraPdfValidator().validate(dest);

    }

}
