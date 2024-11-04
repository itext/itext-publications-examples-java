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

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;

/**
 * Converts a simple HTML file to PDF using an InputStream and an OutputStream
 * as arguments for the convertToPdf() method.
 */
public class C07E06_CreateFromURL3 {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch07/url2pdf_3.pdf";

    /**
     * The target folder for the result.
     */
    public static final String ADDRESS = "https://stackoverflow.com/help/on-topic";

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger("ROOT");

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
				+ "/itextkey-html2pdf_typography.json")) {
            LOGGER.info("Load html2pdf + typography license.");
			LicenseKey.loadLicenseFile(license);
		}
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new C07E06_CreateFromURL3().createPdf(new URL(ADDRESS), DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param url  the URL object for the web page
     * @param dest the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(URL url, String dest) throws IOException {
        LOGGER.info("Initializing variables.");
        ConverterProperties properties = new ConverterProperties();
        MediaDeviceDescription mediaDeviceDescription = new MediaDeviceDescription(MediaType.PRINT);
        properties.setMediaDeviceDescription(mediaDeviceDescription);

        int maxTries = 3;
        InputStream inputStream;

        while (maxTries != 0) {
            LOGGER.info("Tries left " + maxTries);
            LOGGER.info("Opening URL connection.");
            URLConnection urlConnection = url.openConnection();
            LOGGER.info("Add request property.");
            urlConnection.addRequestProperty("User-Agent", USER_AGENT);
            //15 second timeout
            urlConnection.setConnectTimeout(15 * 1000);
            int responseCode;
            try {
                LOGGER.info("getting URL input stream.");
                inputStream = urlConnection.getInputStream();
                LOGGER.info("Converting to PDF.");
                HtmlConverter.convertToPdf(inputStream, new FileOutputStream(dest), properties);
                break;
            } catch (SocketTimeoutException exception) {
                LOGGER.info("Timeout occurred.");
                responseCode = -1;
            }  catch (IOException e) {
                try {
                    LOGGER.info("Getting response code.");
                    responseCode = ((HttpURLConnection) urlConnection).getResponseCode();
                } catch(IOException innerE) {
                    LOGGER.info("Couldn't get response code, retrying.");
                    responseCode = -1;
                }
            }
            assert((responseCode >= 200 && responseCode < 300) || responseCode < 0);
            maxTries--;
        }
    }
}
