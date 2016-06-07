/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * These examples are written by Bruno Lowagie in the context of an article about fonts.
 */
package com.itextpdf.samples.sandbox.fonts.tutorial;

public class F99_ConvertToUnicodeNotation {
    public static void main() throws Exception {
        String s = "Vous êtes d'où?";
        System.out.print("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c > 31 && c < 127)
                System.out.print(String.valueOf(c));
            else
                System.out.print(String.format("\\u%04x", (int) c));
        }
        System.out.println("\"");
    }
}
