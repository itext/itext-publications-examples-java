package com.itextpdf.samples.sandbox.logging;

import com.itextpdf.kernel.counter.EventCounter;
import com.itextpdf.kernel.counter.EventCounterHandler;
import com.itextpdf.kernel.counter.IEventCounterFactory;
import com.itextpdf.kernel.counter.SimpleEventCounterFactory;
import com.itextpdf.kernel.counter.context.IContext;
import com.itextpdf.kernel.counter.context.UnknownContext;
import com.itextpdf.kernel.counter.event.IEvent;
import com.itextpdf.kernel.counter.event.IMetaInfo;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class CounterDemo {
    public static final String DEST_PDF = "./target/sandbox/logging/helloCounterDemo.pdf";
    public static final String DEST = "./target/sandbox/logging/CounterDemo.txt";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CounterDemo().manipulatePdf();
    }

    protected void manipulatePdf() throws IOException {

        // Implement and register custom factory
        IEventCounterFactory myCounterFactory = new SimpleEventCounterFactory(new ToLogCounter(UnknownContext.PERMISSIVE));
        EventCounterHandler.getInstance().register(myCounterFactory);

        // Generate 2 events by creating 2 pdf documents
        for (int i = 0; i < 2; i++) {
            createPdf();
        }

        EventCounterHandler.getInstance().unregister(myCounterFactory);
    }

    private static void createPdf() throws FileNotFoundException {
        Document document = new Document(new PdfDocument(new PdfWriter(DEST_PDF)));
        document.add(new Paragraph("Hello World!"));
        document.close();
    }

    private static class ToLogCounter extends EventCounter {
        private ToLogCounter(IContext fallback) {
            super(fallback);
        }

        // Triggering registered factories to produce events and count them
        @Override
        protected void onEvent(IEvent event, IMetaInfo metaInfo) {
            try (FileWriter writer = new FileWriter(DEST, true)) {
                writer.write(String.format("%s\n", event.getEventType()));
            } catch (IOException e) {
                System.err.println("IOException occurred.");
            }
        }
    }
}
