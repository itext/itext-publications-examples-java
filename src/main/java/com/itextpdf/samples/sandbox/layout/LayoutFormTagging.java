package com.itextpdf.samples.sandbox.layout;

import com.itextpdf.forms.form.FormProperty;
import com.itextpdf.forms.form.element.Button;
import com.itextpdf.forms.form.element.CheckBox;
import com.itextpdf.forms.form.element.ComboBoxField;
import com.itextpdf.forms.form.element.InputField;
import com.itextpdf.forms.form.element.ListBoxField;
import com.itextpdf.forms.form.element.Radio;
import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.forms.form.element.TextArea;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class LayoutFormTagging {

    public static final String DEST = "./target/sandbox/layout/changeFormRole.pdf";

    public static void main(String args[]) throws IOException, InterruptedException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new LayoutFormTagging().changeFormRole(DEST);
    }

    private void changeFormRole(String dest) throws IOException {
        try (Document document = new Document(new PdfDocument(new PdfWriter(dest)))) {
            document.getPdfDocument().setTagged();

            // Creating Button and setting the Div role
            Button formButton = new Button("form button");
            formButton.add(new Paragraph("paragraph with yellow border inside button")
                    .setBorder(new SolidBorder(ColorConstants.YELLOW, 1)));
            formButton.getAccessibilityProperties().setRole(StandardRoles.DIV);
            document.add(formButton);

            // Creating a CheckBox and setting the SECT role
            CheckBox checkBoxUnset = new CheckBox("CheckBox");
            checkBoxUnset.setBorder(new SolidBorder(ColorConstants.GREEN, 10));
            checkBoxUnset.getAccessibilityProperties().setRole(StandardRoles.SECT);
            document.add(checkBoxUnset);

            // Creating a ComboBoxField and setting the SPAN role
            ComboBoxField comboBoxField = new ComboBoxField("empty combo box field");
            comboBoxField.setBackgroundColor(ColorConstants.RED);
            comboBoxField.getAccessibilityProperties().setRole(StandardRoles.SPAN);
            document.add(comboBoxField);

            // Creating an InputField and setting the ANNOT role
            InputField formInputField = new InputField("form input field");
            formInputField.setProperty(FormProperty.FORM_FIELD_VALUE, "form input field");
            formInputField.getAccessibilityProperties().setRole(StandardRoles.ANNOT);
            document.add(formInputField);

            // Creating a ListBoxField and setting the LBL role
            ListBoxField formListBoxField = new ListBoxField("form list box field", 2, false);
            formListBoxField.addOption("option 1", false);
            formListBoxField.addOption("option 2", true);
            formListBoxField.getAccessibilityProperties().setRole(StandardRoles.LBL);
            document.add(formListBoxField);

            // Creating a Radio and setting the CAPTION role
            Radio formRadioField = new Radio("form radio field");
            formRadioField.setChecked(true);
            formRadioField.getAccessibilityProperties().setRole(StandardRoles.CAPTION);
            document.add(formRadioField);

            // Creating a SignatureFieldAppearance and setting the FORMULA role
            SignatureFieldAppearance formSigField = new SignatureFieldAppearance("form SigField");
            formSigField.setContent("form SigField");
            formSigField.setBorder(new SolidBorder(ColorConstants.YELLOW, 1));
            formSigField.getAccessibilityProperties().setRole(StandardRoles.FORMULA);
            document.add(formSigField);

            // Creating a TextArea and setting the H role
            TextArea flattenedTextArea = new TextArea("flattened text area");
            flattenedTextArea.setValue("Text area with custom border");
            flattenedTextArea.setInteractive(false);
            flattenedTextArea.setBorder(new DashedBorder(ColorConstants.ORANGE, 10));
            flattenedTextArea.getAccessibilityProperties().setRole(StandardRoles.H);
            document.add(flattenedTextArea);
        }
    }
}
