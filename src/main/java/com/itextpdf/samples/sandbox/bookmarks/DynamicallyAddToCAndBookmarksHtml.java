package com.itextpdf.samples.sandbox.bookmarks;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;
import com.itextpdf.styledxmlparser.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DynamicallyAddToCAndBookmarksHtml {
    public static final String DEST = "./target/sandbox/bookmarks/DynamicallyAddToCAndBookmarksHtml.pdf";

    public static final String SRC = "./src/main/resources/pdfhtml/DynamicallyAddToCAndBookmarksHtml/original_file.html";

    private static final List<String> IDS = new ArrayList<>();

    static {
        IDS.add("random_id_1");
        IDS.add("random_id_2");
        IDS.add("random_id_3");
        IDS.add("random_id_4");
        IDS.add("random_id_5");
        IDS.add("random_id_6");
    }

    public static void main(String args[]) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DynamicallyAddToCAndBookmarksHtml().manipulatePdf();
    }

    public void manipulatePdf() throws Exception {
        Document htmlDoc = Jsoup.parse(new File(SRC), "UTF-8");

        // This is our Table of Contents aggregating element
        Element tocElement = htmlDoc.body().prependElement("div");
        tocElement.append("<b>Table of contents</b>");

        // We are going to build a complex CSS
        StringBuilder tocStyles = new StringBuilder().append("<style>");

        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(DEST))) {
            PdfOutline bookmarks = pdfDocument.getOutlines(false);
            Elements tocElements = htmlDoc.select("h2");
            for (Element elem : tocElements) {
                // Here we create an anchor to be able to refer to this element when generating page numbers and links
                String id = elem.attr("id");
                if (id == null || id.isEmpty()) {
                    id = generateId();
                    elem.attr("id", id);
                }

                // CSS selector to show page numbers for a TOC entry
                tocStyles.append("*[data-toc-id=\"").append(id)
                        .append("\"] .toc-page-ref::after { content: target-counter(#").append(id).append(", page) }");

                // Generating TOC entry as a small table to align page numbers on the right
                Element tocEntry = tocElement.appendElement("table");
                tocEntry.attr("style", "width: 100%");
                Element tocEntryRow = tocEntry.appendElement("tr");
                tocEntryRow.attr("data-toc-id", id);
                Element tocEntryTitle = tocEntryRow.appendElement("td");
                tocEntryTitle.appendText(elem.text());
                Element tocEntryPageRef = tocEntryRow.appendElement("td");
                tocEntryPageRef.attr("style", "text-align: right");
                // <span> is a placeholder element where target page number will be inserted
                // It is wrapped by an <a> tag to create links pointing to the element in our document
                tocEntryPageRef.append("<a href=\"#" + id + "\"><span class=\"toc-page-ref\"></span></a>");

                // Add bookmark
                PdfOutline bookmark = bookmarks.addOutline(elem.text());
                bookmark.addAction(PdfAction.createGoTo(id));
            }

            tocStyles.append("</style>");
            htmlDoc.head().append(tocStyles.toString());
            String html = htmlDoc.outerHtml();

            ConverterProperties converterProperties = new ConverterProperties().setImmediateFlush(false);
            HtmlConverter.convertToDocument(html, pdfDocument, converterProperties).close();
        }
    }

    private static String generateId() {
        // Usually random id can be generated, but for the purpose of testing we will use predefined ids.
        return IDS.isEmpty() ? null : IDS.remove(0);
    }
}
