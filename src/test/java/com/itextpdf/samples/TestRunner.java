/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples;

import com.itextpdf.kernel.Version;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;

import java.lang.reflect.Field;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class TestRunner extends WrappedSamplesRunner {

    /**
     * List of samples, that should be validated visually and by link annotations on corresponding pages
     */
    private List<String> renderCompareList = Arrays.asList("com.itextpdf.samples.sandbox.tables.CellMethod");

    /**
     * List of samples, that requires xml files comparison
     */
    private List<String> xmlCompareList = Arrays.asList(
            "com.itextpdf.samples.sandbox.acroforms.ReadXFA",
            "com.itextpdf.samples.sandbox.stamper.AddNamedDestinations"
    );

    /**
     * List of samples, that requires tag comparison
     */
    private List<String> tagCompareList = Arrays.asList(
            "com.itextpdf.samples.sandbox.tagging.AddArtifactTable",
            "com.itextpdf.samples.sandbox.tagging.AddStars",
            "com.itextpdf.samples.sandbox.tagging.CreateTaggedDocument"
    );

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addPackageToRunnerSearchPath("com.itextpdf.samples.sandbox");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.Listing_99_01_DifferentLayouts");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.Listing_99_02_ComplexDocumentLayout");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.Listing_99_03_ComplexElementLayout");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.Listing_99_04_PageSizeAndMargins");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.Listing_99_05_BarcodeLayout");

        // Samples are run by separate samples runner
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.merge.MergeAndCount");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.security.EncryptPdf");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.security.EncryptWithCertificate");

        // Not a sample classes
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.fonts.tutorial.F99_ConvertToUnicodeNotation");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.merge.PageVerticalAnalyzer");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.merge.PdfDenseMerger");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.objects.PdfOnButtonClick");

        // TODO DEVSIX-3105
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.interactive.FetchBookmarkTitles");

        // TODO DEVSIX-3179
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.fonts.MergeAndAddFont");

        // TODO DEVSIX-466
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.tables.SplittingNestedTable2");

        // TODO DEVSIX-3146
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.tables.CustomBorder");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.tables.CustomBorder2");

        // TODO DEVSIX-3099
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.acroforms.CheckBoxValues");

        // TODO DEVSIX-526
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.acroforms.ImportXFDF");

        // TODO DEVSIX-3106
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.parse.ExtractStreams");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.parse.ParseCzech");
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.parse.ParseCustom");

        // TODO DEVSIX-3107
        searchConfig.ignorePackageOrClass("com.itextpdf.samples.sandbox.security.GetN2fromSig");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 120000)
    public void test() throws Exception {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");
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
        } else if (renderCompareList.contains(sampleClass.getName())) {
            addError(compareTool.compareVisually(dest, cmp, outPath, "diff_"));
            addError(compareTool.compareLinkAnnotations(dest, cmp));
            addError(compareTool.compareDocumentInfo(dest, cmp));
        } else {
            addError(compareTool.compareByContent(dest, cmp, outPath, "diff_"));
            addError(compareTool.compareDocumentInfo(dest, cmp));
        }

        if (tagCompareList.contains(sampleClass.getName())) {
            addError(compareTool.compareTagStructures(dest, cmp));
        }
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
        }
    }
}
