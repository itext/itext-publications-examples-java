package com.itextpdf.samples.sandbox.tagging;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.tagging.IStructureNode;
import com.itextpdf.kernel.pdf.tagging.PdfStructElem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class WalkTheTree {
    public static final String DEST = "./target/txt/walk_the_tree.txt";

    public static final String SRC = "src/main/resources/pdfs/tree_test.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new WalkTheTree().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));
        StringBuilder builder = new StringBuilder();

        process(pdfDoc.getStructTreeRoot(), builder);

        createResultTxt(dest, builder.toString());
    }

    private static void process(IStructureNode elem, StringBuilder builder) {
        if (elem == null) {
            return;
        }

        builder.append("Role: " + elem.getRole() + "\n");
        builder.append("Class name: " + elem.getClass().getName() + "\n");
        if (elem instanceof PdfStructElem) {
            processStructElem((PdfStructElem) elem, builder);
        }

        if (elem.getKids() != null) {
            for (IStructureNode structElem : elem.getKids()) {
                process(structElem, builder);
            }
        }
    }

    private static void processStructElem(PdfStructElem elem, StringBuilder builder) {
        PdfDictionary page = elem.getPdfObject().getAsDictionary(PdfName.Pg);
        if (page == null) {
            return;
        }

        PdfStream contents = page.getAsStream(PdfName.Contents);
        if (contents != null) {
            builder.append("Content: \n" + new String(contents.getBytes(), StandardCharsets.UTF_8) + "\n");
        } else {
            PdfArray array = page.getAsArray(PdfName.Contents);
            builder.append("Contents array: " + array + "\n");
        }
    }

    private static void createResultTxt(String dest, String text) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest)))) {
            writer.write(text);
        }
    }
}
