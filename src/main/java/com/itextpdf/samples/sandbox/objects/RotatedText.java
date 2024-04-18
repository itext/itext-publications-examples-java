package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.barcodes.BarcodeEAN;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RotatedText {
    public static final String DEST = "./target/sandbox/objects/rotated_text.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RotatedText().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(60, 140));
        doc.setMargins(5, 5, 5, 5);

        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        Paragraph p1 = new Paragraph();
        p1.add(new Text("23").setFont(boldFont).setFontSize(12));
        p1.add(new Text("000").setFont(boldFont).setFontSize(6));
        doc.add(p1);

        Paragraph p2 = new Paragraph("T.T.C.").setFont(regularFont).setFontSize(6);
        p2.setTextAlignment(TextAlignment.RIGHT);
        doc.add(p2);

        BarcodeEAN barcode = new BarcodeEAN(pdfDoc);
        barcode.setCodeType(BarcodeEAN.EAN8);
        barcode.setCode("12345678");

        Rectangle rect = barcode.getBarcodeSize();
        PdfFormXObject formXObject = new PdfFormXObject(new Rectangle(rect.getWidth(), rect.getHeight() + 10));
        PdfCanvas pdfCanvas = new PdfCanvas(formXObject, pdfDoc);
        new Canvas(pdfCanvas, new Rectangle(rect.getWidth(), rect.getHeight() + 10))
                .showTextAligned(new Paragraph("DARK GRAY").setFont(regularFont).setFontSize(6), 0, rect.getHeight() + 2, TextAlignment.LEFT);
        barcode.placeBarcode(pdfCanvas, ColorConstants.BLACK, ColorConstants.BLACK);

        Image image = new Image(formXObject);
        image.setRotationAngle(Math.toRadians(90));
        image.setAutoScale(true);
        doc.add(image);

        Paragraph p3 = new Paragraph("SMALL").setFont(regularFont).setFontSize(6);
        p3.setTextAlignment(TextAlignment.CENTER);
        doc.add(p3);

        doc.close();
    }
}
