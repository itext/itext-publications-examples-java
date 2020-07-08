package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.File;
import java.io.IOException;

public class OrdinalNumbers {
    public static final String DEST = "./target/sandbox/objects/ordinal_numbers.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new OrdinalNumbers().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        Text st = new Text("st").setFont(font).setFontSize(6);
        st.setTextRise(7);
        Text nd = new Text("nd").setFont(font);
        nd.setTextRise(7);
        Text rd = new Text("rd").setFont(font);
        rd.setTextRise(7);
        Text th = new Text("th").setFont(font);
        th.setTextRise(7);

        Paragraph first = new Paragraph();
        first.add("The 1");
        first.add(st);
        first.add(" of May");
        doc.add(first);

        Paragraph second = new Paragraph();
        second.add("The 2");
        second.add(nd);
        second.add(" and the 3");
        second.add(rd);
        second.add(" of June");
        doc.add(second);

        Paragraph third = new Paragraph();
        third.add("The 4");
        third.add(th);
        third.add(" of July");
        doc.add(third);

        doc.close();
    }
}
