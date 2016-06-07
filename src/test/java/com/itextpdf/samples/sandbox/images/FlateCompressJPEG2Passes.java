/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This sample is written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/21958449/can-itextsharp-generate-pdf-with-jpeg-images-that-are-multi-stage-filtered-both
 * <p>
 * The question was about adding compression to an image that already used /DCTDecode
 */
package com.itextpdf.samples.sandbox.images;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;


@Category(SampleTest.class)
public class FlateCompressJPEG2Passes extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/images/flate_compress_jpeg_2passes.pdf";
    public static final String SRC = "./src/test/resources/pdfs/image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FlateCompressJPEG2Passes().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfReader reader = new PdfReader(SRC);
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));

        PdfDictionary pageDict = pdfDoc.getPage(1).getPdfObject();
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
