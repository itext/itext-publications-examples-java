package com.itextpdf.samples.utils.verapdf;

import com.itextpdf.io.util.UrlUtil;
import org.verapdf.core.VeraPDFException;
import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.metadata.fixer.MetadataFixerConfig;
import org.verapdf.pdfa.VeraGreenfieldFoundryProvider;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;
import org.verapdf.processor.BatchProcessor;
import org.verapdf.processor.ProcessorConfig;
import org.verapdf.processor.ProcessorFactory;
import org.verapdf.processor.TaskType;
import org.verapdf.processor.plugins.PluginsCollectionConfig;
import org.verapdf.processor.FormatOption;
import org.verapdf.processor.reports.BatchSummary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;

public class VeraPdfValidator {

    public String validate(String filePath) {
        String errorMessage = null;

        try {
            File xmlReport = new File(filePath.replace(".pdf", ".xml"));
            VeraGreenfieldFoundryProvider.initialise();

            // Initializes default VeraPDF configurations
            ProcessorConfig customProfile = ProcessorFactory.defaultConfig();
            FeatureExtractorConfig featuresConfig = customProfile.getFeatureConfig();
            ValidatorConfig valConfig = customProfile.getValidatorConfig();
            PluginsCollectionConfig plugConfig = customProfile.getPluginsCollectionConfig();
            MetadataFixerConfig metaConfig = customProfile.getFixerConfig();
            ProcessorConfig resultConfig = ProcessorFactory.fromValues(valConfig, featuresConfig,
                    plugConfig, metaConfig, EnumSet.of(TaskType.VALIDATE));

            // Creates validation processor
            BatchProcessor processor = ProcessorFactory.fileBatchProcessor(resultConfig);

            BatchSummary summary = processor.process(Collections.singletonList(new File(filePath)),
                    ProcessorFactory.getHandler(FormatOption.XML, true,
                            new FileOutputStream(String.valueOf(xmlReport)), 125, false));

            String xmlReportPath = UrlUtil.toNormalizedURI(xmlReport.getAbsolutePath()).getPath();

            if (summary.getFailedParsingJobs() != 0) {
                errorMessage = "An error occurred while parsing current file. See report:  file:///" + xmlReportPath;
            } else if (summary.getFailedEncryptedJobs() != 0) {
                errorMessage = "VeraPDF execution failed - specified file is encrypted. See report:  file:///" + xmlReportPath;
            } else if (summary.getValidationSummary().getNonCompliantPdfaCount() != 0) {
                errorMessage = "VeraPDF verification failed. See verification results:  file:///" + xmlReportPath;
            } else {
                System.out.println("VeraPDF verification finished. See verification report: file:///" + xmlReportPath);
            }
        } catch (IOException | VeraPDFException exc) {
            errorMessage = "VeraPDF execution failed:\n" + exc.getMessage();
        }

        return errorMessage;
    }
}