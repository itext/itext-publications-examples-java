/**
 * <p>
 * The question was about adding compression to an image that already used /DCTDecode
 */
package com.itextpdf.samples.sandbox.images;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfArray;

import java.io.File;

public class FlateCompressJPEG2Passes {
    public static final String DEST = "./target/sandbox/images/flate_compress_jpeg_2passes.pdf";

    public static final String SRC = "./src/main/resources/pdfs/image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FlateCompressJPEG2Passes().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfReader reader = new PdfReader(SRC);
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(dest));

        // Assume that there is a single XObject in the source document
        // and this single object is an image.
        PdfDictionary pageDict = pdfDoc.getFirstPage().getPdfObject();
        PdfDictionary pageResources = pageDict.getAsDictionary(PdfName.Resources);
        PdfDictionary pageXObjects = pageResources.getAsDictionary(PdfName.XObject);
        PdfName imgName = pageXObjects.keySet().iterator().next();
        PdfStream imgStream = pageXObjects.getAsStream(imgName);
        imgStream.setData(reader.readStreamBytesRaw(imgStream));

        PdfArray array = new PdfArray();
        array.add(PdfName.FlateDecode);
        array.add(PdfName.DCTDecode);
        imgStream.put(PdfName.Filter, array);

        pdfDoc.close();
    }
}
