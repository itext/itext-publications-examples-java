package com.itextpdf.samples.sandbox.pdfa;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDate;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.pdfa.PdfADocument;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;
import java.io.InputStream;

public class PdfA3 {
    public static final String DEST = "./target/sandbox/pdfa/pdf_a3.pdf";

    public static final String BOLD = "./src/main/resources/font/OpenSans-Bold.ttf";

    public static final String DATA = "./src/main/resources/data/united_states.csv";

    public static final String FONT = "./src/main/resources/font/OpenSans-Regular.ttf";


    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfA3().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, XMPException {
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        PdfFont bold = PdfFontFactory.createFont(BOLD, PdfEncodings.IDENTITY_H);

        InputStream inputStream = new FileInputStream("./src/main/resources/data/sRGB_CS_profile.icm");
        PdfADocument pdfDoc = new PdfADocument(new PdfWriter(dest), PdfAConformanceLevel.PDF_A_3B,
                new PdfOutputIntent("Custom", "",
                        null, "sRGB IEC61966-2.1", inputStream));

        Document document = new Document(pdfDoc, PageSize.A4.rotate());

        PdfDictionary parameters = new PdfDictionary();
        parameters.put(PdfName.ModDate, new PdfDate().getPdfObject());

        // Embeds file to the document
        PdfFileSpec fileSpec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, Files.readAllBytes(Paths.get(DATA)),
                "united_states.csv", "united_states.csv", new PdfName("text/csv"), parameters, PdfName.Data);
        pdfDoc.addAssociatedFile("united_states.csv", fileSpec);

        Table table = new Table(UnitValue.createPercentArray(
                new float[]{4, 1, 3, 4, 3, 3, 3, 3, 1})).useAllAvailableWidth();

        try (BufferedReader br = new BufferedReader(new FileReader(DATA))) {

            // Reads content of csv file
            String line = br.readLine();

            process(table, line, bold, 10, true);
            while ((line = br.readLine()) != null) {
                process(table, line, font, 10, false);
            }
        }

        document.add(table);
        document.close();
    }

    public void process(Table table, String line, PdfFont font, int fontSize, boolean isHeader) {

        // Parses csv string line with specified delimiter
        StringTokenizer tokenizer = new StringTokenizer(line, ";");

        while (tokenizer.hasMoreTokens()) {
            Paragraph content = new Paragraph(tokenizer.nextToken()).setFont(font).setFontSize(fontSize);

            if (isHeader) {
                table.addHeaderCell(content);
            } else {
                table.addCell(content);
            }
        }
    }
}
