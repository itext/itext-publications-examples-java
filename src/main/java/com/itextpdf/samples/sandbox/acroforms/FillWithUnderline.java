package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.StringReader;

public class FillWithUnderline {
    public static final String DEST = "./target/sandbox/acroforms/fill_with_underline.pdf";

    public static final String SRC = "./src/main/resources/pdfs/form.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FillWithUnderline().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        // If no fields have been explicitly included, then all fields are flattened.
        // Otherwise only the included fields are flattened.
        form.flattenFields();

        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        InputSource is = new InputSource(new StringReader("<root><div>Bruno <u>Lowagie</u></div></root>"));
        Rectangle pos = form.getField("Name").getWidgets().get(0).getRectangle().toRectangle();

        // Custom handler gets position of the form field
        // to fill in the document with the parsed content.
        parser.parse(is, new CustomHandler(doc, pos));

        pdfDoc.close();
    }


    private static class CustomHandler extends DefaultHandler {
        protected Document document;
        protected Rectangle position;
        protected Paragraph paragraph;

        // If isUnderlined flag is true, then parsed text should be underlined.
        protected boolean isUnderlined;

        public CustomHandler(Document document, Rectangle position) {
            this.document = document;
            this.position = position;
            this.paragraph = new Paragraph();
            this.isUnderlined = false;
        }

        /**
         * This method implies that if the handler encounters opening tag &lt;u&gt;,
         * then it sets isUnderlined flag to true.
         */
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if ("u".equals(qName)) {
                isUnderlined = true;
            }
        }

        /**
         * This method implies how to handle encountered closing tags &lt;/div&gt; and &lt;/u&gt;:
         * if &lt;/div&gt;, then add the parsed text to the document;
         * if &lt;/u&gt;, then set isUnderlined flag to false
         */
        public void endElement(String uri, String localName, String qName) {
            if ("div".equals(qName)) {
                document.add(paragraph.setFixedPosition(position.getLeft(), position.getBottom(), position.getWidth()));

                // Set the position of the next form field.
                position.moveDown(22);
                paragraph = new Paragraph();
            } else if ("u".equals(qName)) {
                isUnderlined = false;
            }
        }

        /**
         * Creates a {@link Text} from the passed parameters and wraps it if not empty with a paragraph.
         */
        public void characters(char[] ch, int start, int length) {
            Text text = new Text(strip(new StringBuffer().append(ch, start, length)));
            if (isUnderlined) {
                text.setUnderline();
            }

            if (0 != text.getText().length()) {
                paragraph.add(text);
            }
        }

        /**
         * This method replaces all the newline characters by a space.
         */
        protected String strip(StringBuffer buf) {
            while (buf.length() != 0 && (buf.charAt(0) == '\n' || buf.charAt(0) == '\t')) {
                buf.deleteCharAt(0);
            }

            while (buf.length() != 0 && (buf.charAt(0) == '\n' || buf.charAt(0) == '\t')) {
                buf.deleteCharAt(buf.length() - 1);
            }

            return buf.toString();
        }
    }
}
