package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ChangeInfoDictionary {
    public static final String DEST = "./target/sandbox/stamper/change_info_dictionary.pdf";
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ChangeInfoDictionary().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfDocumentInfo info = pdfDoc.getDocumentInfo();

        Map<String, String> newInfo = new HashMap<>();
        newInfo.put("Special Character: \u00e4", "\u00e4");

        StringBuilder buf = new StringBuilder();
        buf.append((char) 0xc3);
        buf.append((char) 0xa4);
        newInfo.put(buf.toString(), "\u00e4");

        info.setMoreInfo(newInfo);

        pdfDoc.close();
    }
}
