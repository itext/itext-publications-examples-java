/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples;

import com.itextpdf.commons.bouncycastle.operator.AbstractOperatorCreationException;
import com.itextpdf.commons.bouncycastle.pkcs.AbstractPKCSException;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.samples.sandbox.signatures.utils.SignaturesCompareTool;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Category(SampleTest.class)
public class SigningSampleTest extends WrappedSamplesRunner {

    /**
     * Global map of classes with ignored areas
     **/
    private static final Map<String, Map<Integer, List<Rectangle>>> ignoredClassesMap;

    static {
        Rectangle signatureIgnoredArea = new Rectangle(150, 660, 100, 80);
        List<Rectangle> rectangles = Collections.singletonList(signatureIgnoredArea);

        Map<Integer, List<Rectangle>> ignoredAreasMap = new HashMap<>();
        ignoredAreasMap.put(1, rectangles);

        ignoredClassesMap = new HashMap<>();
        ignoredClassesMap.put("com.itextpdf.samples.sandbox.signatures.appearance.PadesSignatureAppearanceExample",
                ignoredAreasMap);
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() throws CertificateException, IOException,
            AbstractOperatorCreationException, AbstractPKCSException {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addPackageToRunnerSearchPath("com.itextpdf.samples.sandbox.signatures.pades");
        searchConfig.addPackageToRunnerSearchPath("com.itextpdf.samples.sandbox.signatures.appearance");
        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
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
