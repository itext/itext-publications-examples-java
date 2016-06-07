/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PdfOnButtonClick {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(300, 300);
        frame.setTitle("ATUL doesn't know how to code");
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JButton button = new JButton("Push ATUL, push!");
        button.addActionListener(new PdfOnButtonClick().new PdfActionListener());
        frame.getContentPane().add(button);
        frame.setVisible(true);
    }


    public class PdfActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser dialog = new JFileChooser();
            int dialogResult = dialog.showSaveDialog(null);
            if (dialogResult == JFileChooser.APPROVE_OPTION) {
                String filePath = dialog.getSelectedFile().getPath();
                try {
                    PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filePath));
                    Document doc = new Document(pdfDoc);
                    doc.add(new Paragraph("File with path " + filePath));
                    doc.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
}
