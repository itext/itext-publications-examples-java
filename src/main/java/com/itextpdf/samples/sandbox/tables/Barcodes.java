package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.barcodes.BarcodeEAN;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class Barcodes {
    public static final String DEST = "./target/sandbox/tables/barcodes.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Barcodes().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();

        for (int i = 0; i < 12; i++) {
            table.addCell(createBarcode(String.format("%08d", i), pdfDoc));
        }

        doc.add(table);

        doc.close();
    }

    private static Cell createBarcode(String code, PdfDocument pdfDoc) {
        BarcodeEAN barcode = new BarcodeEAN(pdfDoc);
        barcode.setCodeType(BarcodeEAN.EAN8);
        barcode.setCode(code);

        // Create barcode object to put it to the cell as image
        PdfFormXObject barcodeObject = barcode.createFormXObject(null, null, pdfDoc);
        Cell cell = new Cell().add(new Image(barcodeObject));
        cell.setPaddingTop(10);
        cell.setPaddingRight(10);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(10);

        return cell;
    }
}
