/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.form.element.Button;
import com.itextpdf.forms.form.element.InputField;
import com.itextpdf.forms.form.element.Radio;
import com.itextpdf.forms.form.element.TextArea;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;

public class CreateFormFieldThroughLayout {
    public static final String DEST = "./target/sandbox/acroforms/createFormFieldThroughLayout.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CreateFormFieldThroughLayout().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        try (Document document = new Document(new PdfDocument(new PdfWriter(dest)))) {
            InputField inputField = new InputField("input field");
            inputField.setValue("John");
            inputField.setInteractive(true);

            TextArea textArea = new TextArea("text area");
            textArea.setValue("I'm a chess player.\n" 
                    + "In future I want to compete in professional chess and be the world champion.\n" 
                    + "My favorite opening is caro-kann.\n" 
                    + "Also I play sicilian defense a lot.");
            textArea.setInteractive(true);

            Table table = new Table(2, false);
            table.addCell("Name:");
            table.addCell(new Cell().add(inputField));
            table.addCell("Personal info:");
            table.addCell(new Cell().add(textArea));

            Radio male = new Radio("male", "radioGroup");
            male.setChecked(false);
            male.setInteractive(true);
            male.setBorder(new SolidBorder(1));
            
            Paragraph maleText = new Paragraph("Male: ");
            maleText.add(male);
            
            Radio female = new Radio("female", "radioGroup");
            female.setChecked(true);
            female.setInteractive(true);
            female.setBorder(new SolidBorder(1));

            Paragraph femaleText = new Paragraph("Female: ");
            femaleText.add(female);

            Button button = new Button("submit");
            button.setValue("Submit");
            button.setInteractive(true);
            button.setBorder(new SolidBorder(2));
            button.setWidth(50);
            button.setBackgroundColor(ColorConstants.LIGHT_GRAY);
            
            document.add(table);
            document.add(maleText);
            document.add(femaleText);
            document.add(button);
        }
    }
}
