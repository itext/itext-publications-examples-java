/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

@Category(SampleTest.class)
public class MergeAndAddFont extends GenericTest {
    public static final String FONT =
            "./src/test/resources/font/GravitasOne.ttf";

    public static final String[] FILE_A = {
            "./target/test/resources/sandbox/fonts/testA1.pdf", "./target/test/resources/sandbox/fonts/testA2.pdf",
            "./target/test/resources/sandbox/fonts/testA3.pdf"
    };
    public static final String[] FILE_B = {
            "./target/test/resources/sandbox/fonts/testB1.pdf", "./target/test/resources/sandbox/fonts/testB2.pdf",
            "./target/test/resources/sandbox/fonts/testB3.pdf"
    };
    public static final String[] FILE_C = {
            "./target/test/resources/sandbox/fonts/testC1.pdf", "./target/test/resources/sandbox/fonts/testC2.pdf",
            "./target/test/resources/sandbox/fonts/testC3.pdf"
    };
    public static final String[] CONTENT = {
            "abcdefgh", "ijklmnopq", "rstuvwxyz"
    };
    public static final String MERGED_A1 =
            "./target/test/resources/sandbox/fonts/testA_merged1.pdf";
    public static final String MERGED_A2 =
            "./target/test/resources/sandbox/fonts/testA_merged2.pdf";
    public static final String MERGED_B1 =
            "./target/test/resources/sandbox/fonts/testB_merged1.pdf";
    public static final String MERGED_B2 =
            "./target/test/resources/sandbox/fonts/testB_merged2.pdf";
    public static final String MERGED_C1 =
            "./target/test/resources/sandbox/fonts/testC_merged1.pdf";
    public static final String MERGED_C2 =
            "./target/test/resources/sandbox/fonts/testC_merged2.pdf";

    // we will check via CompareTool only one result file.
    public static final String DEST =
            MERGED_C1;

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MergeAndAddFont().manipulatePdf(DEST);
    }

    public void createPdf(String filename, String text, boolean embedded, boolean subset) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));
        Document doc = new Document(pdfDoc);
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, embedded);
        font.setSubset(subset);
        doc.add(new Paragraph(text).setFont(font));
        doc.close();
    }

    public void mergeFiles(String[] files, String result, boolean isSmartModeOn) throws IOException {
        PdfWriter writer = new PdfWriter(result);
        writer.setSmartMode(isSmartModeOn);
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.initializeOutlines();
        for (int i = 0; i < files.length; i++) {
            PdfDocument addedDoc = new PdfDocument(new PdfReader(files[i]));
            addedDoc.copyPagesTo(1, addedDoc.getNumberOfPages(), pdfDoc);
            addedDoc.close();
        }
        pdfDoc.close();
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        MergeAndAddFont app = new MergeAndAddFont();

        for (int i = 0; i < FILE_A.length; i++) {
            app.createPdf(FILE_A[i], CONTENT[i], true, true);
        }
        app.mergeFiles(FILE_A, MERGED_A1, false);
        app.mergeFiles(FILE_A, MERGED_A2, true);

        for (int i = 0; i < FILE_B.length; i++) {
            app.createPdf(FILE_B[i], CONTENT[i], true, false);
        }
        app.mergeFiles(FILE_B, MERGED_B1, false);
        app.mergeFiles(FILE_B, MERGED_B2, true);

        for (int i = 0; i < FILE_C.length; i++) {
            app.createPdf(FILE_C[i], CONTENT[i], false, false);
        }
        app.mergeFiles(FILE_C, MERGED_C1, true);
        app.embedFont(MERGED_C1, FONT, MERGED_C2);
    }

    protected void embedFont(String merged, String fontfile, String result) throws IOException {
        // the font file
        RandomAccessFile raf = new RandomAccessFile(fontfile, "r");
        byte fontbytes[] = new byte[(int) raf.length()];
        raf.readFully(fontbytes);
        raf.close();
        // create a new stream for the font file
        PdfStream stream = new PdfStream(fontbytes);
        stream.setCompressionLevel(CompressionConstants.DEFAULT_COMPRESSION);
        stream.put(PdfName.Length1, new PdfNumber(fontbytes.length));
        // create a reader object
        PdfObject object;
        PdfDictionary font;
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(merged), new PdfWriter(result));
        PdfName fontname = new PdfName(PdfFontFactory.createFont(fontfile, PdfEncodings.WINANSI)
                .getFontProgram().getFontNames().getFontName());
        int n = pdfDoc.getNumberOfPdfObjects();
        for (int i = 0; i < n; i++) {
            object = pdfDoc.getPdfObject(i);
            if (object == null || !object.isDictionary()) {
                continue;
            }
            font = (PdfDictionary) object;
            if (PdfName.FontDescriptor.equals(font.get(PdfName.Type))
                    && fontname.equals(font.get(PdfName.FontName))) {
                font.put(PdfName.FontFile2, stream.makeIndirect(pdfDoc).getIndirectReference());
            }
        }
        pdfDoc.close();
    }
}
