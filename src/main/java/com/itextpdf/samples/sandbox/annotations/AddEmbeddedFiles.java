package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;

import java.io.File;

public class AddEmbeddedFiles {
    public static final String[] ATTACHMENTS = {
            "hello", "world", "what", "is", "up"
    };

    public static final String DEST = "./target/sandbox/annotations/add_embedded_files.pdf";

    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddEmbeddedFiles().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        for (String text : ATTACHMENTS) {
            String embeddedFileName = String.format("%s.txt", text);
            String embeddedFileDescription = String.format("Some test: %s", text);
            byte[] embeddedFileContentBytes = embeddedFileDescription.getBytes();

            // the 5th argument is the mime-type of the embedded file;
            // the 6th argument is the PdfDictionary containing embedded file's parameters;
            // the 7th argument is the AFRelationship key value.
            PdfFileSpec spec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, embeddedFileContentBytes,
                    embeddedFileDescription, embeddedFileName, null, null, null);

            // This method adds file attachment at document level.
            pdfDoc.addFileAttachment(String.format("embedded_file%s", text), spec);
        }

        pdfDoc.close();
    }
}
