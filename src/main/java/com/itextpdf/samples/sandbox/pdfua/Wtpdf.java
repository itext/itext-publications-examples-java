package com.itextpdf.samples.sandbox.pdfua;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.OutlineHandler;
import com.itextpdf.kernel.pdf.PdfAConformance;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.validation.ValidationContainer;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.layout.tagging.ProhibitedTagRelationsResolver;
import com.itextpdf.pdfa.PdfADocument;
import com.itextpdf.pdfua.checkers.PdfUA2Checker;
import com.itextpdf.styledxmlparser.resolver.font.BasicFontProvider;
import com.itextpdf.test.pdfa.VeraPdfValidator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Wtpdf {


    public static final String DEST = "./target/sandbox/pdfua2/pdf_wtpdf.pdf";
    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";
    private static final String SOURCE_FOLDER = "./src/main/resources/wtpdf/";


    public static void main(String[] args) throws IOException, XMPException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Wtpdf().manipulatePdf(DEST);
    }

    private void manipulatePdf(String dest) throws IOException, XMPException {
        PdfOutputIntent outputIntent = new PdfOutputIntent(
                "Custom",
                "",
                "http://www.color.org",
                "sRGB IEC61964-2.1",
                Files.newInputStream(Paths.get(SOURCE_FOLDER + "sRGB Color Space Profile.icm")));


        WriterProperties writerProperties = new WriterProperties().setPdfVersion(PdfVersion.PDF_2_0);
        //If you need attachments you would PDF/A4-F
        PdfADocument pdfDocument = new PdfADocument(new PdfWriter(dest, writerProperties), PdfAConformance.PDF_A_4,
                outputIntent);

        // By default PdfUADocument has a tag repairing mechanism under the hood, to avoid creating illegal
        // tag structures for example from invalid html, but because PdfADocument as base we have to register it
        // manually

        pdfDocument.getDiContainer()
                .register(ProhibitedTagRelationsResolver.class, new ProhibitedTagRelationsResolver(pdfDocument));
        ValidationContainer container = pdfDocument.getDiContainer().getInstance(ValidationContainer.class);
        //Because we are using PDF/A4, there will already be a pdf 2.0 checker , so we only need to add the pdf ua
        // checker
        container.addChecker(new PdfUA2Checker(pdfDocument));

        // setup the general requirements for a wtpdf document
        byte[] bytes = Files.readAllBytes(Paths.get(SOURCE_FOLDER + "simplePdfUA2.xmp"));
        XMPMeta xmpMeta = XMPMetaFactory.parse(new ByteArrayInputStream(bytes));
        pdfDocument.setXmpMetadata(xmpMeta);
        pdfDocument.setTagged();
        pdfDocument.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));
        pdfDocument.getCatalog().setLang(new PdfString("en-US"));
        PdfDocumentInfo info = pdfDocument.getDocumentInfo();
        info.setTitle("Well tagged PDF document");

        // Use custom font provider as we only want embedded fonts
        BasicFontProvider fontProvider = new BasicFontProvider(false, false, false);
        fontProvider.addFont(SOURCE_FOLDER + "NotoSans-Regular.ttf");
        fontProvider.addFont(SOURCE_FOLDER + "NotoEmoji-Regular.ttf");

        ConverterProperties converterProperties = new ConverterProperties()
                .setBaseUri(SOURCE_FOLDER)
                .setOutlineHandler(OutlineHandler.createStandardHandler())
                .setFontProvider(fontProvider);


        File file = new File(SOURCE_FOLDER + "article.html");
        try (FileInputStream str = new FileInputStream(file)) {
            HtmlConverter.convertToPdf(str, pdfDocument, converterProperties);
        }
        pdfDocument.close();
        VeraPdfValidator validator = new VeraPdfValidator();
        assert null == validator.validate(DEST);
    }
}
