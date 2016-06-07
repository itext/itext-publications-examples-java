/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.pdfa;


import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.pdfa.PdfADocument;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.*;
import java.util.StringTokenizer;

@Category(SampleTest.class)
public class PdfA3 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/pdfa/pdf_a3.pdf";
    public static final String BOLD = "./src/test/resources/font/OpenSans-Bold.ttf";
    public static final String DATA = "./src/test/resources/data/united_states.csv";
    public static final String FONT = "./src/test/resources/font/OpenSans-Regular.ttf";


    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PdfA3().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, XMPException {
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        PdfFont bold = PdfFontFactory.createFont(BOLD, PdfEncodings.IDENTITY_H);
        InputStream is = new FileInputStream("./src/test/resources/data/sRGB_CS_profile.icm");
        PdfADocument pdfDoc = new PdfADocument(new PdfWriter(dest), PdfAConformanceLevel.PDF_A_3B,
                new PdfOutputIntent("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", is));

        Document document = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        PdfDictionary parameters = new PdfDictionary();
        parameters.put(PdfName.ModDate, new PdfDate().getPdfObject());

        PdfFileSpec fileSpec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, DATA.getBytes(), "united_states.csv",
                "united_states.csv", new PdfName("text/csv"), parameters, PdfName.Data, false);
        fileSpec.put(new PdfName("AFRelationship"), new PdfName("Data"));

        pdfDoc.addFileAttachment("united_states.csv", fileSpec);
        PdfArray array = new PdfArray();
        array.add(fileSpec.getPdfObject().getIndirectReference());
        pdfDoc.getCatalog().put(new PdfName("AF"), array);
        Table table = new Table(new float[]{4, 1, 3, 4, 3, 3, 3, 3, 1});
        table.setWidthPercent(100);
        BufferedReader br = new BufferedReader(new FileReader(DATA));
        String line = br.readLine();
        process(table, line, bold, 10, true);
        while ((line = br.readLine()) != null) {
            process(table, line, font, 10, false);
        }
        br.close();
        document.add(table);
        document.close();
    }

    public void process(Table table, String line, PdfFont font, int fontSize, boolean isHeader) {
        StringTokenizer tokenizer = new StringTokenizer(line, ";");
        while (tokenizer.hasMoreTokens()) {
            if (isHeader) {
                table.addHeaderCell(new Paragraph(tokenizer.nextToken()).setFont(font).setFontSize(fontSize));
            } else {
                table.addCell(new Paragraph(tokenizer.nextToken()).setFont(font).setFontSize(fontSize));
            }
        }
    }
}
