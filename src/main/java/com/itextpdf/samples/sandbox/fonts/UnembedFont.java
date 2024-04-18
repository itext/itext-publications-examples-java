package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class UnembedFont {
    public static final String DEST = "./target/sandbox/fonts/unembed_font.pdf";

    public static final String SRC = "./target/sandbox/fonts/withSerifFont.pdf";
    public static final String FONT = "./src/main/resources/font/PT_Serif-Web-Regular.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new UnembedFont().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {

        // Create a pdf file with an embedded font in memory.
        byte[] sourcePdfBytes = createSourcePdf().toByteArray();
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(sourcePdfBytes)),
                new PdfWriter(dest));

        for (int i = 0; i < pdfDoc.getNumberOfPdfObjects(); i++) {
            PdfObject obj = pdfDoc.getPdfObject(i);

            // Skip all objects that aren't a dictionary
            if (obj == null || !obj.isDictionary()) {
                continue;
            }

            // Process all dictionaries
            unembedTTF((PdfDictionary) obj);
        }

        pdfDoc.close();
    }

    /*
     * Creates a PDF with an embedded font.
     */
    public ByteArrayOutputStream createSourcePdf() throws Exception {
        ByteArrayOutputStream resultFile = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(resultFile));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_EMBEDDED);
        doc.add(new Paragraph("This is a test with Times New Roman.").setFont(font));

        doc.close();
        return resultFile;
    }

    /*
     * Unembeds a font dictionary.
     */
    public void unembedTTF(PdfDictionary dict) {

        // Ignore all dictionaries that aren't font dictionaries
        if (!PdfName.Font.equals(dict.getAsName(PdfName.Type))) {
            return;
        }

        // Only TTF fonts should be removed
        if (dict.getAsDictionary(PdfName.FontFile2) != null) {
            return;
        }

        // Check if a subset was used (in which case we remove the prefix)
        PdfName baseFont = dict.getAsName(PdfName.BaseFont);
        if (baseFont.getValue().getBytes()[6] == '+') {
            baseFont = new PdfName(baseFont.getValue().substring(7));
            dict.put(PdfName.BaseFont, baseFont);
        }

        // Check if there's a font descriptor
        PdfDictionary fontDescriptor = dict.getAsDictionary(PdfName.FontDescriptor);
        if (fontDescriptor == null) {
            return;
        }

        // Replace the fontname and remove the font file
        fontDescriptor.put(PdfName.FontName, baseFont);
        fontDescriptor.remove(PdfName.FontFile2);
    }
}
