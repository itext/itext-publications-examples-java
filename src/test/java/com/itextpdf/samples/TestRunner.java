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
    private List<String> renderCompareList = Arrays.asList();

    /**
     * List of samples, that requires xml files comparison
     */
    private List<String> xmlCompareList = Arrays.asList("com.itextpdf.samples.sandbox.acroforms.ReadXFA");

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.acroforms.FillXFA");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.acroforms.FillXFA2");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.acroforms.ReadXFA");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.acroforms.RemoveXFA");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 120000)
    public void test() throws Exception {
        unloadLicense();
        runSamples();
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
