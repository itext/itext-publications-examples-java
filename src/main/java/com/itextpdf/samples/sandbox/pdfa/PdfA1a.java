package com.itextpdf.samples.sandbox.pdfa;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDate;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.pdfa.PdfADocument;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

public class PdfA1a {
    public static final String DEST = "./target/sandbox/pdfa/pdf_a_1a.pdf";

    public static final String BOLD = "./src/main/resources/font/OpenSans-Bold.ttf";

    public static final String DATA = "./src/main/resources/data/united_states.csv";

    public static final String FONT = "./src/main/resources/font/OpenSans-Regular.ttf";

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

        FileInputStream fileInputStream = new FileInputStream("./src/main/resources/data/sRGB_CS_profile.icm");

        PdfADocument pdfDoc = new PdfADocument(new PdfWriter(dest), PdfAConformanceLevel.PDF_A_1A,
                new PdfOutputIntent("Custom", "",
                        null, "sRGB IEC61966-2.1", fileInputStream));

        Document document = new Document(pdfDoc, PageSize.A4.rotate());
        pdfDoc
                .setTagged()
                .getCatalog()
                .setLang(new PdfString("en-us"));

        template = new PdfFormXObject(new Rectangle(795, 575, 30, 30));
        PdfCanvas canvas = new PdfCanvas(template, pdfDoc);

        total = new Image(template);
        total.getAccessibilityProperties().setRole(StandardRoles.ARTIFACT);

        // Creates a header for every page in the document
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new HeaderHandler());

        PdfDictionary parameters = new PdfDictionary();
        parameters.put(PdfName.ModDate, new PdfDate().getPdfObject());

        Table table = new Table(UnitValue.createPercentArray(
                new float[]{4, 1, 3, 4, 3, 3, 3, 3, 1})).useAllAvailableWidth();

        BufferedReader br = new BufferedReader(new FileReader(DATA));

        // Reads content of csv file
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

        // Parses csv string line with specified delimiter
        StringTokenizer tokenizer = new StringTokenizer(line, ";");

        while (tokenizer.hasMoreTokens()) {
            Paragraph content = new Paragraph(tokenizer.nextToken()).setFont(font).setFontSize(fontSize);

            if (isHeader) {
                table.addHeaderCell(content);
            } else {
                table.addCell(content);
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

            // Creates header text content
            canvas.beginText();
            canvas.setFontAndSize(font, 12);
            canvas.beginMarkedContent(PdfName.Artifact);
            canvas.moveText(34, 575);
            canvas.showText("Test");
            canvas.moveText(703, 0);
            canvas.showText(String.format("Page %d of", pageNum));
            canvas.endText();
            canvas.stroke();
            canvas.addXObjectAt(template, 0, 0);
            canvas.endMarkedContent();
            canvas.release();
        }
    }
}
