/*
This file is part of the iText (R) project.
Copyright (c) 1998-2022 iText Group NV
Authors: iText Software.

For more information, please contact iText Software at this address:
sales@itextpdf.com
*/
package com.itextpdf.samples;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.itextpdf.io.font.FontCache;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.Version;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Category(SampleTest.class)
public class CreateFromURLSampleTest extends WrappedSamplesRunner {
    private static final Map<String, Integer[]> expectedNumbersOfPages;

    static {
        expectedNumbersOfPages = new HashMap<>();

        expectedNumbersOfPages.put("com.itextpdf.samples.htmlsamples.chapter07.C07E04_CreateFromURL", new Integer[] {2, 4});
        expectedNumbersOfPages.put("com.itextpdf.samples.htmlsamples.chapter07.C07E05_CreateFromURL2", new Integer[] {3, 4});
        expectedNumbersOfPages.put("com.itextpdf.samples.htmlsamples.chapter07.C07E06_CreateFromURL3", new Integer[] {3, 4, 5});
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.htmlsamples.chapter07.C07E04_CreateFromURL");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.htmlsamples.chapter07.C07E05_CreateFromURL2");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.htmlsamples.chapter07.C07E06_CreateFromURL3");

        return generateTestsList(searchConfig);
    }

    /**
     * This test is expected to be flaky, because its output depends on the content of the site page,
     * which could be changed at any time
     */
    @Test(timeout = 60000)
    public void test() throws Exception {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/all-products.xml");
        FontCache.clearSavedFonts();
        FontProgramFactory.clearRegisteredFonts();

        Logger logger = (Logger) LoggerFactory.getLogger("ROOT");
        Appender<ILoggingEvent> loggerAppender = logger.getAppender("console");
        if (null != loggerAppender) {
            loggerAppender.stop();

            runSamples();

            loggerAppender.start();
        } else {
            runSamples();
        }

        unloadLicense();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(dest));

        int currentNumberOfPages = pdfDoc.getNumberOfPages();
        List<Integer> expectedNumberOfPages = Arrays.asList(expectedNumbersOfPages.get(sampleClass.getName()));
        if (!expectedNumberOfPages.contains(currentNumberOfPages)) {
            addError("Number of pages is out of expected range.\nActual: " + currentNumberOfPages);
        }

        // Get words to check the resultant pdf file
        String compareFilePath = "./cmpfiles/txt/cmp_" + sampleClass.getSimpleName() + "_keywords.txt";
        String compareContent = readFile(compareFilePath);
        List<String> cmpPdfWords = Arrays.asList(compareContent.split("[|]"));

        // Get all words from all pages of the resultant pdf file
        List<String> destPdfWords = new ArrayList<>();
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            String pageContentString = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i),
                    new LocationTextExtractionStrategy());
            List<String> pageWords = Arrays.asList(pageContentString.replace("\n", " ")
                    .split(" "));
            destPdfWords.addAll(pageWords);
        }

        StringBuilder errorMessage = new StringBuilder();
        for (String word : cmpPdfWords) {
            if (!destPdfWords.contains(word)) {
                errorMessage.append(word).append(",");
            }
        }

        String errorText = errorMessage.toString();
        if (!errorText.equals("")) {
            addError("Some words are missing in the result pdf: " + errorText);
        }
    }

    private String readFile(String filePath) throws Exception {
        StringBuilder contentString = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while (line != null) {
                contentString.append(line);
                line = reader.readLine();
            }
        }

        return contentString.toString();
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
