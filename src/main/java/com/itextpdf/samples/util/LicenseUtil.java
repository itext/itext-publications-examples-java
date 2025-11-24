package com.itextpdf.samples.util;

/**
 * The class is used to simplify running samples locally by specifying paths to license files
 */
public class LicenseUtil {

    private final static String INVALID_LICENSE_FOLDER_PATH = "To run the samples locally, provide the absolute path to your %s license file - either directly or by updating %s method.";

    /**
     * Method is used to get absolute path to iTextCore, PdfHtml, PdfCalligraph license file.
     * Used to run samples locally.
     * If you want to run samples locally, you could either replace in sample code calling this method to absolute path of your license file or update the method.
     *
     * @return Result license file name.
     */
    public static String getPathToLicenseFileWithITextCoreAndPdfHtmlAndPdfCalligraphProducts() {
        String licencePath = System.getenv("ITEXT_LICENSE_FILE_LOCAL_STORAGE");
        if (licencePath == null) {
            throw new IllegalArgumentException(String.format(INVALID_LICENSE_FOLDER_PATH, "iTextCore, PdfHtml, PdfCalligraph", "getPathToLicenseFileWithITextCoreAndPdfHtmlAndPdfCalligraphProducts"));
        }
        return licencePath + "/dev_all_products.json";
    }

    /**
     * Method is used to get absolute path to iTextCore, PdfCalligraph license file.
     * Used to run samples locally.
     * If you want to run samples locally, you could either replace in sample code calling this method to absolute path of your license file or update the method.
     *
     * @return Result license file name.
     */
    public static String getPathToLicenseFileWithITextCoreAndPdfCalligraphProducts() {
        String licencePath = System.getenv("ITEXT_LICENSE_FILE_LOCAL_STORAGE");
        if (licencePath == null) {
            throw new IllegalArgumentException(String.format(INVALID_LICENSE_FOLDER_PATH, "iTextCore, PdfCalligraph", "getPathToLicenseFileWithITextCoreAndPdfCalligraphProducts"));
        }
        return licencePath + "/dev_all_products.json";
    }

}
