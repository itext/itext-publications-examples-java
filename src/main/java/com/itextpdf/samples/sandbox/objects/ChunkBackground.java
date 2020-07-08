package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.File;
import java.io.IOException;

public class ChunkBackground {
    public static final String DEST = "./target/sandbox/objects/chunk_background.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChunkBackground().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont f = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD, PdfEncodings.WINANSI);
        Text text = new Text("White text on red background")
                .setFont(f)
                .setFontSize(25.0f)
                .setFontColor(ColorConstants.WHITE)
                .setBackgroundColor(ColorConstants.RED);

        Paragraph p = new Paragraph(text);
        doc.add(p);

        doc.close();
    }
}
