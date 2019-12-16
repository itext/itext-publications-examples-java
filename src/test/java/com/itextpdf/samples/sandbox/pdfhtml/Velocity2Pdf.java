/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.HtmlConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class Velocity2Pdf {

    public static final String DEST = "./target/sandbox/pdfhtml/velocity-test.pdf";

    public static final String SRC = "src/main/resources/html/templates/velocity-test.vm";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Velocity2Pdf().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {

        // Initialize the velocity engine
        VelocityEngine engine = new VelocityEngine();
        engine.init();

        // Create a velocity context and fill it with content
        VelocityContext context = new VelocityContext();
        context.put("message", "Hello World!");

        // Load the template
        StringWriter writer = new StringWriter();
        Template template = engine.getTemplate(SRC);
        template.merge(context, writer);

        try (FileOutputStream stream = new FileOutputStream(dest)) {
            HtmlConverter.convertToPdf(writer.toString(), stream);
        }
    }
}
