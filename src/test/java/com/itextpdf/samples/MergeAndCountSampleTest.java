/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples;

import com.itextpdf.kernel.Version;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.samples.sandbox.merge.MergeAndCount;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

import java.lang.reflect.Field;
import java.util.Collection;

@Category(SampleTest.class)
public class MergeAndCountSampleTest extends WrappedSamplesRunner {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.merge.MergeAndCount");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
        unloadLicense();
        runSamples();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();

        for (int i = 1; i < 8; i++) {
            String currentDest = String.format(dest, i);
            String currentCmp = String.format(cmp, i);

            addError(compareTool.compareByContent(currentDest, currentCmp, outPath, "diff_"));
            addError(compareTool.compareDocumentInfo(currentDest, currentCmp));
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

            // No exception handling required, because there can be no license loaded
        }
    }
}
