package com.itextpdf.samples.sandbox.images;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.File;

public class LargeImage1 {
    public static final String DEST = "./target/sandbox/images/large_image1.pdf";

    public static final String SRC = "./src/main/resources/pdfs/large_image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new LargeImage1().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument resultDoc = new PdfDocument(new PdfWriter(dest));
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));

        // Assume that there is a single XObject in the source document
        // and this single object is an image.
        PdfDictionary pageDict = srcDoc.getFirstPage().getPdfObject();
        PdfDictionary pageResources = pageDict.getAsDictionary(PdfName.Resources);
        PdfDictionary pageXObjects = pageResources.getAsDictionary(PdfName.XObject);
        PdfName imgRef = pageXObjects.keySet().iterator().next();
        PdfStream imgStream = pageXObjects.getAsStream(imgRef);
        PdfImageXObject imgObject = new PdfImageXObject((PdfStream) imgStream.copyTo(resultDoc));
        Image image = new Image(imgObject);
        image.scaleToFit(14400, 14400);
        image.setFixedPosition(0, 0);

        srcDoc.close();

        PageSize pageSize = new PageSize(image.getImageScaledWidth(), image.getImageScaledHeight());
        Document doc = new Document(resultDoc, pageSize);
        doc.add(image);

        doc.close();
    }
}
