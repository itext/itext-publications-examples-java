/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.logging;

import com.itextpdf.commons.actions.AbstractContextBasedEventHandler;
import com.itextpdf.commons.actions.AbstractContextBasedITextEvent;
import com.itextpdf.commons.actions.EventManager;
import com.itextpdf.commons.actions.confirmations.ConfirmEvent;
import com.itextpdf.commons.actions.contexts.IContext;
import com.itextpdf.commons.actions.contexts.UnknownContext;
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
        ToLogCounter logCounter = new ToLogCounter(UnknownContext.PERMISSIVE);
        EventManager.getInstance().register(logCounter);

        // Generate 2 events by creating 2 pdf documents
        for (int i = 0; i < 2; i++) {
            createPdf();
        }

        EventManager.getInstance().unregister(logCounter);
    }

    private static void createPdf() throws FileNotFoundException {
        Document document = new Document(new PdfDocument(new PdfWriter(DEST_PDF)));
        document.add(new Paragraph("Hello World!"));
        document.close();
    }

    private static class ToLogCounter extends AbstractContextBasedEventHandler {
        private ToLogCounter(IContext fallback) {
            super(fallback);
        }

        // Triggering registered factories to produce events and count them
        @Override
        protected void onAcceptedEvent(AbstractContextBasedITextEvent event) {
            try (FileWriter writer = new FileWriter(DEST, true)) {
                if (event instanceof ConfirmEvent) {
                    ConfirmEvent confirmEvent = (ConfirmEvent) event;
                    writer.write(String.format("%s\n", confirmEvent.getEventType()));
                }
            } catch (IOException e) {
                System.err.println("IOException occurred.");
            }
        }
    }
}
