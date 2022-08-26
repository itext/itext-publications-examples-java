/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples;

import com.itextpdf.io.util.UrlUtil;
import com.itextpdf.kernel.Version;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.kernel.utils.CompareTool.CompareResult;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Collection;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class SystemOutRunnerSampleTest extends WrappedSamplesRunner {
    private PrintStream STORED_SYSTEM_OUT = null;

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.logging.CounterDemoSystemOut");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
        initClass();
        STORED_SYSTEM_OUT = System.out;

        // By default target dir is created during runSamples method. In this particular case we need this
        // action before due to overriding system out stream
        new File(getDest()).getParentFile().mkdirs();

        System.setOut(new PrintStream(getStringField(sampleClass, "DEST").replace(".pdf", ".txt")));

        runSamples();
        unloadLicense();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws IOException, InterruptedException {

        // Make sure that the stream's content is flushed
        System.out.flush();

        System.setOut(STORED_SYSTEM_OUT);
        String dest_txt = dest.replace(".pdf", ".txt");
        String cmp_txt = cmp.replace(".pdf", ".txt");

        System.out.println("Out txt file: " + UrlUtil.getNormalizedFileUriString(dest_txt));
        System.out.println("Cmp txt file: " + UrlUtil.getNormalizedFileUriString(cmp_txt)+ "\n");

        addError(compareTxt(dest_txt, cmp_txt));
    }

    private String compareTxt(String dest, String cmp) throws IOException {
        String errorMessage = null;

        try (
                BufferedReader destReader = new BufferedReader(new FileReader(dest));
                BufferedReader cmpReader = new BufferedReader(new FileReader(cmp))
        ) {
            int lineNumber = 1;
            String destLine = destReader.readLine();
            String cmpLine = cmpReader.readLine();
            while (destLine != null || cmpLine != null) {
                if (destLine == null || cmpLine == null) {
                    errorMessage = "The number of lines is different\n";
                    break;
                }

                if (!destLine.equals(cmpLine)) {
                    errorMessage = "Txt files differ at line " + lineNumber
                            + "\n See difference: cmp file: \"" + cmpLine + "\"\n"
                            + "target file: \"" + destLine + "\n";
                }

                destLine = destReader.readLine();
                cmpLine = cmpReader.readLine();
                lineNumber++;
            }
        }

        return errorMessage;
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
