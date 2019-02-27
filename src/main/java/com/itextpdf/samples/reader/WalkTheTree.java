/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.reader;

import java.io.IOException;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.tagging.IStructureNode;
import com.itextpdf.kernel.pdf.tagging.PdfStructElem;

public class WalkTheTree {

	public static final String SRC = "src/main/resources/pdf/test.pdf";
	
	/**
	 * <p>main.</p>
	 *
	 * @param args an array of {@link java.lang.String} objects.
	 * @throws java.io.IOException if any.
	 */
	public static void main(String[] args) throws IOException {
		PdfDocument document = new PdfDocument(new PdfReader(SRC));
		process(document.getStructTreeRoot());
	}
	
	/**
	 * <p>process.</p>
	 *
	 * @param elem a {@link com.itextpdf.kernel.pdf.tagging.IStructureNode} object.
	 */
	public static void process(IStructureNode elem) {
		if (elem == null) return;
		System.out.println(elem.getRole());
		System.out.println(elem.getClass().getName());
		if (elem instanceof PdfStructElem) {
			processStructElem((PdfStructElem) elem);
		}
		if (elem.getKids() == null) return;
		for (IStructureNode structElem : elem.getKids()) {
			process(structElem);
		}
	}
	
	/**
	 * <p>processStructElem.</p>
	 *
	 * @param elem a {@link com.itextpdf.kernel.pdf.tagging.PdfStructElem} object.
	 */
	public static void processStructElem(PdfStructElem elem) {
		PdfDictionary page = elem.getPdfObject().getAsDictionary(PdfName.Pg);
		if (page == null) return;
		PdfStream contents = page.getAsStream(PdfName.Contents);
		if (contents != null) {
			System.out.println(new String(contents.getBytes()));
			System.exit(0);
		}
		PdfArray array = page.getAsArray(PdfName.Contents);
		System.out.println(array);
	}
}
