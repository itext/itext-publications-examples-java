package com.itextpdf.samples;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("SampleTest")
public class StandardOutWithPdfSampleTest extends WrappedSamplesRunner {

    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.security.DecryptPdf");

        return generateTestsList(searchConfig);
    }

    @Timeout(unit = TimeUnit.MILLISECONDS, value = 60000)
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("data")
    public void test(RunnerParams data) throws Exception {
        this.sampleClassParams = data;
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/all-products.json")) {
            LicenseKey.loadLicenseFile(license);
        }

        initClass();
        // By default target dir is created during runSamples method. In this particular case we need this
        // action before due to overriding system out stream
        new File(getDest()).getParentFile().mkdirs();

        PrintStream storedSystemOut = System.out;
        try (PrintStream printStream = new PrintStream(getDest().replace(".pdf", "_sout.txt"))) {
            System.setOut(printStream);
            runSamples();
        } finally {
            System.setOut(storedSystemOut);
            LicenseKey.unloadLicenses();
        }
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        addError(compareSystemOut(dest, cmp));

        CompareTool compareTool = new CompareTool();
        addError(compareTool.compareByContent(dest, cmp, outPath, "diff_"));
        addError(compareTool.compareDocumentInfo(dest, cmp));
    }

    private String compareSystemOut(String dest, String cmp) throws IOException {
        System.out.flush();

        String destSystemOut = dest.replace(".pdf", "_sout.txt");
        String cmpSystemOut = cmp.replace(".pdf", "_sout.txt");

        try (BufferedReader destReader = new BufferedReader(new FileReader(destSystemOut));
                BufferedReader cmpReader = new BufferedReader(new FileReader(cmpSystemOut))) {
            for (int lineIndex = 1; true; ++lineIndex) {
                String destLine = destReader.readLine();
                String cmpLine = cmpReader.readLine();

                if (destLine == null && cmpLine == null) {
                    return null;
                } else if (destLine == null || cmpLine == null) {
                    return "The number of lines is different\n";
                } else if (!cmpLine.equals(destLine)) {
                    return String.format("Result differs at line %d\nExpected: \"%s\"\nActual: \"%s\"",
                            lineIndex, cmpLine, destLine);
                }
            }
        }
    }
}
