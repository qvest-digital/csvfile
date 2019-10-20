package org.evolvis.tartools.csvfile.testsuite;

/*-
 * Copyright © 2019
 *      mirabilos <t.glaser@tarent.de>
 *
 * Provided that these terms and disclaimer and all copyright notices
 * are retained or reproduced in an accompanying document, permission
 * is granted to deal in this work without restriction, including un‐
 * limited rights to use, publicly perform, distribute, sell, modify,
 * merge, give away, or sublicence.
 *
 * This work is provided “AS IS” and WITHOUT WARRANTY of any kind, to
 * the utmost extent permitted by applicable law, neither express nor
 * implied; without malicious intent or gross negligence. In no event
 * may a licensor, author or contributor be held liable for indirect,
 * direct, other damage, loss, or other issues arising in any way out
 * of dealing in the work, even if advised of the possibility of such
 * damage or existence of a defect, except proven that it results out
 * of said person’s immediate fault when using the work as intended.
 */

import org.evolvis.tartools.csvfile.SSVFileWriter;
import org.junit.Test;

import java.io.StringWriter;

import static org.evolvis.tartools.csvfile.CSVFile.CR;
import static org.evolvis.tartools.csvfile.CSVFile.CRLF;
import static org.evolvis.tartools.csvfile.CSVFile.LF;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link SSVFileWriter} that aren’t already in {@link CSVFileTest}
 *
 * @author mirabilos (t.glaser@tarent.de)
 */
public class SSVFileTest {
    @Test
    public void testPosEncodedLineSeparators() {
        assertEquals(1, CR.length());
        assertEquals(0x0D, CR.charAt(0));
        assertEquals(1, LF.length());
        assertEquals(0x0D, LF.charAt(0));
        assertEquals(2, CRLF.length());
        assertEquals(0x0D, CRLF.charAt(0));
        assertEquals(0x0A, CRLF.charAt(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNegSetFS() {
        final StringWriter sw = new StringWriter();
        final SSVFileWriter w = new SSVFileWriter(sw);
        w.setFieldSeparator(';');
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNegSetQC() {
        final StringWriter sw = new StringWriter();
        final SSVFileWriter w = new SSVFileWriter(sw);
        w.setTextQualifier('"');
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNegGetQC() {
        final StringWriter sw = new StringWriter();
        final SSVFileWriter w = new SSVFileWriter(sw);
        System.err.println(String.format("testNegGetQC: %02X", (int) w.getTextQualifier()));
    }

    @Test
    public void testPosGetFS() {
        final StringWriter sw = new StringWriter();
        final SSVFileWriter w = new SSVFileWriter(sw);
        assertEquals(0x1C, w.getFieldSeparator());
    }
}
