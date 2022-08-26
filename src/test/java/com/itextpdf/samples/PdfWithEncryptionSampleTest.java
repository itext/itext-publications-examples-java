/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;

import java.util.Collection;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class PdfWithEncryptionSampleTest extends WrappedSamplesRunner {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.security.EncryptPdf");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.security.DecryptPdf2");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
        LicenseKey.unloadLicenses();
        runSamples();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();
        compareTool.enableEncryptionCompare();

        addError(compareTool.compareByContent(dest, cmp, outPath, "diff_",
                "World".getBytes(), "World".getBytes()));
        addError(compareTool.compareDocumentInfo(dest, cmp, "World".getBytes(), "World".getBytes()));
    }
}
