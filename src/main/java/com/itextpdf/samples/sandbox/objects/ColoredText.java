package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.File;
import java.io.IOException;

public class ColoredText {
    public static final String DEST = "./target/sandbox/objects/colored_text.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColoredText().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Text redText = new Text("This text is red. ")
                .setFontColor(ColorConstants.RED)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA));
        Text blueText = new Text("This text is blue and bold. ")
                .setFontColor(ColorConstants.BLUE)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD));
        Text greenText = new Text("This text is green and italic. ")
                .setFontColor(ColorConstants.GREEN)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE));

        Paragraph p1 = new Paragraph(redText).setMargin(0);
        doc.add(p1);
        Paragraph p2 = new Paragraph().setMargin(0);
        p2.add(blueText);
        p2.add(greenText);
        doc.add(p2);

        new Canvas(new PdfCanvas(pdfDoc.getLastPage()), new Rectangle(36, 600, 108, 160))
                .add(p1)
                .add(p2);

        doc.close();
    }
}
