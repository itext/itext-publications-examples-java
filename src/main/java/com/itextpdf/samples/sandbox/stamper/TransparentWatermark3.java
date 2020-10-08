package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;

public class TransparentWatermark3 {
    public static final String DEST = "./target/sandbox/stamper/transparent_watermark3.pdf";
    public static final String IMG = "./src/main/resources/img/itext.png";
    public static final String SRC = "./src/main/resources/pdfs/pages.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TransparentWatermark3().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font = PdfFontFactory.createFont(FontProgramFactory.createFont(StandardFonts.HELVETICA));
        Paragraph paragraph = new Paragraph("My watermark (text)").setFont(font).setFontSize(30);
        ImageData img = ImageDataFactory.create(IMG);

        float w = img.getWidth();
        float h = img.getHeight();

        PdfExtGState gs1 = new PdfExtGState().setFillOpacity(0.5f);

        // Implement transformation matrix usage in order to scale image
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            PdfPage pdfPage = pdfDoc.getPage(i);
            Rectangle pageSize = pdfPage.getPageSize();
            float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
            float y = (pageSize.getTop() + pageSize.getBottom()) / 2;
            PdfCanvas over = new PdfCanvas(pdfPage);
            over.saveState();
            over.setExtGState(gs1);
            if (i % 2 == 1) {
                doc.showTextAligned(paragraph, x, y, i, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
            } else {
                over.addImageWithTransformationMatrix(img, w, 0, 0, h, x - (w / 2), y - (h / 2), true);
            }
            over.restoreState();
        }

        doc.close();
    }
}
