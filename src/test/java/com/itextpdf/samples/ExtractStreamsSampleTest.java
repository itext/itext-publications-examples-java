package com.itextpdf.samples;

import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class ExtractStreamsSampleTest extends WrappedSamplesRunner {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.parse.ExtractStreams");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
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
            Assert.assertArrayEquals(cmpBytes, destBytes);
        } catch (AssertionError exc) {
            errorMessage = "Files are not equal.";
        }

        return errorMessage;
    }
}
