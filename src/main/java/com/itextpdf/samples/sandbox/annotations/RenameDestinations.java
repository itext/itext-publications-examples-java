package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfNameTree;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfString;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenameDestinations {
    public static final String DEST = "./target/sandbox/annotations/rename_destinations.pdf";

    public static final String SRC = "./src/main/resources/pdfs/nameddestinations.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RenameDestinations().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        Map<String, PdfString> renamed = new HashMap<>();

        PdfNameTree nameTree = pdfDoc.getCatalog().getNameTree(PdfName.Dests);
        Map<String, PdfObject> names = nameTree.getNames();
        List<String> keys = new ArrayList<String>(names.keySet());

        // Loop over all named destinations and rename its string values with new names
        for (String key : keys) {
            String newName = "new" + key;

            names.put(newName, names.get(key));
            names.remove(key);
            renamed.put(key, new PdfString(newName));
        }

        // Specify that the name tree has been modified.
        // This implies that the name tree will be rewritten on close() method.
        nameTree.setModified();

        PdfDictionary page = pdfDoc.getPage(1).getPdfObject();
        PdfArray annotations = page.getAsArray(PdfName.Annots);

        // Loop over all link annotations of the first page and change their destinations.
        for (int i = 0; i < annotations.size(); i++) {
            PdfDictionary annotation = annotations.getAsDictionary(i);
            PdfDictionary action = annotation.getAsDictionary(PdfName.A);
            if (action == null) {
                continue;
            }

            PdfString n = action.getAsString(PdfName.D);
            if (n != null && renamed.containsKey(n.toString())) {
                action.put(PdfName.D, renamed.get(n.toString()));
            }
        }

        pdfDoc.close();
    }
}
