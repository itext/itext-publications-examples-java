package com.itextpdf.samples;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.samples.sandbox.pdfhtml.PdfHtmlResponsiveDesign;
import com.itextpdf.styledxmlparser.css.util.CssDimensionParsingUtils;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.FileInputStream;
import java.util.Collection;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

@Category(SampleTest.class)
public class PdfHtmlResponsiveSampleTest extends WrappedSamplesRunner {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.pdfhtml.PdfHtmlResponsiveDesign");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/all-products.json")) {
            LicenseKey.loadLicenseFile(license);
        }
        FontProgramFactory.clearRegisteredFonts();

        runSamples();
        LicenseKey.unloadLicenses();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();

        for (int i = 0; i < PdfHtmlResponsiveDesign.pageSizes.length; i++) {
            float width = CssDimensionParsingUtils.parseAbsoluteLength(Float.toString(PdfHtmlResponsiveDesign.pageSizes[i].getWidth()));
            String currentDest = dest.replace("<filename>", "responsive_" + width + ".pdf");
            String currentCmp = cmp.replace("<filename>", "responsive_" + width + ".pdf");

            addError(compareTool.compareByContent(currentDest, currentCmp, outPath, "diff_"));
            addError(compareTool.compareDocumentInfo(currentDest, currentCmp));
        }
    }
}
