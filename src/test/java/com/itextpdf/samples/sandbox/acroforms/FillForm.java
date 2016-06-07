/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/*
 * Example written by Bruno Lowagie in answer to
 * http://stackoverflow.com/questions/36523371
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.xfa.XfaForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileInputStream;

@Category(SampleTest.class)
public class FillForm extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/fill_form.pdf";
    public static final String SRC = "./src/test/resources/pdfs/CertificateOfExcellence.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FillForm().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        form.getField("course").setValue("Copying and Pasting from StackOverflow");
        form.getField("name").setValue("Some dude on StackOverflow");
        form.getField("date").setValue("April 10, 2016");
        form.getField("description").setValue(
                "In this course, people consistently ignore the existing documentation completely. "
                        + "They are encouraged to do no effort whatsoever, but instead post their questions "
                        + "on StackOverflow. It would be a mistake to refer to people completing this course "
                        + "as developers. A better designation for them would be copy/paste artist. "
                        + "Only in very rare cases do these people know what they are actually doing. "
                        + "Not a single student has ever learned anything substantial during this course.");

        form.flattenFields();

        pdfDoc.close();
    }
}
