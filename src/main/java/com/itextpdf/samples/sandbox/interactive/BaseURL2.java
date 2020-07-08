package com.itextpdf.samples.sandbox.interactive;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class BaseURL2 {
    public static final String DEST = "./target/sandbox/interactive/base_url2.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new BaseURL2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfDictionary uri = new PdfDictionary();
        uri.put(PdfName.Type, PdfName.URI);
        uri.put(new PdfName("Base"), new PdfString("http://itextpdf.com/"));
        pdfDoc.getCatalog().put(PdfName.URI, uri);

        PdfAction action = PdfAction.createURI("index.php");
        Link link = new Link("Home page", action);
        doc.add(new Paragraph(link));

        pdfDoc.close();
    }
}
