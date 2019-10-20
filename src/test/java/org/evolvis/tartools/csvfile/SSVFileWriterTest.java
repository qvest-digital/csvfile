package org.evolvis.tartools.csvfile;

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

import org.junit.Test;

import java.io.StringWriter;

import static org.evolvis.tartools.csvfile.CSVFile.CR;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link SSVFileWriter} that need protected access and can’t be in
 * {@link org.evolvis.tartools.csvfile.testsuite.SSVFileTest}
 *
 * @author mirabilos (t.glaser@tarent.de)
 */
public class SSVFileWriterTest {
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

    @Test
    public void testPosNilObject() {
        final StringWriter sw = new StringWriter();
        final SSVFileWriter w = new SSVFileWriter(sw);
        assertEquals("", w.prepareField(null));
    }
}
