package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;

import java.io.File;

public class AddEmbeddedFile {
    public static final String DEST = "./target/sandbox/annotations/add_embedded_file.pdf";

    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddEmbeddedFile().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        String embeddedFileName = "test.txt";
        String embeddedFileDescription = "some_test";
        byte[] embeddedFileContentBytes = "Some test".getBytes();

        // the 5th argument is the mime-type of the embedded file;
        // the 6th argument is the PdfDictionary containing embedded file's parameters;
        // the 7th argument is the AFRelationship key value.
        PdfFileSpec spec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, embeddedFileContentBytes,
                embeddedFileDescription, embeddedFileName, null, null, null);

        // This method adds file attachment at document level.
        pdfDoc.addFileAttachment("embedded_file", spec);

        pdfDoc.close();
    }
}
