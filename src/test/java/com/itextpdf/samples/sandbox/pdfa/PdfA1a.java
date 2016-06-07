/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/27500586/itext-page-number-in-header-within-pdf-a
 */
package com.itextpdf.samples.sandbox.pdfa;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.pdfa.PdfADocument;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.*;
import java.util.StringTokenizer;

@Category(SampleTest.class)
public class PdfA1a extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox//pdfa/pdf_a_1a.pdf";
    public static final String BOLD = "./src/test/resources/font/OpenSans-Bold.ttf";
    public static final String DATA = "./src/test/resources/data/united_states.csv";
    public static final String FONT = "./src/test/resources/font/OpenSans-Regular.ttf";

    protected PdfFormXObject template;
    protected Image total;
    protected PdfFont font;
    protected PdfFont bold;

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PdfA1a().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, XMPException {
        font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        bold = PdfFontFactory.createFont(BOLD, PdfEncodings.IDENTITY_H);
        FileInputStream is = new FileInputStream("./src/test/resources/data/sRGB_CS_profile.icm");
        PdfADocument pdfDoc = new PdfADocument(new PdfWriter(dest), PdfAConformanceLevel.PDF_A_1A,
                new PdfOutputIntent("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", is));

        Document document = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());
        pdfDoc.setTagged();

        template = new PdfFormXObject(new Rectangle(795, 575, 30, 30));
        PdfCanvas canvas = new PdfCanvas(template, pdfDoc);
        total = new Image(template);
        total.setRole(PdfName.Artifact);

        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new HeaderHandler());
        pdfDoc.getCatalog().setLang(new PdfString("en-us"));
        PdfDictionary parameters = new PdfDictionary();
        parameters.put(PdfName.ModDate, new PdfDate().getPdfObject());

        Table table = new Table(new float[]{4, 1, 3, 4, 3, 3, 3, 3, 1});
        table.setWidthPercent(100);
        BufferedReader br = new BufferedReader(new FileReader(DATA));
        String line = br.readLine();
        process(table, line, bold, 10, true);
        while ((line = br.readLine()) != null) {
            process(table, line, font, 10, false);
        }
        br.close();
        document.add(table);

        canvas.beginText();
        canvas.setFontAndSize(bold, 12);
        canvas.moveText(795, 575);
        canvas.showText(Integer.toString(pdfDoc.getNumberOfPages()));
        canvas.endText();
        canvas.stroke();

        document.close();
    }

    public void process(Table table, String line, PdfFont font, int fontSize, boolean isHeader) {
        StringTokenizer tokenizer = new StringTokenizer(line, ";");
        while (tokenizer.hasMoreTokens()) {
            if (isHeader) {
                table.addHeaderCell(new Paragraph(tokenizer.nextToken()).setFont(font).setFontSize(fontSize));
            } else {
                table.addCell(new Paragraph(tokenizer.nextToken()).setFont(font).setFontSize(fontSize));
            }
        }
    }


    public class HeaderHandler implements IEventHandler {
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();
            int pageNum = docEvent.getDocument().getPageNumber(page);
            PdfCanvas canvas = new PdfCanvas(page);
            canvas.beginText();
            canvas.setFontAndSize(font, 12);
            canvas.beginMarkedContent(PdfName.Artifact);
            canvas.moveText(34, 575);
            canvas.showText("Test");
            canvas.moveText(703, 0);
            canvas.showText(String.format("Page %d of", pageNum));
            canvas.endText();
            canvas.stroke();
            canvas.addXObject(template, 0, 0);
            canvas.endMarkedContent();
            canvas.release();
        }
    }
}
