/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.html.templates;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensekey.LicenseKey;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

public class Velocity2Pdf {

    public static final String TARGET = " target/results/html/";
    public static final String DEST = String.format("%svelocity-test.pdf", TARGET);
    public static final String SRC = "src/main/resources/html/templates/velocity-test.vm";


    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.io.IOException if any.
     */
    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
        File file = new File(TARGET);
        file.mkdirs();

        /*Initialize the velocity engine*/
        VelocityEngine engine = new VelocityEngine();
        engine.init();

        /*Create a velocity context and populate it*/
        VelocityContext context = new VelocityContext();
        context.put("message","Hello World!");

        /*Load the template*/
        StringWriter writer = new StringWriter();
        Template template = engine.getTemplate(SRC);
        template.merge(context, writer);

        new Velocity2Pdf().createPdf(writer.toString(), DEST);
    }

    /**
     * <p>createPdf.</p>
     *
     * @param html a {@link java.lang.String} object.
     * @param dest a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public void createPdf(String html, String dest) throws IOException {
        HtmlConverter.convertToPdf(html, new FileOutputStream(dest));
    }
}
