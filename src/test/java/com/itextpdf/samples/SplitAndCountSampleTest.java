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
public class SplitAndCountSampleTest extends WrappedSamplesRunner {

    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.split.SplitAndCount");

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

        for (int i = 1; i < 8; i++) {
            String currentDest = String.format(dest, i);
            String currentCmp = String.format(cmp, i);

            addError(compareTool.compareByContent(currentDest, currentCmp, outPath, "diff_"));
            addError(compareTool.compareDocumentInfo(currentDest, currentCmp));
        }
    }
}
