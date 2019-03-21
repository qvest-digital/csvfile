package org.evolvis.tartools.csvfile;

import org.evolvis.tartools.csvfile.testsuite.CSVFileTest;
import org.junit.Test;

import java.io.StringWriter;

import static org.evolvis.tartools.csvfile.SSVFileWriter.CR;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link SSVFileWriter} that aren’t already in {@link CSVFileTest}
 *
 * @author mirabilos (t.glaser@tarent.de)
 */
public class SSVFileWriterTest {
    @Test
    public void testPosEncodedLineSeparators() {
        assertEquals(1, CR.length());
        assertEquals(0x0D, CR.charAt(0));
        assertEquals(2, SSVFileWriter.CRLF.length());
        assertEquals(0x0D, SSVFileWriter.CRLF.charAt(0));
        assertEquals(0x0A, SSVFileWriter.CRLF.charAt(1));
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

    @Test
    public void testPosPrepSP() {
        final StringWriter sw = new StringWriter();
        final SSVFileWriter w = new SSVFileWriter(sw);
        final String s = "a" + (char) 0x20 + "b";
        assertEquals("a b", w.prepareField(s));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegPrepNUL() {
        final StringWriter sw = new StringWriter();
        final SSVFileWriter w = new SSVFileWriter(sw);
        final String s = "a" + (char) 0 + "b";
        System.err.println(String.format("testNegPrepNUL: %s", w.prepareField(s)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegPrepFS() {
        final StringWriter sw = new StringWriter();
        final SSVFileWriter w = new SSVFileWriter(sw);
        final String s = "a" + (char) 0x1C + "b";
        System.err.println(String.format("testNegPrepFS: %s", w.prepareField(s)));
    }

    @Test
    public void testPosPrepNL() {
        final StringWriter sw = new StringWriter();
        final SSVFileWriter w = new SSVFileWriter(sw);
        String s;

        s = "a" + (char) 0x0A + "b";
        assertEquals("a" + CR + "b", w.prepareField(s));
        s = "a" + (char) 0x0A + (char) 0x0A + "b";
        assertEquals("a" + CR + CR + "b", w.prepareField(s));
        s = "a" + (char) 0x0D + "b";
        assertEquals("a" + CR + "b", w.prepareField(s));
        s = "a" + (char) 0x0D + (char) 0x0D + "b";
        assertEquals("a" + CR + CR + "b", w.prepareField(s));

        s = "a" + (char) 0x0D + (char) 0x0A + "b";
        assertEquals("a" + CR + "b", w.prepareField(s));
        s = "a" + (char) 0x0D + (char) 0x0D + (char) 0x0A + "b";
        assertEquals("a" + CR + CR + "b", w.prepareField(s));
        s = "a" + (char) 0x0D + (char) 0x0A + (char) 0x0A + "b";
        assertEquals("a" + CR + CR + "b", w.prepareField(s));
        s = "a" + (char) 0x0D + (char) 0x0D + (char) 0x0A + (char) 0x0A + "b";
        assertEquals("a" + CR + CR + CR + "b", w.prepareField(s));
    }
}
