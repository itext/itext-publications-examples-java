/*
This file is part of the iText (R) project.
Copyright (c) 1998-2022 iText Group NV
Authors: iText Software.

For more information, please contact iText Software at this address:
sales@itextpdf.com
*/
package com.itextpdf.samples;

import com.itextpdf.io.font.FontCache;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.Version;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.test.pdfa.VeraPdfValidator;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

import java.lang.reflect.Field;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class GenericSampleTest extends WrappedSamplesRunner {

    /**
     * List of samples, which should be validated visually and by link annotations on corresponding pages
     */
    private static final List<String> renderCompareList = Arrays.asList(
            "com.itextpdf.samples.sandbox.tables.CellMethod",
            "com.itextpdf.samples.sandbox.signatures.SignatureExample"
    );

    private static final List<String> veraPdfCompareList = Arrays.asList(
            "com.itextpdf.samples.sandbox.pdfa.HelloPdfA2a",
            "com.itextpdf.samples.sandbox.pdfa.PdfA1a",
            "com.itextpdf.samples.sandbox.pdfa.PdfA1a_images",
            "com.itextpdf.samples.sandbox.pdfa.PdfA3");

    /**
     * List of samples, which require xml files comparison
     */
    private static final List<String> xmlCompareList = Arrays.asList(
            "com.itextpdf.samples.sandbox.acroforms.ReadXFA",
            "com.itextpdf.samples.sandbox.stamper.AddNamedDestinations",
            "com.itextpdf.samples.sandbox.acroforms.CreateXfdf"
    );

    /**
     * List of samples, which require txt files comparison
     */
    private List<String> txtCompareList = Arrays.asList(
            "com.itextpdf.samples.sandbox.interactive.FetchBookmarkTitles",
            "com.itextpdf.samples.sandbox.parse.ParseCustom",
            "com.itextpdf.samples.sandbox.parse.ParseCzech",
            "com.itextpdf.samples.sandbox.logging.CounterDemo",
            "com.itextpdf.samples.sandbox.tagging.WalkTheTree"
    );

    /**
     * List of samples, which require tag comparison
     */
    private static final List<String> tagCompareList = Arrays.asList(
            "com.itextpdf.samples.sandbox.tagging.AddArtifactTable",
            "com.itextpdf.samples.sandbox.tagging.AddStars",
            "com.itextpdf.samples.sandbox.tagging.CreateTaggedDocument"
    );

    /**
     * Global map of classes with ignored areas
     **/
    private static final Map<String, Map<Integer, List<Rectangle>>> ignoredClassesMap;

    static {
        Rectangle latinClassIgnoredArea = new Rectangle(30, 539, 250, 13);
        List<Rectangle> rectangles = Arrays.asList(latinClassIgnoredArea);

        Map<Integer, List<Rectangle>> ignoredAreasMap = new HashMap<>();
        ignoredAreasMap.put(1, rectangles);

        ignoredClassesMap = new HashMap<>();
        ignoredClassesMap.put("com.itextpdf.samples.sandbox.typography.latin.LatinSignature", ignoredAreasMap);
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addPackageToRunnerSearchPath("com.itextpdf.samples.htmlsamples");
        searchConfig.addPackageToRunnerSearchPath("com.itextpdf.samples.sandbox");

        // Samples are run by separate samples runner
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.merge.MergeAndCount");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.security.DecryptPdf");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.security.DecryptPdf2");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.security.EncryptPdf");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.security.EncryptWithCertificate");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.pdfhtml.PdfHtmlResponsiveDesign");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.fonts.MergeAndAddFont");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.parse.ExtractStreams");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.annotations.RemoteGoto");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.annotations.RemoteGoToPage");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.logging.CounterDemoSystemOut");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.htmlsamples.chapter05.C05E03_Invitations");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.htmlsamples.chapter07.C07E04_CreateFromURL");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.htmlsamples.chapter07.C07E05_CreateFromURL2");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.htmlsamples.chapter07.C07E06_CreateFromURL3");

        // Not a sample classes
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.merge.densemerger.PageVerticalAnalyzer");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.merge.densemerger.PdfDenseMerger");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.objects.PdfOnButtonClick");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.pdfhtml.colorblindness");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.pdfhtml.headertagging");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.pdfhtml.qrcodetag");

        // Should not be run due to falling on different systems with different system fonts
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.htmlsamples.chapter06.C06E03_SystemFonts");

        // TODO DEVSIX-3189
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.Tables.TableBorder");

        // TODO DEVSIX-3188
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.tables.SplitRowAtEndOfPage");

        // TODO DEVSIX-3188
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.tables.SplitRowAtSpecificRow");

        // TODO DEVSIX-3187
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.tables.RepeatLastRows");

        // TODO DEVSIX-3187
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.tables.RepeatLastRows2");

        // TODO DEVSIX-3326
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.tables.SplittingNestedTable2");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/all-products.xml");
        FontCache.clearSavedFonts();
        FontProgramFactory.clearRegisteredFonts();

        runSamples();
        unloadLicense();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();

        if (xmlCompareList.contains(sampleClass.getName())) {
            if (!compareTool.compareXmls(dest, cmp)) {
                addError("The XML structures are different.");
            }
        } else if (txtCompareList.contains(sampleClass.getName())) {
            addError(compareTxt(dest, cmp));
        } else if (renderCompareList.contains(sampleClass.getName())) {
            addError(compareTool.compareVisually(dest, cmp, outPath, "diff_"));
            addError(compareTool.compareLinkAnnotations(dest, cmp));
            addError(compareTool.compareDocumentInfo(dest, cmp));
        } else if (ignoredClassesMap.keySet().contains(sampleClass.getName())) {
            addError(compareTool.compareVisually(dest, cmp, outPath, "diff_",
                    ignoredClassesMap.get(sampleClass.getName())));
        } else {
            addError(compareTool.compareByContent(dest, cmp, outPath, "diff_"));
            addError(compareTool.compareDocumentInfo(dest, cmp));
        }
        if (tagCompareList.contains(sampleClass.getName())) {
            addError(compareTool.compareTagStructures(dest, cmp));
        }
        if (veraPdfCompareList.contains(sampleClass.getName())) {
            addError(new VeraPdfValidator().validate(dest));
        }
    }

    private String compareTxt(String dest, String cmp) throws IOException {
        String errorMessage = null;

        try (
                BufferedReader destReader = new BufferedReader(new FileReader(dest));
                BufferedReader cmpReader = new BufferedReader(new FileReader(cmp))
        ) {
            int lineNumber = 1;
            String destLine = destReader.readLine();
            String cmpLine = cmpReader.readLine();
            while (destLine != null || cmpLine != null) {
                if (destLine == null || cmpLine == null) {
                    errorMessage = "The number of lines is different\n";
                    break;
                }

                if (!destLine.equals(cmpLine)) {
                    errorMessage = "Txt files differ at line " + lineNumber
                            + "\n See difference: cmp file: \"" + cmpLine + "\"\n"
                            + "target file: \"" + destLine + "\n";
                }

                destLine = destReader.readLine();
                cmpLine = cmpReader.readLine();
                lineNumber++;
            }
        }

        return errorMessage;
    }

    private void unloadLicense() {
        try {
            Field validators = LicenseKey.class.getDeclaredField("validators");
            validators.setAccessible(true);
            validators.set(null, null);
            Field versionField = Version.class.getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(null, null);
        } catch (Exception ignored) {

            // No exception handling required, because there can be no license loaded
        }
    }
}
