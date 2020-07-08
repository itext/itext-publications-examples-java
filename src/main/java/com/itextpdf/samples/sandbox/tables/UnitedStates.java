package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;

public class UnitedStates {
    public static final String DATA = "./src/main/resources/data/united_states.csv";

    public static final String DEST = "./target/sandbox/tables/united_states.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new UnitedStates().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4.rotate());

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        Table table = new Table(UnitValue.createPercentArray(new float[] {14, 6, 12, 16, 12, 12, 12, 12, 6}));
        try (BufferedReader br = new BufferedReader(new FileReader(DATA))) {
            String line = br.readLine();

            // The last argument defines which cell will be added: a header or the usual one
            addRowToTable(table, line, bold, true);
            while ((line = br.readLine()) != null) {
                addRowToTable(table, line, font, false);
            }
        }

        doc.add(table);

        doc.close();
    }

    private static void addRowToTable(Table table, String line, PdfFont font, boolean isHeader) {

        // Parses string line with specified delimiter
        StringTokenizer tokenizer = new StringTokenizer(line, ";");

        // Creates cells according to parsed csv line
        while (tokenizer.hasMoreTokens()) {
            Cell cell = new Cell().add(new Paragraph(tokenizer.nextToken()).setFont(font));

            if (isHeader) {
                table.addHeaderCell(cell);
            } else {
                table.addCell(cell);
            }
        }
    }
}
