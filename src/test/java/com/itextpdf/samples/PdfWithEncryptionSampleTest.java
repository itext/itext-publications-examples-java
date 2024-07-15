package com.itextpdf.samples;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("SampleTest")
public class PdfWithEncryptionSampleTest extends WrappedSamplesRunner {

    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.security.EncryptPdf");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.security.DecryptPdf2");

        return generateTestsList(searchConfig);
    }

    @Timeout(unit = TimeUnit.MILLISECONDS, value = 60000)
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("data")
    public void test(RunnerParams data) throws Exception {
        this.sampleClassParams = data;
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
