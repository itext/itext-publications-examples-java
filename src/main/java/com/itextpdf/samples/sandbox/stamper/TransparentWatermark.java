package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfResources;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;

public class TransparentWatermark {
    public static final String DEST = "./target/sandbox/stamper/transparent_watermark.pdf";
    public static final String SRC = "./src/main/resources/pdfs/hero.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TransparentWatermark().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfCanvas under = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamBefore(), new PdfResources(), pdfDoc);
        PdfFont font = PdfFontFactory.createFont(FontProgramFactory.createFont(StandardFonts.HELVETICA));
        Paragraph paragraph = new Paragraph("This watermark is added UNDER the existing content")
                .setFont(font)
                .setFontSize(15);

        Canvas canvasWatermark1 = new Canvas(under, pdfDoc.getDefaultPageSize())
                .showTextAligned(paragraph, 297, 550, 1, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
        canvasWatermark1.close();
        PdfCanvas over = new PdfCanvas(pdfDoc.getFirstPage());
        over.setFillColor(ColorConstants.BLACK);
        paragraph = new Paragraph("This watermark is added ON TOP OF the existing content")
                .setFont(font)
                .setFontSize(15);

        Canvas canvasWatermark2 = new Canvas(over, pdfDoc.getDefaultPageSize())
                .showTextAligned(paragraph, 297, 500, 1, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
        canvasWatermark2.close();
        paragraph = new Paragraph("This TRANSPARENT watermark is added ON TOP OF the existing content")
                .setFont(font)
                .setFontSize(15);
        over.saveState();

        // Creating a dictionary that maps resource names to graphics state parameter dictionaries
        PdfExtGState gs1 = new PdfExtGState();
        gs1.setFillOpacity(0.5f);
        over.setExtGState(gs1);
        Canvas canvasWatermark3 = new Canvas(over, pdfDoc.getDefaultPageSize())
                .showTextAligned(paragraph, 297, 450, 1, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
        canvasWatermark3.close();
        over.restoreState();

        pdfDoc.close();
    }
}
