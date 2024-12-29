package com.itextpdf.samples;

import com.itextpdf.commons.bouncycastle.operator.AbstractOperatorCreationException;
import com.itextpdf.commons.bouncycastle.pkcs.AbstractPKCSException;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.samples.sandbox.signatures.utils.SignaturesCompareTool;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag("SampleTest")
public class SigningSampleTest extends WrappedSamplesRunner {

    /**
     * Global map of classes with ignored areas
     **/
    private static final Map<String, Map<Integer, List<Rectangle>>> ignoredClassesMap;

    static {
        Map<Integer, List<Rectangle>> ignoredAreasMap1 = new HashMap<>();
        ignoredAreasMap1.put(1, Collections.singletonList(new Rectangle(150, 660, 100, 80)));
        Map<Integer, List<Rectangle>> ignoredAreasMap2 = new HashMap<>();
        ignoredAreasMap2.put(1, Collections.singletonList(new Rectangle(50, 450, 200, 200)));

        ignoredClassesMap = new HashMap<>();
        ignoredClassesMap.put("com.itextpdf.samples.sandbox.signatures.appearance.PadesSignatureAppearanceExample",
                ignoredAreasMap1);
        ignoredClassesMap.put("com.itextpdf.samples.sandbox.signatures.appearance.SignatureAppearanceExample",
                ignoredAreasMap2);
    }

    public static Collection<Object[]> data() throws CertificateException, IOException,
            AbstractOperatorCreationException, AbstractPKCSException {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addPackageToRunnerSearchPath("com.itextpdf.samples.sandbox.signatures.pades");
        searchConfig.addPackageToRunnerSearchPath("com.itextpdf.samples.sandbox.signatures.appearance");
        searchConfig.addPackageToRunnerSearchPath("com.itextpdf.samples.sandbox.signatures.twophase");
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.signatures.signaturetag.ConvertToPdfAndSign");
        return generateTestsList(searchConfig);
    }

    @Timeout(unit = TimeUnit.MILLISECONDS, value = 60000)
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("data")
    public void test(RunnerParams data) throws Exception {
        this.sampleClassParams = data;
        try (FileInputStream allLicense = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY") + "/all-products.json")) {
            LicenseKey.loadLicenseFile(allLicense);
        }
        runSamples();
        LicenseKey.unloadLicenses();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();
        if (ignoredClassesMap.containsKey(sampleClass.getName())) {
            addError(compareTool.compareVisually(dest, cmp, outPath, "diff_",
                    ignoredClassesMap.get(sampleClass.getName())));
        } else {
            addError(compareTool.compareVisually(dest, cmp, outPath, "diff_"));
        }
        addError(SignaturesCompareTool.compareSignatures(dest, cmp));
    }
}
