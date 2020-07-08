package com.itextpdf.samples.sandbox.barcodes;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class BarcodeInTable {
    public static final String DEST = "./target/sandbox/barcodes/barcode_in_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new BarcodeInTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        String code = "675-FH-A12";

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.addCell("Change baseline:");

        Barcode128 code128 = new Barcode128(pdfDoc);

        // If the value is positive, the text distance under the bars. If zero or negative,
        // the text distance above the bars.
        code128.setBaseline(-1);
        code128.setSize(12);
        code128.setCode(code);
        code128.setCodeType(Barcode128.CODE128);
        Image code128Image = new Image(code128.createFormXObject(pdfDoc));

        // Notice that in iText5 in default PdfPCell constructor (new PdfPCell(Image img))
        // this image does not fit the cell, but it does in addCell().
        // In iText7 there is no constructor (new Cell(Image img)),
        // so the image adding to the cell can be done only using method add().
        Cell cell = new Cell().add(code128Image);
        table.addCell(cell);
        table.addCell("Add text and bar code separately:");

        code128 = new Barcode128(pdfDoc);

        // Suppress the barcode text
        code128.setFont(null);
        code128.setCode(code);
        code128.setCodeType(Barcode128.CODE128);

        // Let the image resize automatically by setting it to be autoscalable.
        code128Image = new Image(code128.createFormXObject(pdfDoc)).setAutoScale(true);
        cell = new Cell();
        cell.add(new Paragraph("PO #: " + code));
        cell.add(code128Image);
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }
}
