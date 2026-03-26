package com.itextpdf.samples;

import com.itextpdf.io.font.FontCache;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.util.UrlUtil;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//These tests are separated as they require a longer timeout
@Tag("SampleTest")
public class PdfOcrOnnxTest extends WrappedSamplesRunner {

    /**
     * List of samples, which require txt files comparison
     */
    private final List<String> txtCompareList = Arrays.asList(
            "com.itextpdf.samples.sandbox.pdfocr.onnx.PdfOcrOnnxTxtFileExample"
    );

    /**
     * Global map of classes with ignored areas
     **/
    private static final Map<String, Map<Integer, List<Rectangle>>> ignoredClassesMap;

    static {
        ignoredClassesMap = new HashMap<>();
        // Output PDFs are different in Windows and Linux (in float values), but visually they're the same.
        ignoredClassesMap.put("com.itextpdf.samples.sandbox.pdfocr.onnx.PdfOcrOnnxTextPositioningExample",
                new HashMap<>());
    }

    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addPackageToRunnerSearchPath("com.itextpdf.samples.sandbox.pdfocr.onnx");

        return generateTestsList(searchConfig);
    }

    @Timeout(unit = TimeUnit.MILLISECONDS, value = 180000)
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("data")
    public void test(RunnerParams data) throws Exception {
        this.sampleClassParams = data;
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT_LICENSE_FILE_LOCAL_STORAGE")
                + "/dev_all_products.json")) {
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

        if (txtCompareList.contains(sampleClass.getName())) {
            addError(compareTxt(dest, cmp));
        } else if (ignoredClassesMap.keySet().contains(sampleClass.getName())) {
            addError(compareTool.compareVisually(dest, cmp, outPath, "diff_",
                    ignoredClassesMap.get(sampleClass.getName())));
        } else {
            addError(compareTool.compareByContent(dest, cmp, outPath, "diff_"));
        }
    }

    private String compareTxt(String dest, String cmp) throws IOException {
        String errorMessage = null;
        System.out.println("Out txt: " + UrlUtil.getNormalizedFileUriString(dest));
        System.out.println("Cmp txt: " + UrlUtil.getNormalizedFileUriString(cmp) + "\n");

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
}
