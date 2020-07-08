package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.property.TabAlignment;

import java.io.File;
import java.io.IOException;

public class DottedLineEnder {
    public static final String DEST = "./target/sandbox/objects/dotted_line_ender.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new DottedLineEnder().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph p = new Paragraph("Ends with dots ");
        p.addTabStops(new TabStop(523, TabAlignment.RIGHT, new DottedLine()));
        p.add(new Tab());
        doc.add(p);

        p = new Paragraph("This is a much longer paragraph that spans "
                + "several lines. The String used to create this paragraph "
                + "will be split automatically at the end of the line. The "
                + "final line of this paragraph will end in a dotted line. ");
        p.addTabStops(new TabStop(523, TabAlignment.LEFT, new DottedLine()));
        p.add(new Tab());
        doc.add(p);

        doc.close();
    }
}
