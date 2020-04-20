package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class ColumnTextAscender {
    public static final String DEST = "./target/sandbox/objects/column_text_ascender.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColumnTextAscender().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Rectangle[] areas = new Rectangle[]{
                new Rectangle(50, 750, 200, 50),
                new Rectangle(300, 750, 200, 50)
        };

        // For canvas usage one should create a page
        pdfDoc.addNewPage();
        for (Rectangle rect : areas) {
            new PdfCanvas(pdfDoc.getFirstPage()).setLineWidth(0.5f).setStrokeColor(ColorConstants.RED).rectangle(rect).stroke();
        }

        doc.setRenderer(new ColumnDocumentRenderer(doc, areas));
        addColumn(doc, false);
        addColumn(doc, true);
        doc.close();
    }

    private static void addColumn(Document doc, boolean useAscender) {
        Paragraph p = new Paragraph("This text is added at the top of the column.");

        // setUseAscender (boolean) - this is the approach used in iText5.
        // We can change leading instead, the result will be the same
        if (useAscender) {
            p.setMargin(0);

            // The Leading is a spacing between lines of text
            p.setMultipliedLeading(1);
        }

        doc.add(p);
    }
}
