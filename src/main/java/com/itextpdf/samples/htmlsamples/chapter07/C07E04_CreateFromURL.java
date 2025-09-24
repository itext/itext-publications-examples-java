package com.itextpdf.samples.htmlsamples.chapter07;

import ch.qos.logback.classic.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import org.slf4j.LoggerFactory;

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

    private static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; WOW64; " +
            "Trident/4.0; SLCC1; .NET CLR 2.0.50727; Media Center PC 5.0; " +
            ".NET CLR 3.5.21022; .NET CLR 3.5.30729; .NET CLR 3.0.30618; " +
            "InfoPath.2; OfficeLiveConnector.1.3; OfficeLivePatch.0.0)";

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger("ROOT");

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT_LICENSE_FILE_LOCAL_STORAGE")
				+ "/itextkey-html2pdf_typography.json")) {
            LOGGER.info("Load html2pdf + typography license.");
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
        LOGGER.info("Opening URL connection.");
        URLConnection urlConnection = url.openConnection();
        LOGGER.info("Add request property.");
        urlConnection.addRequestProperty("User-Agent", USER_AGENT);
        // 15 seconds timeout
        urlConnection.setConnectTimeout(15 * 1000);
        
        try {
            LOGGER.info("getting URL input stream.");
            InputStream inputStream = urlConnection.getInputStream();
            LOGGER.info("Converting to PDF.");
            HtmlConverter.convertToPdf(inputStream, new FileOutputStream(dest));
        } catch (SocketTimeoutException exception) {
            LOGGER.info("Timeout occurred.");
        } catch (IOException e) {
            try {
                LOGGER.info("Getting response code.");
                ((HttpURLConnection) urlConnection).getResponseCode();
            } catch(IOException innerE) {
                LOGGER.info("Couldn't get response code, retrying.");
            }
        }
    }
}
