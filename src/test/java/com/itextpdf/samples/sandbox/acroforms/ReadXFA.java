/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/
/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/28601068/get-names-field-from-interactive-form-pdf
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.xfa.XfaForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.io.FileOutputStream;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.experimental.categories.Category;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Category(SampleTest.class)
public class ReadXFA extends GenericTest {
    public static final String DEST = "./target/test/resources/xml/xfa_form_poland.xml";
    public static final String SRC = "./src/test/resources/pdfs/xfa_form_poland.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ReadXFA().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        compareXml = true;

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        XfaForm xfa = form.getXfaForm();

        Node node = xfa.getDatasetsNode();
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if ("data".equals(list.item(i).getLocalName())) {
                node = list.item(i);
                break;
            }
        }
        list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if ("movies".equals(list.item(i).getLocalName())) {
                node = list.item(i);
                break;
            }
        }

        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        FileOutputStream os = new FileOutputStream(DEST);
        tf.transform(new DOMSource(node), new StreamResult(os));
        os.close();

        pdfDoc.close();
    }
}
