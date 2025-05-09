package com.itextpdf.samples.sandbox.pdfua;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfUAConformance;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.pdfua.PdfUAConfig;
import com.itextpdf.pdfua.PdfUADocument;

import java.io.File;
import java.io.IOException;

public class PdfUA2 {

    public static final String DEST = "./target/sandbox/pdfua2/pdf_ua.pdf";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PdfUA2().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) {
        try (PdfDocument pdfDocument = new PdfUADocument(
                new PdfWriter(dest, new WriterProperties().setPdfVersion(PdfVersion.PDF_2_0)),
                new PdfUAConfig(PdfUAConformance.PDF_UA_2, "Some title", "en-US"))) {
            Document document = new Document(pdfDocument);
            //Embed font
            PdfFont font = PdfFontFactory.createFont(FONT, "WinAnsi", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);

            document.setFont(font);
            Paragraph paragraph = new Paragraph("Hello PdfUA2");
            document.add(paragraph);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
