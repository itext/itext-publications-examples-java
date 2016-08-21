/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples;

import ch.qos.logback.classic.Logger;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.LoggerFactory;

import javax.management.OperationsException;
import java.io.File;
import java.lang.reflect.Field;

import static org.junit.Assert.assertNull;

@Category(SampleTest.class)
public class GenericTest {

    /**
     * The logger class
     */
    protected static final Logger LOGGER = (Logger) LoggerFactory.getLogger(GenericTest.class.getName());

    protected boolean compareRenders = false;

    protected boolean compareXml = false;
    /**
     * An error message
     */
    protected String errorMessage;
    /**
     * A prefix that is part of the error message.
     */
    protected String differenceImagePrefix = "difference";

    /**
     * Gets triggered before the test is performed.
     * When writing tests, you need to override this method to set
     * the klass variable (using the setKlass() method)
     */
    /*@Before
    public void setup() {

    }*/
    protected void beforeManipulatePdf() {
    }

    protected void afterManipulatePdf() {
    }

    protected void setCompareRenders(boolean compareRenders) {
        this.compareRenders = compareRenders;
    }

    /**
     * Tests the example.
     * If SRC and DEST are defined, the example manipulates a PDF;
     * if only DEST is defined, the example creates a PDF.
     */
    @Test// (timeout = 120000)
    public void test() throws Exception {
        if (this.getClass().getName().equals(GenericTest.class.getName()))
            return;
        LOGGER.info("Starting test " + getClass().getName() + ".");
        // Getting the destination PDF file (must be there!)
        String dest = getDest();
        if (dest == null || dest.length() == 0)
            throw new OperationsException("DEST cannot be empty!");
        // Compare the destination PDF with a reference PDF
        beforeManipulatePdf();
        manipulatePdf(dest);
        afterManipulatePdf();
        System.out.println(dest + "\n" + getCmpPdf());
        comparePdf(dest, getCmpPdf());
        LOGGER.info("Test complete.");
    }

    /**
     * Manupulates a PDF by invoking the manipulatePdf() method in the
     * original sample class.
     *
     * @param    dest    the resulting PDF
     */
    protected void manipulatePdf(String dest) throws Exception {
    }

    /**
     * Gets the path to the resulting PDF from the sample class;
     * this method also creates directories if necessary.
     *
     * @return a path to a resulting PDF
     */
    protected String getDest() {
        String dest = getStringField("DEST");
        if (dest != null) {
            File file = new File(dest);
            file.getParentFile().mkdirs();
        }
        return dest;
    }

    /**
     * Returns a string value that is stored as a static variable
     * inside an example class.
     *
     * @param name the name of the variable
     * @return the value of the variable
     */
    protected String getStringField(String name) {
        try {
            Field field = getClass().getField(name);
            if (field == null) {
                return null;
            }
            Object obj = field.get(null);
            if (obj == null || !(obj instanceof String))
                return null;
            return (String) obj;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Compares two PDF files using iText's CompareTool.
     *
     * @param    dest    the PDF that resulted from the test
     * @param    cmp        the reference PDF
     */
    protected void comparePdf(String dest, String cmp) throws Exception {
        if (cmp == null || cmp.length() == 0) return;
        CompareTool compareTool = new CompareTool();
        String outPath = new File(dest).getParent();
        new File(outPath).mkdirs();
        if (compareXml) {
            if (!compareTool.compareXmls(dest, cmp)) {
                addError("The XML structures are different.");
            }
        } else {
            if (compareRenders) {
                addError(compareTool.compareVisually(dest, cmp, outPath, differenceImagePrefix));
                addError(compareTool.compareLinkAnnotations(dest, cmp));
            } else {
                addError(compareTool.compareByContent(dest, cmp, outPath, differenceImagePrefix));
            }
            addError(compareTool.compareDocumentInfo(dest, cmp));
        }

        assertNull(errorMessage);
    }

    /**
     * Every test needs to know where to find its reference file.
     */
    protected String getCmpPdf() {
        String tmp = getDest();
        if (tmp == null) {
            return null;
        }
        int i = tmp.lastIndexOf("/");
        return "./src" + tmp.substring(8, i + 1) + "cmp_" + tmp.substring(i + 1);
    }

    /**
     * Helper method to construct error messages.
     *
     * @param    error    part of an error message.
     */
    protected void addError(String error) {
        if (error != null && error.length() > 0) {
            if (errorMessage == null) {
                errorMessage = "";
            } else {
                errorMessage += "\n";
            }

            errorMessage += error;
        }
    }
}
