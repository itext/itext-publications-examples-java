package com.itextpdf.samples.sandbox.pdfua;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.attach.impl.tags.HTagWorker;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.pdfa.PdfADocument;
import com.itextpdf.styledxmlparser.node.IElementNode;
import com.itextpdf.styledxmlparser.resolver.font.BasicFontProvider;
import com.itextpdf.test.pdfa.VeraPdfValidator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class Wtpdf {


    public static final String DEST = "./target/sandbox/pdfua2/pdf_wtpdf.pdf";
    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";
    private static final String SOURCE_FOLDER = "./src/main/resources/wtpdf/";
    private static final Set<String> H_TAGS = new HashSet<>(Arrays.asList("h1", "h2", "h3", "h4", "h5", "h6"));


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
        PdfADocument pdfDocument = new PdfADocument(new PdfWriter(dest, writerProperties), PdfAConformance.PDF_A_4, outputIntent);


	// The custom tag factory is needed because the PDF2.0 specification prohibts from a p tag being placed inside a Hn tag.
	// THis is the current default behaviour for html2pdf but will change in the future.
        DefaultTagWorkerFactory factory = new DefaultTagWorkerFactory() {
            @Override
            public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
                if (H_TAGS.contains(tag.name())) {
                    return new HTagWorker(tag, context) {

                        @Override
                        public IPropertyContainer getElementResult() {
                            IPropertyContainer elementResult = super.getElementResult();
                            if (elementResult instanceof Div) {
                                for (IElement child : ((Div) elementResult).getChildren()) {
                                    if (child instanceof Paragraph) {
                                        ((Paragraph) child).setNeutralRole();
                                    }
                                }
                            }
                            return elementResult;
                        }
                    };
                }
                return super.getCustomTagWorker(tag, context);
            }
        };

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
                .setTagWorkerFactory(factory)
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
