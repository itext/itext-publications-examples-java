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
import com.itextpdf.samples.sandbox.annotations.RemoteGoto;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

import java.lang.reflect.Field;
import java.util.Collection;

@Category(SampleTest.class)
public class RemoteGoToSampleTest extends WrappedSamplesRunner {
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.annotations.RemoteGoto");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.annotations.RemoteGoToPage");

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
        String[] names = getDestNames(sampleClass);

        for (String fileName : names) {
            String currentDest = dest + fileName;
            String temp = cmp + fileName;
            int i = temp.lastIndexOf("/");
            String currentCmp = temp.substring(0, i + 1) + "cmp_" + temp.substring(i + 1);

            addError(compareTool.compareByContent(currentDest, currentCmp, outPath, "diff_"));
            addError(compareTool.compareDocumentInfo(currentDest, currentCmp));
        }
    }

    @Override
    protected String getCmpPdf(String dest) {
        if (dest == null) {
            return null;
        }

        return "./cmpfiles/" + dest.substring(8);
    }

    private static String[] getDestNames(Class<?> c) {
        try {
            Field field = c.getField("DEST_NAMES");
            if (field == null) {
                return null;
            }

            Object obj = field.get(null);
            if (obj == null || !(obj instanceof String[])) {
                return null;
            }

            return (String[]) obj;
        } catch (Exception e) {
            return null;
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
