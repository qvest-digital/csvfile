package org.evolvis.tartools.csvfile.testsuite;

/*-
 * Copyright © 2019, 2020
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

import org.evolvis.tartools.csvfile.SSVFileReader;
import org.evolvis.tartools.csvfile.SSVFileWriter;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static org.evolvis.tartools.csvfile.CSVFile.CR;
import static org.evolvis.tartools.csvfile.CSVFile.CRLF;
import static org.evolvis.tartools.csvfile.CSVFile.LF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link SSVFileWriter} that aren’t already in {@link CSVFileTest}
 *
 * @author mirabilos (t.glaser@tarent.de)
 */
public class SSVFileTest {
    private static final byte[] T01 = { 'a', (byte) 0x0D, 'b', (byte) 0x1F, 'c', (byte) 0x0D, (byte) 0x0A };
    private static final byte[] T02 = { 'a', (byte) 0x00, 'b', (byte) 0x1F, 'c', (byte) 0x0D, (byte) 0x0A };
    private static final byte[] T03 = { 'a', (byte) 0x00, 'b', (byte) 0x0A, 'c', (byte) 0x1F, 'd', (byte) 0x0A };

    @Test
    public void testPosEncodedLineSeparators() {
        assertEquals(1, CR.length());
        assertEquals(0x0D, CR.charAt(0));
        assertEquals(1, LF.length());
        assertEquals(0x0A, LF.charAt(0));
        assertEquals(2, CRLF.length());
        assertEquals(0x0D, CRLF.charAt(0));
        assertEquals(0x0A, CRLF.charAt(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNegWSetFS() {
        final StringWriter sw = new StringWriter();
        final SSVFileWriter w = new SSVFileWriter(sw);
        w.setFieldSeparator(';');
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNegWSetQC() {
        final StringWriter sw = new StringWriter();
        final SSVFileWriter w = new SSVFileWriter(sw);
        w.setTextQualifier('"');
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNegWGetQC() {
        final StringWriter sw = new StringWriter();
        final SSVFileWriter w = new SSVFileWriter(sw);
        System.err.println(String.format("testNegGetQC: %02X", (int) w.getTextQualifier()));
    }

    @Test
    public void testPosWGetFS() {
        final StringWriter sw = new StringWriter();
        final SSVFileWriter w = new SSVFileWriter(sw);
        assertEquals(0x1F, w.getFieldSeparator());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNegRSetFS() {
        final StringReader sr = new StringReader("");
        final SSVFileReader w = new SSVFileReader(sr);
        w.setFieldSeparator(';');
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNegRSetQC() {
        final StringReader sr = new StringReader("");
        final SSVFileReader w = new SSVFileReader(sr);
        w.setTextQualifier('"');
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNegRGetQC() {
        final StringReader sr = new StringReader("");
        final SSVFileReader w = new SSVFileReader(sr);
        System.err.println(String.format("testNegGetQC: %02X", (int) w.getTextQualifier()));
    }

    @Test
    public void testPosRGetFS() {
        final StringReader sr = new StringReader("");
        final SSVFileReader w = new SSVFileReader(sr);
        assertEquals(0x1F, w.getFieldSeparator());
    }

    @Test
    public void testPosReadWithEOL() throws IOException {
        final SSVFileReader sr = new SSVFileReader(new ByteArrayInputStream(T01));
        final List<String> l = sr.readFields();
        assertNotNull(l);
        assertEquals(2, l.size());
        assertEquals("a" + System.lineSeparator() + "b", l.get(0));
        assertEquals("c" + System.lineSeparator(), l.get(1));
        assertNull(sr.readFields());
    }

    @Test
    public void testPosReadNoEOL() throws IOException {
        final SSVFileReader sr = new SSVFileReader(new ByteArrayInputStream(T01, 0, T01.length - 1));
        final List<String> l = sr.readFields();
        assertNotNull(l);
        assertEquals(2, l.size());
        assertEquals("a" + System.lineSeparator() + "b", l.get(0));
        assertEquals("c" + System.lineSeparator(), l.get(1));
        assertNull(sr.readFields());
    }

    @Test
    public void testPosReadIDidRightWithNoEOL() throws IOException {
        final SSVFileReader sr = new SSVFileReader(new ByteArrayInputStream(T01, 0, T01.length - 2));
        final List<String> l = sr.readFields();
        assertNotNull(l);
        assertEquals(2, l.size());
        assertEquals("a" + System.lineSeparator() + "b", l.get(0));
        assertEquals("c", l.get(1));
        assertNull(sr.readFields());
    }

    @Test
    public void testPosReadWithNUL() throws IOException {
        final SSVFileReader sr = new SSVFileReader(new ByteArrayInputStream(T02));
        final List<String> l = sr.readFields();
        assertNotNull(l);
        assertEquals(1, l.size());
        assertEquals("a", l.get(0));
        assertNull(sr.readFields());
    }

    @Test
    public void testPosReadWithLineAfterNUL() throws IOException {
        final SSVFileReader sr = new SSVFileReader(new ByteArrayInputStream(T03));
        List<String> l = sr.readFields();
        assertNotNull(l);
        assertEquals(1, l.size());
        assertEquals("a", l.get(0));
        l = sr.readFields();
        assertNotNull(l);
        assertEquals(2, l.size());
        assertEquals("c", l.get(0));
        assertEquals("d", l.get(1));
        assertNull(sr.readFields());
    }

    @Test
    public void testPosLineSeparator() {
        // no 68k Macintosh
        assertTrue(LF.equals(System.lineSeparator()) || CRLF.equals(System.lineSeparator()));
    }
}
