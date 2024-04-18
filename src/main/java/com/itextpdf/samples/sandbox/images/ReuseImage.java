package com.itextpdf.samples.sandbox.images;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.File;

public class ReuseImage {
    public static final String DEST = "./target/sandbox/images/reuse_image.pdf";

    public static final String SRC = "./src/main/resources/pdfs/single_image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ReuseImage().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        PdfDocument resultDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(resultDoc, PageSize.A4.rotate());

        // Assume that there is a single XObject in the source document
        // and this single object is an image.
        PdfDictionary pageDict = srcDoc.getFirstPage().getPdfObject();
        PdfDictionary pageResources = pageDict.getAsDictionary(PdfName.Resources);
        PdfDictionary pageXObjects = pageResources.getAsDictionary(PdfName.XObject);
        PdfName imgRef = pageXObjects.keySet().iterator().next();
        PdfStream imgStream = pageXObjects.getAsStream(imgRef);
        PdfImageXObject imgObject = new PdfImageXObject((PdfStream) imgStream.copyTo(resultDoc));

        srcDoc.close();

        Image image = new Image(imgObject);
        image.scaleToFit(PageSize.A4.getHeight(), PageSize.A4.getWidth());
        image.setFixedPosition((PageSize.A4.getHeight() - image.getImageScaledWidth()) / 2,
                (595 - image.getImageScaledHeight()) / 2);
        doc.add(image);

        doc.close();
    }
}
