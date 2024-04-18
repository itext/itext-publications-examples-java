package com.itextpdf.samples.sandbox.images;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class LargeImage2 {
    public static final String DEST = "./target/sandbox/images/large_image2.pdf";

    public static final String SRC = "./src/main/resources/pdfs/large_image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new LargeImage2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument resultDoc = new PdfDocument(new PdfWriter(dest));
        ByteArrayOutputStream tempFile = new ByteArrayOutputStream();

        // The source pdf document's page size is expected to be huge: more than 14400 in width in height
        PdfDocument tempDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(tempFile));

        // Assume that there is a single XObject in the source document
        // and this single object is an image.
        PdfDictionary pageDict = tempDoc.getFirstPage().getPdfObject();
        PdfDictionary pageResources = pageDict.getAsDictionary(PdfName.Resources);
        PdfDictionary pageXObjects = pageResources.getAsDictionary(PdfName.XObject);
        PdfName imgRef = pageXObjects.keySet().iterator().next();
        PdfStream imgStream = pageXObjects.getAsStream(imgRef);
        PdfImageXObject imgObject = new PdfImageXObject(imgStream);
        Image img = new Image(imgObject);
        img.scaleToFit(14400, 14400);
        img.setFixedPosition(0, 0);

        tempDoc.addNewPage(1, new PageSize(img.getImageScaledWidth(), img.getImageScaledHeight()));
        PdfPage page = tempDoc.getFirstPage();
        new Canvas(page, page.getPageSize())
                .add(img)
                .close();
        tempDoc.close();

        PdfDocument docToCopy = new PdfDocument(new PdfReader(new ByteArrayInputStream(tempFile.toByteArray())));
        docToCopy.copyPagesTo(1, 1, resultDoc);

        docToCopy.close();
        resultDoc.close();
    }
}
