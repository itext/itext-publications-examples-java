package com.itextpdf.samples;

import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("SampleTest")
public class ExtractStreamsSampleTest extends WrappedSamplesRunner {

    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.parse.ExtractStreams");

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
        for (int i = 1; i < 3; i++) {
            String currentDest = String.format(dest + "/extract_streams%s.dat", i);
            String currentCmp = String.format(cmp + "/cmp_extract_streams%s.dat", i);

            addError(compareFiles(currentDest, currentCmp));
        }
    }

    @Override
    protected String getCmpPdf(String dest) {
        if (dest == null) {
            return null;
        }

        return "./cmpfiles/" + dest.substring(8);
    }

    private String compareFiles(String dest, String cmp) throws IOException {
        String errorMessage = null;

        RandomAccessFile file = new RandomAccessFile(dest, "r");
        byte[] destBytes = new byte[(int)file.length()];
        file.readFully(destBytes);
        file.close();

        file = new RandomAccessFile(cmp, "r");
        byte[] cmpBytes = new byte[(int)file.length()];
        file.readFully(cmpBytes);
        file.close();

        try {
            Assertions.assertArrayEquals(cmpBytes, destBytes);
        } catch (AssertionError exc) {
            errorMessage = "Files are not equal.";
        }

        return errorMessage;
    }
}
