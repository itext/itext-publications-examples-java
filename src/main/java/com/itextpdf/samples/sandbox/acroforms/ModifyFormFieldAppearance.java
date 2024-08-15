package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.forms.fields.PushButtonFormFieldBuilder;
import com.itextpdf.forms.fields.TextFormFieldBuilder;
import com.itextpdf.forms.form.element.Button;
import com.itextpdf.forms.form.element.TextArea;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.properties.Leading;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;

public class ModifyFormFieldAppearance {
    public static final String DEST = "./target/sandbox/acroforms/modifyFormFieldAppearance.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ModifyFormFieldAppearance().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        try (PdfDocument document = new PdfDocument(new PdfWriter(dest))) {
            PdfTextFormField textFormField = new TextFormFieldBuilder(document, "text")
                    .setWidgetRectangle(new Rectangle(50, 400, 200, 200)).createMultilineText();
            textFormField.setValue("Some text\nto show that\nleading and font changes\n work as expected");
            TextArea textArea = new TextArea("text");
            // It is not recommended to change font preferences since those won't be preserved after appearance changes,
            // but it is still possible.
            textArea.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 3f));
            textArea.setFontColor(ColorConstants.RED);
            textFormField.getFirstFormAnnotation().setFormFieldElement(textArea);

            PdfButtonFormField buttonFormField = new PushButtonFormFieldBuilder(document, "button")
                    .setWidgetRectangle(new Rectangle(300, 400, 200, 200)).setCaption("Send").createPushButton();
            Button button = new Button("button");
            button.setOpacity(0.5f);
            button.setBorderLeft(new SolidBorder(ColorConstants.RED, 10));
            button.setProperty(Property.PADDING_LEFT, UnitValue.createPointValue(50));
            button.setProperty(Property.PADDING_TOP, UnitValue.createPointValue(50));
            buttonFormField.getFirstFormAnnotation().setFormFieldElement(button);

            PdfAcroForm acroForm = PdfFormCreator.getAcroForm(document, true);
            acroForm.addField(textFormField);
            acroForm.addField(buttonFormField);
        }
    }
}
