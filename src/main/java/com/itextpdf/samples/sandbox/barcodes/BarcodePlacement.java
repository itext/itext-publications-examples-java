package com.itextpdf.samples.sandbox.barcodes;

import com.itextpdf.barcodes.BarcodePDF417;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class BarcodePlacement {
    public static final String DEST = "./target/sandbox/barcodes/barcode_placement.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new BarcodePlacement().manipulatePdf(DEST);
    }

    public Image createBarcode(float xScale, float yScale, PdfDocument pdfDoc) {
        BarcodePDF417 barcode = new BarcodePDF417();
        barcode.setCode("BarcodePDF417 barcode");
        PdfFormXObject barcodeObject = barcode.createFormXObject(ColorConstants.BLACK, pdfDoc);
        Image barcodeImage = new Image(barcodeObject).scale(xScale, yScale);
        return barcodeImage;
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Image img = createBarcode(1, 1, pdfDoc);
        doc.add(new Paragraph(String.format("This barcode measures %s by %s user units",
                img.getImageScaledWidth(), img.getImageScaledHeight())));
        doc.add(img);

        img = createBarcode(3, 3, pdfDoc);
        doc.add(new Paragraph(String.format("This barcode measures %s by %s user units",
                img.getImageScaledWidth(), img.getImageScaledHeight())));
        doc.add(img);

        img = createBarcode(3, 1, pdfDoc);
        doc.add(new Paragraph(String.format("This barcode measures %s by %s user units",
                img.getImageScaledWidth(), img.getImageScaledHeight())));
        doc.add(img);

        doc.close();
    }
}
