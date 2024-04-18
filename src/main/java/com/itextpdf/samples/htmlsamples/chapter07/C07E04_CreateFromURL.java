package com.itextpdf.samples.htmlsamples.chapter07;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import org.junit.Assert;
import java.net.HttpURLConnection;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensing.base.LicenseKey;

/**
 * Converts a simple HTML file to PDF using an InputStream and an OutputStream
 * as arguments for the convertToPdf() method.
 */
public class C07E04_CreateFromURL {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch07/url2pdf_1.pdf";

    /**
     * The target folder for the result.
     */
    public static final String ADDRESS = "https://stackoverflow.com/help/on-topic";

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
				+ "/itextkey-html2pdf_typography.json")) {
			LicenseKey.loadLicenseFile(license);
		}
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new C07E04_CreateFromURL().createPdf(new URL(ADDRESS), DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param url  the URL object for the web page
     * @param dest the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(URL url, String dest) throws IOException {
        int maxTries = 3;

        while (maxTries != 0) {
            int responseCode;
            URLConnection urlConnection = url.openConnection();
            urlConnection.addRequestProperty("User-Agent", USER_AGENT);
            // 15 seconds timeout
            urlConnection.setConnectTimeout(15 * 1000);
            try {
                InputStream inputStream = urlConnection.getInputStream();
                HtmlConverter.convertToPdf(inputStream, new FileOutputStream(dest));
                break;
            } catch (SocketTimeoutException exception) {
                //Time-out occurred
                responseCode = -1;
            } catch (IOException e) {
                try {
                    responseCode = ((HttpURLConnection) urlConnection).getResponseCode();
                } catch(IOException innerE) {
                    // If we couldn't get error code we still want to retry
                    responseCode = -1;
                }
            }
            Assert.assertTrue("Http request was not successful. Error code: " + responseCode,
                    (responseCode >= 200 && responseCode < 300) || responseCode < 0);
            maxTries--;
        }
    }
}
