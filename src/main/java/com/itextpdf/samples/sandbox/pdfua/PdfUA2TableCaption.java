package com.itextpdf.samples.sandbox.pdfua;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfUAConformance;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.CaptionSide;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.pdfua.PdfUAConfig;
import com.itextpdf.pdfua.PdfUADocument;

import java.io.File;
import java.io.IOException;

public class PdfUA2TableCaption {

    public static final String DEST = "./target/sandbox/pdfua2/pdf_ua_table_caption.pdf";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PdfUA2TableCaption().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) {
        try (PdfDocument pdfDocument = new PdfUADocument(new PdfWriter(dest, new WriterProperties().setPdfVersion(
                PdfVersion.PDF_2_0)),
                new PdfUAConfig(PdfUAConformance.PDF_UA_2, "Pdf/UA2 tilte", "en-US"))) {
            Document document = new Document(pdfDocument);
            PdfFont font = PdfFontFactory.createFont(FONT,
                    "WinAnsi", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
            document.setFont(font);

            Table tableCaptionBottom = new Table(new float[]{1, 2, 2});
            Paragraph caption = new Paragraph("This is Caption to the bottom").setBackgroundColor(ColorConstants.GREEN);
            tableCaptionBottom.setCaption(new Div().add(caption), CaptionSide.BOTTOM);
            tableCaptionBottom.setHorizontalAlignment(HorizontalAlignment.CENTER);
            tableCaptionBottom.setWidth(200);
            tableCaptionBottom.addHeaderCell("ID");
            tableCaptionBottom.addHeaderCell("Name");
            tableCaptionBottom.addHeaderCell("Age");

            for (int i = 1; i <= 5; i++) {
                tableCaptionBottom.addCell("ID: " + i);
                tableCaptionBottom.addCell("Name " + i);
                tableCaptionBottom.addCell("Age: " + (20 + i));
            }
            document.add(tableCaptionBottom);

            Table captionTopTable = new Table(new float[]{1, 2, 3});
            captionTopTable.setCaption(new Div().add(new Paragraph("Caption on top")));

            captionTopTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
            captionTopTable.setWidth(200);
            captionTopTable.addHeaderCell("ID");
            captionTopTable.addHeaderCell("Name");
            captionTopTable.addHeaderCell("Age");

            for (int i = 1; i <= 5; i++) {
                captionTopTable.addCell("ID: " + i);
                captionTopTable.addCell("Name " + i);
                captionTopTable.addCell("Age: " + (20 + i));
            }

            document.add(captionTopTable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
