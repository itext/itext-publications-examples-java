/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples;

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

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class CreateFromURLSampleTest extends WrappedSamplesRunner {
    private static final Map<String, Integer> expectedNumbersOfPages;

    static {
        expectedNumbersOfPages = new HashMap<>();

        expectedNumbersOfPages.put("com.itextpdf.samples.htmlsamples.chapter07.C07E04_CreateFromURL", 5);
        expectedNumbersOfPages.put("com.itextpdf.samples.htmlsamples.chapter07.C07E05_CreateFromURL2", 2);
        expectedNumbersOfPages.put("com.itextpdf.samples.htmlsamples.chapter07.C07E06_CreateFromURL3", 2);
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.htmlsamples.chapter07.C07E04_CreateFromURL");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.htmlsamples.chapter07.C07E05_CreateFromURL2");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.htmlsamples.chapter07.C07E06_CreateFromURL3");

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
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(dest));

        int currentNumberOfPages = pdfDoc.getNumberOfPages();
        int expectedNumberOfPages = expectedNumbersOfPages.get(sampleClass.getName());
        if (currentNumberOfPages != expectedNumberOfPages) {
            addError("Numbers of pages are not equal.\nExpected: " + expectedNumberOfPages
                    + "\nActual: " + currentNumberOfPages);
        }

        String compareFilePath = "./cmpfiles/txt/cmp_" + sampleClass.getSimpleName() + "_keywords.txt";
        String compareContent = readFile(compareFilePath);
        String[] comparePagesContent = compareContent.split(";");

        // Get the content words of the first page and compare it with expected content words
        String firstPageContentString = PdfTextExtractor.getTextFromPage(pdfDoc.getFirstPage(),
                new LocationTextExtractionStrategy());
        List<String> firstPageWords = Arrays.asList(firstPageContentString.split("\n"));
        List<String> firstPageWordsToCompare = Arrays.asList(comparePagesContent[0].split("[|]"));
        if (!firstPageWords.containsAll(firstPageWordsToCompare)) {
            addError("Some of the expected words do not present on the first page");
        }

        // Get the content words of the last page and compare it with expected content words
        String lastPageContentString = PdfTextExtractor.getTextFromPage(pdfDoc.getLastPage(),
                new LocationTextExtractionStrategy());
        List<String> lastPageWords = Arrays.asList(lastPageContentString.split("\n"));
        List<String> lastPageWordsToCompare = Arrays.asList(comparePagesContent[1].split("[|]"));
        if (!lastPageWords.containsAll(lastPageWordsToCompare)) {
            addError("Some of the expected words do not present on the last page");
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
