/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/28579382/underline-portion-of-text-using-itextsharp
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.StringReader;

@Category(SampleTest.class)
public class FillWithUnderline extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/fill_with_underline.pdf";
    public static final String SRC = "./src/test/resources/pdfs/form.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FillWithUnderline().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        form.flattenFields();

        Rectangle pos = form.getField("Name").getWidgets().get(0).getRectangle().toRectangle();

        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        InputSource is = new InputSource(new StringReader("<root><div>Bruno <u>Lowagie</u></div></root>"));
        parser.parse(is, new CustomHandler(doc, pos));

        pdfDoc.close();
    }


    private static class CustomHandler extends DefaultHandler {
        protected Document document;
        protected Rectangle position;
        protected Paragraph paragraph;
        protected boolean isUnderlined;

        public CustomHandler(Document document, Rectangle position) {
            this.document = document;
            this.position = position;
            paragraph = new Paragraph();
            isUnderlined = false;
        }

        /**
         * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
         * java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("u".equals(qName)) {
                isUnderlined = true;
            }
        }

        /**
         * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
         * java.lang.String, java.lang.String)
         */
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if ("div".equals(qName)) {
                document.add(paragraph.setFixedPosition(position.getLeft(), position.getBottom(), position.getWidth()));
                position.moveDown(20);
                paragraph = new Paragraph();
            } else if ("u".equals(qName)) {
                isUnderlined = false;
            }
        }

        /**
         * @see org.xml.sax.ContentHandler#characters(char[], int, int)
         */
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            Text text = new Text(strip(new StringBuffer().append(ch, start, length)));
            if (isUnderlined) {
                text.setUnderline();
            }
            if (0 != text.getText().length()) {
                paragraph.add(text);
            }
        }

        /**
         * Replaces all the newline characters by a space.
         *
         * @param buf the original StringBuffer
         * @return a String without newlines
         */
        protected String strip(StringBuffer buf) {
            while (buf.length() != 0 && (buf.charAt(0) == '\n' || buf.charAt(0) == '\t')) {
                buf.deleteCharAt(0);
            }
            while (buf.length() != 0 && (buf.charAt(0) == '\n' || buf.charAt(0) == '\t'))
                buf.deleteCharAt(buf.length() - 1);
            return buf.toString();
        }
    }
}
