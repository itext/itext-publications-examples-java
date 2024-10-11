package com.itextpdf.samples.sandbox.tagging;

import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.kernel.pdf.tagging.*;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class ExtractContentBasedOnTags {

    public static final String SRC = "./src/main/resources/tagging/starter_pdfua1.pdf";
    public static final String DEST = "./target/sandbox/tagging/extracted_content.pdf";

    public static void main(String[] args) throws IOException {
        new ExtractContentBasedOnTags().extractContentText();
    }

    public void extractContentText() throws IOException {
        PdfReader reader = new PdfReader(SRC);
        // Add a dummy writer to the output stream if iText needs to repair the document.
        PdfDocument doc = new PdfDocument(reader, new PdfWriter(DEST));
        TagContentLookup contentLookup = new TagContentLookup(doc);

        System.out.println("Example 1: Print all tag content");
        examplePrintAllTagContent(doc, contentLookup);
        System.out.println("Example 2: Print all tag content and collapsed leafs");
        examplePrintAllTagContentAndCollapsedLeafs(doc, contentLookup);
        System.out.println("Example 3: Only get H1 tags");
        exampleOnlyGetH1Tags(doc, contentLookup);
        System.out.println("Example 4: Extract alternate description from all images");
        extractAlternateDescriptionFromAllImages(doc, contentLookup);
        System.out.println("Example 5: Extract specific MCID");
        extractSpecificMCID(doc, contentLookup);
        doc.close();
    }


    private static void examplePrintAllTagContent(PdfDocument doc, TagContentLookup contentLookup) {
        TagTreePointer tagPointer = doc.getTagStructureContext().getAutoTaggingPointer();
        TagTreePointerIterator iterator = new TagTreePointerIterator(tagPointer);
        while (iterator.hasNext()) {
            TagTreePointer currentPointer = iterator.next();
            ExtractionContent content = contentLookup.getContent(currentPointer, false);
            System.out.println("Tag: " + currentPointer.getRole() + " - " + content);
        }
    }

    private static void examplePrintAllTagContentAndCollapsedLeafs(PdfDocument doc, TagContentLookup contentLookup) {
        TagTreePointer tagPointer = doc.getTagStructureContext().getAutoTaggingPointer();
        TagTreePointerIterator iterator = new TagTreePointerIterator(tagPointer);
        while (iterator.hasNext()) {
            TagTreePointer currentPointer = iterator.next();
            ExtractionContent content = contentLookup.getContent(currentPointer, true);
            System.out.println("Tag: " + currentPointer.getRole() + " - " + content);
        }
    }

    private static void exampleOnlyGetH1Tags(PdfDocument doc, TagContentLookup contentLookup) {
        TagTreePointer tagPointer = doc.getTagStructureContext().getAutoTaggingPointer();
        TagTreePointerIterator iterator = new TagTreePointerIterator(tagPointer);
        while (iterator.hasNext()) {
            TagTreePointer currentPointer = iterator.next();
            if (currentPointer.getRole().equals("H1")) {
                ExtractionContent content = contentLookup.getContent(currentPointer, true);
                System.out.println("Tag: " + currentPointer.getRole() + " - " + content);
            }
        }
    }

    private static void extractAlternateDescriptionFromAllImages(PdfDocument doc, TagContentLookup contentLookup) {
        TagTreePointer tagPointer = doc.getTagStructureContext().getAutoTaggingPointer();
        TagTreePointerIterator iterator = new TagTreePointerIterator(tagPointer);
        while (iterator.hasNext()) {
            TagTreePointer currentPointer = iterator.next();
            if (currentPointer.getRole().equals("Figure")) {
                System.out.println("Tag: " + currentPointer.getRole() + " - " + currentPointer.getProperties().getAlternateDescription());
            }
        }
    }

    private static void extractSpecificMCID(PdfDocument doc, TagContentLookup contentLookup) {
        TagTreePointer tagPointer = doc.getTagStructureContext().getAutoTaggingPointer();
        TagTreePointerIterator iterator = new TagTreePointerIterator(tagPointer);
        int pageNumber = 1;
        int mcid = 6;
        while (iterator.hasNext()) {
            TagTreePointer currentPointer = iterator.next();
            ExtractionContent content = contentLookup.getContent(currentPointer, false);
            for (Content content1 : content.content) {
                if (content1.McId == mcid && content1.pageNumber == pageNumber) {
                    System.out.println("Tag: " + currentPointer.getRole() + " - " + content1);
                }
            }
        }
    }


    static class TagTreePointerIterator {

        private final List<TagTreePointer> data = new ArrayList<>();
        private int currentIndex = 0;

        TagTreePointerIterator(TagTreePointer pointer) {
            traverse(new TagTreePointer(pointer), data);
        }


        public boolean hasNext() {
            return currentIndex < data.size();
        }

        public TagTreePointer next() {
            return data.get(currentIndex++);
        }

        private static void traverse(TagTreePointer pointer, List<TagTreePointer> pointers) {
            pointers.add(new TagTreePointer(pointer));
            for (int i = 0; i < pointer.getKidsRoles().size(); i++) {
                if (pointer.getKidsRoles().get(i).equals("MCR")) {
                    continue;
                }
                pointer.moveToKid(i);
                traverse(pointer, pointers);
                pointer.moveToParent();
            }
        }
    }

    static class TagContentLookup {
        private final List<Content> mcidToContent;

        public TagContentLookup(PdfDocument doc) {
            TagContentLookupListener listener = new TagContentLookupListener();
            PdfCanvasProcessor processor = new PdfCanvasProcessor(listener);
            for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                listener.newPage(i);
                processor.processPageContent(doc.getPage(i));
            }
            listener.finish();
            //We are using a simple list to store the content, you could use a more advanced data structure to store the content
            //to allow for faster lookups for example a map with HashMap<Integer/*page*/,HashMap<Integer/*mcid*/, List<Content>>
            mcidToContent = listener.mcidToContent;
        }


        /*
        Collapsing nodes mean let's say you have some structure on like this
         - caption
            - paragraph
         With collapsedLeaf node set to true you will get the content of the caption and the paragraph in the same list
         Which could make sense in some cases as the paragraph is part of the caption
         */
        public ExtractionContent getContent(TagTreePointer pointer, boolean collapseLeafNodes) {
            PdfStructElem elem = pointer.getDocument().getTagStructureContext().getPointerStructElem(pointer);
            return getContent(pointer, elem, collapseLeafNodes);
        }

        private ExtractionContent getContent(TagTreePointer pointer, PdfStructElem elem, boolean collapseLeafNodes) {
            ExtractionContent extractionContent = new ExtractionContent();
            extractionContent.content = new ArrayList<>();
            extractionContent.pointer = pointer;
            for (IStructureNode kid : elem.getKids()) {
                if (kid instanceof PdfMcrNumber || kid instanceof PdfMcrDictionary) {
                    PdfMcr mcr = (PdfMcr) kid;
                    int pageNumber = getPageNumberFromPointer(pointer.getDocument(), elem);
                    for (Content s : mcidToContent) {
                        if (s.McId == mcr.getMcid() && s.pageNumber == pageNumber) {
                            extractionContent.content.add(s);
                        }
                    }
                } else if (kid instanceof PdfStructElem && collapseLeafNodes) {
                    ExtractionContent cCollapsed = getContent(pointer, (PdfStructElem) kid, true);
                    if (cCollapsed.content != null) {
                        extractionContent.content.addAll(cCollapsed.content);
                    }
                }
            }
            return extractionContent;
        }
    }

    private static int getPageNumberFromPointer(PdfDocument doc, PdfStructElem elem) {
        PdfObject elemPage = elem.getPdfObject().getAsDictionary(PdfName.Pg);
        for (int i = 1; i <= doc.getNumberOfPages(); i++) {
            PdfObject page = doc.getPage(i).getPdfObject();
            if (page.equals(elemPage)) {
                return i;
            }
        }
        return -1;
    }

    public static abstract class Content {
        protected int McId;
        protected int pageNumber;
    }

    private static class TextContent extends Content {

        public TextContent(String content) {
            this.content = content;
        }

        protected String content;

        @Override
        public String toString() {
            return "TextContent{" +
                    "McId= " + this.McId + ", " +
                    "pageNumber= " + this.pageNumber + ", " +
                    "content='" + content + '\'' +
                    '}';
        }
    }

    private static class ImageContent extends Content {


        public PdfImageXObject image;
        public PdfName imageName;
        public Vector startPoint;
        public boolean isInline;


        @Override
        public String toString() {
            return "ImageContent{" +
                    "pageNumber=" + pageNumber +
                    ", McId=" + McId +
                    ", isInline=" + isInline +
                    ", startPoint=" + startPoint +
                    ", imageName=" + imageName +
                    ", image=" + image +
                    '}';
        }
    }

    private static class TagContentLookupListener implements IEventListener {

        private final List<Content> mcidToContent = new ArrayList<>();
        private LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        private int pageNumber;
        private int currentMcid = -1;


        @Override
        public void eventOccurred(IEventData data, EventType type) {
            if (type == EventType.RENDER_TEXT) {
                TextRenderInfo renderInfo = (TextRenderInfo) data;
                if (currentMcid != renderInfo.getMcid()) {
                    textTagFinished();
                    currentMcid = renderInfo.getMcid();
                }
                strategy.eventOccurred(data, type);
            } else if (type == EventType.RENDER_IMAGE) {
                ImageRenderInfo imageRenderInfo = (ImageRenderInfo) data;
                //Inline images might be part of a text block
                if (currentMcid != imageRenderInfo.getMcid()) {
                    textTagFinished();
                    currentMcid = imageRenderInfo.getMcid();
                }
                ImageContent imageContent = new ImageContent();
                imageContent.image = imageRenderInfo.getImage();
                imageContent.imageName = imageRenderInfo.getImageResourceName();
                imageContent.startPoint = imageRenderInfo.getStartPoint();
                imageContent.isInline = imageRenderInfo.isInline();
                imageContent.pageNumber = pageNumber;
                imageContent.McId = currentMcid;
                mcidToContent.add(imageContent);
            }
        }


        public void newPage(int pageNumber) {
            //We are starting a new page, so we should finish the current tag as the page is a new context
            textTagFinished();
            this.pageNumber = pageNumber;
            //Reset the current Mcid as we are starting a new page
            currentMcid = -1;
        }

        public void finish() {
            textTagFinished();
        }

        private void textTagFinished() {
            if (currentMcid == -1) {
                return;
            }
            Content content = new TextContent(strategy.getResultantText());
            content.McId = currentMcid;
            content.pageNumber = pageNumber;
            mcidToContent.add(content);
            strategy = new LocationTextExtractionStrategy();
            currentMcid = -1;
        }

        @Override
        public Set<EventType> getSupportedEvents() {
            return null;
        }
    }

    static class ExtractionContent {
        public List<Content> content;
        public TagTreePointer pointer;

        @Override
        public String toString() {
            return "ExtractionContent{" +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}


