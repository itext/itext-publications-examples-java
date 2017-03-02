package com.itextpdf.samples.sandbox.xfa;

import com.itextpdf.licensekey.XfaLicenseKey;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.tool.xml.xtra.xfa.MetaData;
import com.itextpdf.tool.xml.xtra.xfa.XFAFlattener;
import com.itextpdf.tool.xml.xtra.xfa.XFAFlattenerProperties;
import com.itextpdf.tool.xml.xtra.xfa.font.XFAFontSettings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.experimental.categories.Category;

/**
 * @author Michael Demey
 */
@Category( SampleTest.class)
public class FlattenXfaDocument extends GenericTest {
    private final String XFA = "./src/test/resources/xfa/xfa.pdf";
    private final String DEST = "./target/test/resources/xfa/flattened.pdf";

    private List<String> javascriptEvents;

    @Override
    protected void beforeManipulatePdf() {
        XfaLicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/all-products.xml");

        this.javascriptEvents = new ArrayList<>();

        this.javascriptEvents.add("click");
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        XFAFlattenerProperties flattenerProperties = new XFAFlattenerProperties()
                .setPdfVersion(XFAFlattenerProperties.PDF_1_7)
                .createXmpMetaData()
                .setTagged()
                .setExtractXdpConcurrently(false)
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