/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/35011232
 */
package com.itextpdf.samples.sandbox.barcodes;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class BarcodeInTable extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/barcodes/barcode_in_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new BarcodeInTable().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        String code = "675-FH-A12";

        Table table = new Table(2);

        table.addCell("Change baseline:");
        Barcode128 code128 = new Barcode128(pdfDoc);
        code128.setBaseline(-1);
        code128.setSize(12);
        code128.setCode(code);
        code128.setCodeType(Barcode128.CODE128);
        // Notice that in itext5 in default PdfPCell constructor (new PdfPCell(Image img))
        // the image does not fit the cell, but it does in addCell
        Image code128Image = new Image(code128.createFormXObject(pdfDoc));//.setAutoScale(true);
        Cell cell = new Cell().add(code128Image);
        table.addCell(cell);

        table.addCell("Add text and bar code separately:");
        code128 = new Barcode128(pdfDoc);
        code128.setFont(null);
        code128.setCode(code);
        code128.setCodeType(Barcode128.CODE128);
        code128Image = new Image(code128.createFormXObject(pdfDoc)).setAutoScale(true);
        cell = new Cell();
        cell.add("PO #: " + code);
        cell.add(code128Image);
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }
}
