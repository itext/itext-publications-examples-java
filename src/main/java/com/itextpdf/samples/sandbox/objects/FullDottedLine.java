package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class FullDottedLine {
    public static final String DEST = "./target/sandbox/objects/full_dotted_line.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FullDottedLine().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("Before dotted line"));
        doc.add(new LineSeparator(new CustomDottedLine(pdfDoc.getDefaultPageSize())));
        doc.add(new Paragraph("After dotted line"));

        doc.close();
    }


    protected class CustomDottedLine extends DottedLine {
        private Rectangle pageSize;

        public CustomDottedLine(Rectangle pageSize) {
            this.pageSize = pageSize;
        }

        @Override
        public void draw(PdfCanvas canvas, Rectangle drawArea) {
            // Dotted line from the left edge of the page to the right edge.
            super.draw(canvas, new Rectangle(pageSize.getLeft(), drawArea.getBottom(), pageSize.getWidth(), drawArea.getHeight()));
        }
    }
}
