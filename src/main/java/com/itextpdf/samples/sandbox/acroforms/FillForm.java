package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;

public class FillForm {
    public static final String DEST = "./target/sandbox/acroforms/fill_form.pdf";

    public static final String SRC = "./src/main/resources/pdfs/CertificateOfExcellence.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FillForm().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
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

        // If no fields have been explicitly included, then all fields are flattened.
        // Otherwise only the included fields are flattened.
        form.flattenFields();

        pdfDoc.close();
    }
}
