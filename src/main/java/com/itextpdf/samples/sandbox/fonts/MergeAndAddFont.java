package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.CompressionConstants;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class MergeAndAddFont {
    public static final String FONT = "./src/main/resources/font/GravitasOne.ttf";

    public static final String[] FILE_A = {
            "./target/sandbox/fonts/testA1.pdf",
            "./target/sandbox/fonts/testA2.pdf",
            "./target/sandbox/fonts/testA3.pdf"
    };

    public static final String[] FILE_B = {
            "./target/sandbox/fonts/testB1.pdf",
            "./target/sandbox/fonts/testB2.pdf",
            "./target/sandbox/fonts/testB3.pdf"
    };

    public static final String[] FILE_C = {
            "./target/sandbox/fonts/testC1.pdf",
            "./target/sandbox/fonts/testC2.pdf",
            "./target/sandbox/fonts/testC3.pdf"
    };

    public static final String[] CONTENT = {
            "abcdefgh", "ijklmnopq", "rstuvwxyz"
    };

    public static final Map<String, String> DEST_NAMES = new HashMap<>();

    static {
        DEST_NAMES.put("A1", "testA_merged1.pdf");
        DEST_NAMES.put("A2", "testA_merged2.pdf");
        DEST_NAMES.put("B1", "testB_merged1.pdf");
        DEST_NAMES.put("B2", "testB_merged2.pdf");
        DEST_NAMES.put("C1", "testC_merged1.pdf");
        DEST_NAMES.put("C2", "testC_merged2.pdf");
    }

    public static final String DEST = "./target/sandbox/fonts/";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.mkdirs();

        new MergeAndAddFont().manipulatePdf(DEST);
    }

    public void createPdf(String filename, String text, EmbeddingStrategy embeddingStrategy, boolean subset) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));
        Document doc = new Document(pdfDoc);

        // The 3rd parameter indicates whether the font is to be embedded into the target document.
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, embeddingStrategy);

        // When set to true, only the used glyphs will be included in the font.
        // When set to false, the full font will be included and all subset ranges will be removed.
        font.setSubset(subset);
        doc.add(new Paragraph(text).setFont(font));

        doc.close();
    }

    public void mergeFiles(String[] files, String result, boolean isSmartModeOn) throws IOException {
        PdfWriter writer = new PdfWriter(result);

        // In smart mode when resources (such as fonts, images,...) are encountered,
        // a reference to these resources is saved in a cache and can be reused.
        // This mode reduces the file size of the resulting PDF document.
        writer.setSmartMode(isSmartModeOn);
        PdfDocument pdfDoc = new PdfDocument(writer);

        // This method initializes an outline tree of the document and sets outline mode to true.
        pdfDoc.initializeOutlines();

        for (int i = 0; i < files.length; i++) {
            PdfDocument addedDoc = new PdfDocument(new PdfReader(files[i]));
            addedDoc.copyPagesTo(1, addedDoc.getNumberOfPages(), pdfDoc);
            addedDoc.close();
        }

        pdfDoc.close();
    }

    protected void manipulatePdf(String dest) throws Exception {
        for (int i = 0; i < FILE_A.length; i++) {

            // Create pdf files with font, which will be embedded into the target document,
            // and only the used glyphs will be included in the font.
            createPdf(FILE_A[i], CONTENT[i], EmbeddingStrategy.FORCE_EMBEDDED, true);
        }

        mergeFiles(FILE_A, dest + DEST_NAMES.get("A1"), false);
        mergeFiles(FILE_A, dest + DEST_NAMES.get("A2"), true);

        for (int i = 0; i < FILE_B.length; i++) {

            // Create pdf files with font, which will embedded into the target document.
            // Full font will be included and all subset ranges will be removed
            createPdf(FILE_B[i], CONTENT[i], EmbeddingStrategy.FORCE_EMBEDDED, false);
        }

        mergeFiles(FILE_B, dest + DEST_NAMES.get("B1"), false);
        mergeFiles(FILE_B, dest + DEST_NAMES.get("B2"), true);

        for (int i = 0; i < FILE_C.length; i++) {

            // Create pdf files with font, which won't be embedded into the target document.
            // Full font will be included and all subset ranges will be removed
            createPdf(FILE_C[i], CONTENT[i], EmbeddingStrategy.FORCE_NOT_EMBEDDED, false);
        }

        mergeFiles(FILE_C, dest + DEST_NAMES.get("C1"), true);

        // Embed the font manually
        embedFont(dest + DEST_NAMES.get("C1"), FONT, dest + DEST_NAMES.get("C2"));
    }

    protected void embedFont(String merged, String fontfile, String result) throws IOException {

        // The font file
        RandomAccessFile raf = new RandomAccessFile(fontfile, "r");
        byte fontbytes[] = new byte[(int) raf.length()];
        raf.readFully(fontbytes);
        raf.close();

        // Create a new stream for the font file
        PdfStream stream = new PdfStream(fontbytes);
        stream.setCompressionLevel(CompressionConstants.DEFAULT_COMPRESSION);
        stream.put(PdfName.Length1, new PdfNumber(fontbytes.length));

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(merged), new PdfWriter(result));
        int numberOfPdfObjects = pdfDoc.getNumberOfPdfObjects();

        // Search for the font dictionary
        for (int i = 0; i < numberOfPdfObjects; i++) {
            PdfObject object = pdfDoc.getPdfObject(i);
            if (object == null || !object.isDictionary()) {
                continue;
            }

            PdfDictionary fontDictionary = (PdfDictionary) object;
            PdfFont font = PdfFontFactory.createFont(fontfile, PdfEncodings.WINANSI);
            PdfName fontname = new PdfName(font.getFontProgram().getFontNames().getFontName());
            if (PdfName.FontDescriptor.equals(fontDictionary.get(PdfName.Type))
                    && fontname.equals(fontDictionary.get(PdfName.FontName))) {

                // Embed the passed font to the pdf document
                fontDictionary.put(PdfName.FontFile2, stream.makeIndirect(pdfDoc).getIndirectReference());
            }
        }

        pdfDoc.close();
    }
}
