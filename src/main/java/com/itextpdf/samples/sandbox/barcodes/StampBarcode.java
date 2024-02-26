package com.itextpdf.samples.sandbox.barcodes;

import com.itextpdf.barcodes.BarcodeEAN;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;

public class StampBarcode {
    public static final String DEST = "./target/sandbox/barcodes/stamp_barcode.pdf";
    public static final String SRC = "./src/main/resources/pdfs/superman.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new StampBarcode().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            PdfPage pdfPage = pdfDoc.getPage(i);
            Rectangle pageSize = pdfPage.getPageSize();
            float x = pageSize.getLeft() + 10;
            float y = pageSize.getTop() - 50;
            BarcodeEAN barcode = new BarcodeEAN(pdfDoc);
            barcode.setCodeType(BarcodeEAN.EAN8);
            barcode.setCode(createBarcodeNumber(i));

            PdfFormXObject barcodeXObject = barcode.createFormXObject(ColorConstants.BLACK, ColorConstants.BLACK, pdfDoc);
            PdfCanvas over = new PdfCanvas(pdfPage);
            over.addXObjectAt(barcodeXObject, x, y);
        }

        pdfDoc.close();
    }

    private static String createBarcodeNumber(int i) {
        String barcodeNumber = String.valueOf(i);
        barcodeNumber = "00000000".substring(barcodeNumber.length()) + barcodeNumber;

        return barcodeNumber;
    }
}
