package com.itextpdf.samples.sandbox.pdfhtml.columncount;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.attach.impl.tags.DivTagWorker;
import com.itextpdf.samples.sandbox.columncontainer.ColumnAllowMoreRelayouts.MultiColRendererAllow10RetriesRenderer;
import com.itextpdf.styledxmlparser.node.IElementNode;

import java.io.File;
import java.io.IOException;

public class CustomColumnRendererDivTagWorker {

    public static final String SRC = "./src/main/resources/pdfhtml/CustomColumnRendererDivTagWorker"
            + "/CustomColumnRendererDivTagWorker.html";
    public static final String DEST = "./target/sandbox/pdfhtml/CustomColumnRendererDivTagWorker.pdf";

    public static void main(String[] args) throws IOException, InterruptedException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CustomColumnRendererDivTagWorker().manipulatePdf(SRC, DEST);
    }

    private void manipulatePdf(String src, String dest) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setMulticolEnabled(true);

        converterProperties.setTagWorkerFactory(
                new DefaultTagWorkerFactory() {
                    @Override
                    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
                        if ("div".equalsIgnoreCase(tag.name())) {
                            return new ColumnDivTagWorker(tag, context);
                        }
                        return super.getCustomTagWorker(tag, context);
                    }
                }
        );

        HtmlConverter.convertToPdf(new File(src), new File(dest), converterProperties);

    }


    static class ColumnDivTagWorker extends DivTagWorker {
        public ColumnDivTagWorker(IElementNode element, ProcessorContext context) {
            super(element, context);
            this.multicolContainer.setNextRenderer(new MultiColRendererAllow10RetriesRenderer(this
                    .multicolContainer));
        }
    }
}
