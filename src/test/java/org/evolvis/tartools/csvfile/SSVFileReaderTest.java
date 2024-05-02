package org.evolvis.tartools.csvfile;

/*-
 * Copyright © 2019
 *      mirabilos <t.glaser@qvest-digital.com>
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.evolvis.tartools.csvfile.CSVFile.CR;
import static org.evolvis.tartools.csvfile.SSVFileReader.BUFSIZ;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link SSVFileReader} that need protected access and can’t be in
 * {@link org.evolvis.tartools.csvfile.testsuite.SSVFileTest}
 *
 * @author mirabilos (t.glaser@qvest-digital.com)
 */
public class SSVFileReaderTest {
    private static final byte[] BUF = new byte[BUFSIZ + 2];

    private static final byte[] T01 = { 'a', (byte) 0x0D, (byte) 0x0A, 'b', (byte) 0x0A, 'c' };

    @Test
    public void testPosReadlineImmediatelyEOF() throws IOException {
        final SSVFileReader sr = new SSVFileReader(new ByteArrayInputStream(BUF, 0, 0));
        assertNull(sr.inReadLine());
    }

    @Test
    public void testPosReadlineEasy() throws IOException {
        final SSVFileReader sr = new SSVFileReader(new ByteArrayInputStream(T01));
        assertEquals("a" + CR, sr.inReadLine());
        assertEquals("b", sr.inReadLine());
        assertEquals("c", sr.inReadLine());
        assertNull(sr.inReadLine());
    }

    @Test
    public void testPosReadlineBlockThenEOF() throws IOException {
        Arrays.fill(BUF, (byte) 1);
        final String cs = new String(BUF, 0, BUFSIZ, StandardCharsets.UTF_8);
        final SSVFileReader sr = new SSVFileReader(new ByteArrayInputStream(BUF, 0, BUFSIZ));
        assertEquals(cs, sr.inReadLine());
        assertNull(sr.inReadLine());
    }

    @Test
    public void testPosReadlineBlockThenOneAndEOF() throws IOException {
        Arrays.fill(BUF, (byte) 1);
        final String cs = new String(BUF, 0, BUFSIZ + 1, StandardCharsets.UTF_8);
        final SSVFileReader sr = new SSVFileReader(new ByteArrayInputStream(BUF, 0, BUFSIZ + 1));
        assertEquals(cs, sr.inReadLine());
        assertNull(sr.inReadLine());
    }
}
