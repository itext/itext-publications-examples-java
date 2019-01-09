/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.xfa;

import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.tool.xml.xtra.xfa.MetaData;
import com.itextpdf.tool.xml.xtra.xfa.XFAFlattener;
import com.itextpdf.tool.xml.xtra.xfa.XFAFlattenerProperties;
import com.itextpdf.tool.xml.xtra.xfa.font.XFAFontSettings;
import org.junit.experimental.categories.Category;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Demey
 */
@Category( SampleTest.class)
public class FlattenXfaDocument extends GenericTest {
    public static final String XFA = "./src/test/resources/xfa/xfa.pdf";
    public static final String DEST = "./target/test/resources/xfa/flattened.pdf";

    private List<String> javascriptEvents;

    @Override
    protected void beforeManipulatePdf() {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/all-products.xml");

        this.javascriptEvents = new ArrayList<>();

        this.javascriptEvents.add("click");
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        XFAFlattenerProperties flattenerProperties = new XFAFlattenerProperties()
                .setPdfVersion(XFAFlattenerProperties.PDF_1_7)
                .createXmpMetaData()
                .setTagged()
                .setMetaData(
                        new MetaData()
                            .setAuthor("iText Samples")
                            .setLanguage("EN")
                            .setSubject("Showing off our flattening skills")
                            .setTitle("Flattened XFA"));

        XFAFlattener xfaf = new XFAFlattener()
                .setFontSettings(new XFAFontSettings().setEmbedExternalFonts(true))
                .setExtraEventList(this.javascriptEvents)
                .setFlattenerProperties(flattenerProperties)
                .setViewMode(XFAFlattener.ViewMode.SCREEN);


        xfaf.flatten(new FileInputStream(XFA), new FileOutputStream(dest));
    }
}
