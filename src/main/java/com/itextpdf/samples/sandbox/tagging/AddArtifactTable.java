package com.itextpdf.samples.sandbox.tagging;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class AddArtifactTable {
    public static final String DEST = "./target/sandbox/tagging/88th_Academy_Awards_artifact_table.pdf";
    public static final String SRC = "./src/main/resources/tagging/88th_Academy_Awards.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddArtifactTable().manipulatePdf(SRC, DEST);
    }

    protected void manipulatePdf(String src, String dest) throws Exception {
        PdfReader reader = new PdfReader(src);
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDocument = new PdfDocument(reader, writer);
        Document document = new Document(pdfDocument);

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.addCell(new Cell().add(new Paragraph("Created as a sample document.")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("30.03.2016")).setBorder(Border.NO_BORDER));

        // Create area break to the end of the document in order to use the last page as the current area to draw.
        document.add(new AreaBreak(AreaBreakType.LAST_PAGE));
        table.setFixedPosition(40, 150, 500);

        // This marks the whole table contents as an Artifact.
        // NOTE: Only content that is already added before this call will be marked as Artifact.
        // New content will be tagged, unless you make this call again.
        table.getAccessibilityProperties().setRole(StandardRoles.ARTIFACT);
        document.add(table);

        document.close();
    }
}
