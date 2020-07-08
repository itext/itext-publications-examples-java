package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfWidgetAnnotation;
import com.itextpdf.kernel.pdf.tagging.PdfObjRef;
import com.itextpdf.kernel.pdf.tagging.PdfStructElem;
import com.itextpdf.kernel.pdf.tagging.PdfStructureAttributes;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JawsFlattenedButtonFieldsTagging {
    public static final String DEST = "./target/sandbox/acroforms/jawsRecognition.pdf";
    public static final String SRC = "./src/main/resources/pdfs/jawsRecognition.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new JawsFlattenedButtonFieldsTagging().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDocument = initializeDocument(dest, SRC);

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, false);

        // Here we handle radio buttons and checkboxes (Button fields type) but there are also other field types
        // which can be used as well, for example they are Text fields, Choice fields, Signature fields
        for (PdfFormField field : form.getFormFields().values()) {
           if (field.getFieldFlag(PdfButtonFormField.FF_RADIO)) {
               addAttributes("rb", field, pdfDocument);

               // Checkbox existence should be checked by verifying if its field type is Btn and that a Push button
               // and Radio flags are both clear
           } else if (field.getFormType().equals(PdfName.Btn) &&
                   ((!field.getFieldFlag(PdfButtonFormField.FF_RADIO)) && (!field.getFieldFlag(PdfButtonFormField.FF_PUSH_BUTTON)))){
               addAttributes("cb", field, pdfDocument);
           }
        }

        form.flattenFields();

        pdfDocument.close();
    }

    private static void addAttributes(String attributeValue, PdfFormField pdfFormField, PdfDocument pdfDocument) {
        for (PdfWidgetAnnotation widget : pdfFormField.getWidgets()) {
            PdfDictionary pdfObject = widget.getPage().getPdfObject();
            int i = widget.getPdfObject().getAsNumber(PdfName.StructParent).intValue();
            PdfObjRef objRef = pdfDocument.getStructTreeRoot()
                    .findObjRefByStructParentIndex(pdfObject, i);
            if (objRef != null) {
                TagTreePointer p = pdfDocument.getTagStructureContext()
                        .createPointerForStructElem((PdfStructElem) objRef.getParent());
                List<PdfStructureAttributes> attributes = p.getProperties().getAttributesList();
                boolean printFieldAttrFound = false;
                for (PdfStructureAttributes attribute : attributes) {
                    if (attribute.getAttributeAsEnum("O").equals("PrintField")) {
                        printFieldAttrFound = true;
                        break;
                    }
                }
                if (!printFieldAttrFound) {
                    p.getProperties().addAttributes(new PdfStructureAttributes("PrintField")
                            .addEnumAttribute("Role", attributeValue));
                }
            } else {
                System.out.println("The object reference couldn't be found.");
                return;
            }
        }
    }

    // Create the new document using already existed one. New document can be also created using iText, but this approach
    // will require adding custom form fields first of all
    private static PdfDocument initializeDocument(String dest, String src) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(src), new PdfWriter(dest));

        if (!pdfDocument.isTagged()) {
            System.out.println("The document should be tagged to add desired attributes to it.");
        }

        return pdfDocument;
    }
}
