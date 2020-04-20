package com.itextpdf.samples.sandbox.collections;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.collection.PdfCollection;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class PortableCollection {
    public static final String DEST = "./target/sandbox/collections/portable_collection.pdf";

    public static final String DATA = "./src/main/resources/data/united_states.csv";
    public static final String HELLO = "./src/main/resources/pdfs/hello.pdf";
    public static final String IMG = "./src/main/resources/img/berlin2013.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PortableCollection().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("Portable collection"));
        
        PdfCollection collection = new PdfCollection();
        collection.setView(PdfCollection.TILE);
        pdfDoc.getCatalog().setCollection(collection);

        addFileAttachment(pdfDoc, DATA, "united_states.csv");
        addFileAttachment(pdfDoc, HELLO, "hello.pdf");
        addFileAttachment(pdfDoc, IMG, "berlin2013.jpg");

        doc.close();
    }

    // This method adds file attachment to the pdf document
    private void addFileAttachment(PdfDocument document, String attachmentPath, String fileName) throws IOException {
        String embeddedFileName = fileName;
        String embeddedFileDescription = fileName;
        String fileAttachmentKey = fileName;

        // the 5th argument is the mime-type of the embedded file;
        // the 6th argument is the AFRelationship key value.
        PdfFileSpec fileSpec = PdfFileSpec.createEmbeddedFileSpec(document, attachmentPath, embeddedFileDescription,
                embeddedFileName, null, null);
        document.addFileAttachment(fileAttachmentKey, fileSpec);
    }
}
