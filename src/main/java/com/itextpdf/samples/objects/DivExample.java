/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.IOException;

public class DivExample {


	public final static String TEXT1 = "Test document which can be altered and ignored. alls ein Fehler innerhalb der Adaption auftritt wird dies im Signal ";
	public final static String TEXT2 = "Test document which can be altered and ignored. alls ein FeTest  document which can be altered and ignored. alls ein Fe";
	public final static String TEXT3 = "This text should come below the rectangle and there after a normal flow should happen ";

	/**
	 * <p>main.</p>
	 *
	 * @param args an array of {@link java.lang.String} objects.
	 * @throws java.io.IOException if any.
	 */
	public static void main(String[] args) throws IOException {
		PdfWriter writer = new PdfWriter("Notiz.pdf");
		PdfDocument pdfDocument = new PdfDocument(writer);
		PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
		Document doc = new Document(pdfDocument);
		Paragraph para = new Paragraph(TEXT1);
		doc.add(para);
	    doc.add(para);
		Paragraph paragraph = new Paragraph(TEXT2)
				.setFontSize(11)
				.setFont(font);
		Div div = new Div()
				.add(new Paragraph("Notiz:").setBold())
				.add(paragraph)
				.setWidth(400)
				.setPadding(3f)
				.setHorizontalAlignment(HorizontalAlignment.CENTER)
				.setBorder(new SolidBorder(0.5f));
		doc.add(div);
        para = new Paragraph(TEXT3);
        doc.add(para);
		pdfDocument.close();
	}
}
