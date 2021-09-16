/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2021 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples;

import com.itextpdf.io.font.FontCache;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class InvitationsSampleTest extends WrappedSamplesRunner {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.htmlsamples.chapter05.C05E03_Invitations");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/all-products.json")) {
            LicenseKey.loadLicenseFile(license);
        }
        FontCache.clearSavedFonts();
        FontProgramFactory.clearRegisteredFonts();

        runSamples();
        LicenseKey.unloadLicenses();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();
        for (int i = 1; i <= 3; i++) {
            String currentDest = String.format(dest, i);
            String currentCmp = String.format(cmp, i);

            Rectangle ignoredArea = new Rectangle(30, 700, 120, 18);
            List<Rectangle> rectangles = Arrays.asList(ignoredArea);
            Map<Integer, List<Rectangle>> ignoredAreasMap = new HashMap<>();
            ignoredAreasMap.put(1, rectangles);
            addError(compareTool.compareVisually(currentDest, currentCmp, outPath, "diff_",
                    ignoredAreasMap));
            addError(compareTool.compareDocumentInfo(currentDest, currentCmp));
        }
    }
}
