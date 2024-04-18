package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfCatalog;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.layer.PdfOCProperties;

import java.io.File;

public class ChangeOCGOrder {
    public static final String DEST = "./target/sandbox/stamper/change_ocg_order.pdf";
    public static final String SRC = "./src/main/resources/pdfs/ocg.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ChangeOCGOrder().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        PdfCatalog catalog = pdfDoc.getCatalog();
        PdfOCProperties ocProps = catalog.getOCProperties(true);
        PdfDictionary occd = (PdfDictionary) ocProps.getPdfObject().get(PdfName.D);
        PdfArray order = occd.getAsArray(PdfName.Order);

        PdfObject nestedLayers = order.get(0);
        PdfObject nestedLayerArray = order.get(1);
        PdfObject groupedLayers = order.get(2);
        PdfObject radioGroup = order.get(3);
        order.set(0, radioGroup);
        order.set(1, nestedLayers);
        order.set(2, nestedLayerArray);
        order.set(3, groupedLayers);

        pdfDoc.close();
    }
}
