package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class UnderlineWithDottedLine {
    public static final String DEST = "./target/sandbox/objects/underline_with_dotted_line.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new UnderlineWithDottedLine().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("This line will be underlined with a dotted line.").setMarginBottom(0));
        doc.add(new LineSeparator(new DottedLine(1, 2)).setMarginTop(-4));

        doc.close();
    }

}
