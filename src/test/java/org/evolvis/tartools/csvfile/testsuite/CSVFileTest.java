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

import org.evolvis.tartools.csvfile.CSVFileReader;
import org.evolvis.tartools.csvfile.CSVFileWriter;
import org.evolvis.tartools.csvfile.SSVFileReader;
import org.evolvis.tartools.csvfile.SSVFileWriter;
import org.evolvis.tartools.csvfile.example.CSVFile10Writer;
import org.evolvis.tartools.csvfile.example.CSVFileNilReader;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.evolvis.tartools.csvfile.CSVFile.CR;
import static org.evolvis.tartools.csvfile.CSVFile.CRLF;
import static org.evolvis.tartools.csvfile.CSVFile.LF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test suite (integration) for the CSVFile suite
 *
 * @author mirabilos (t.glaser@tarent.de)
 */
public class CSVFileTest {
    private static final String CONT_02 = "\"\",,a,\"b\"";
    private static final byte[] CONT_03 = { 'm', (byte) 0xE4, 'h' };
    private static final byte[] CONT_05 = { '|', 'a', ';', 'b', '|', ';', (byte) 0xFC };
    private static final byte[] CONT_06 = { '|', 'a', ';', 'b', '|', ';', (byte) 0xFC, ';' };
    private static final String CONT_CR = "1,foo\r\n2,bar\r\n";

    private static String FILE(final int nr) {
        return String.format("src/test/resources/%02d.in", nr);
    }

    private static String CMPF(final int nr) {
        return String.format("src/test/resources/%02d.csv", nr);
    }

    private static String OUTF(final int nr) {
        return String.format("target/%02d.csv", nr);
    }

    private void cpy(final CSVFileReader r, final CSVFileWriter w, final String of, final String cmpf)
      throws IOException {
        assertNotNull(r);
        assertNotNull(w);
        List<String> fields = r.readFields();
        while (fields != null) {
            w.writeFields(fields);
            fields = r.readFields();
        }
        r.close();
        w.close();
        cmp(of, cmpf);
    }

    private void cmp(final String of, final String cmpf) throws IOException {
        String os = new String(Files.readAllBytes(Paths.get(of)), StandardCharsets.UTF_8);
        cmps(cmpf, os);
    }

    private void cmps(final String cmpf, final String os) throws IOException {
        String cs = new String(Files.readAllBytes(Paths.get(cmpf)), StandardCharsets.UTF_8);
        assertEquals(cs, os);
    }

    @Test
    public void testPos() throws IOException {
        String fc = new String(Files.readAllBytes(Paths.get(FILE(1))), StandardCharsets.UTF_8);
        assertNotNull(fc);
        StringWriter sw = new StringWriter();
        assertNotNull(sw);
        // comma and double quote
        CSVFileReader fr = new CSVFileReader(FILE(1));
        CSVFileWriter fw = new CSVFileWriter(sw, ',', '"');
        assertNotNull(fr);
        assertNotNull(fw);
        // ensure reader matches expectation
        assertEquals(fw.getFieldSeparator(), fr.getFieldSeparator());
        assertEquals(fw.getTextQualifier(), fr.getTextQualifier());
        // ensure test file contents match expectation
        List<String> f = fr.readFields();   // #1 headline
        assertNotNull(f);
        assertEquals(3, f.size());
        assertEquals("a", f.get(0));
        assertEquals("b", f.get(1));
        assertEquals("c", f.get(2));
        fw.writeFields(f);

        f = fr.readFields();        // #2
        assertNotNull(f);
        assertEquals(3, f.size());
        assertEquals("d", f.get(0));
        assertEquals("e\"f", f.get(1));
        assertEquals("g", f.get(2));
        fw.writeFields(f);

        f = fr.readFields();        // #3
        assertNotNull(f);
        assertEquals(3, f.size());
        assertEquals("h", f.get(0));
        assertEquals("", f.get(1));
        assertEquals("i", f.get(2));
        fw.writeFields(f);

        f = fr.readFields();        // #4
        assertNotNull(f);
        assertEquals(2, f.size());
        assertEquals("", f.get(0));
        assertEquals("j", f.get(1));
        fw.writeFields(f);

        f = fr.readFields();        // #5
        assertNotNull(f);
        assertEquals(2, f.size());
        assertEquals("", f.get(0));
        assertEquals("k", f.get(1));
        fw.writeFields(f);

        f = fr.readFields();        // #6
        assertNotNull(f);
        assertEquals(1, f.size());
        assertEquals("l", f.get(0));
        fw.writeFields(f);

        f = fr.readFields();        // #7
        assertNotNull(f);
        assertEquals(1, f.size());
        assertEquals("", f.get(0));
        fw.writeFields(f);

        f = fr.readFields();        // #8
        assertNotNull(f);
        assertEquals(1, f.size());
        assertEquals("", f.get(0));
        fw.writeFields(f);

        f = fr.readFields();        // EOF
        assertNull(f);
        fr.close();
        fw.close();
        // double quotes are trimmed on output if not mandatory
        fc = fc.replaceAll("(?m)(?<=(?:^|,))\"([^\"]*)\"(?=(?:$|,))", "$1");
        // trailing commas are ignored
        fc = fc.replaceAll("(?m),$", "");
        // ensure the result matches
        assertEquals(fc, sw.toString());

        // next case: test subclassing and exercise more functions
        Reader sr = new StringReader(CONT_02);
        assertNotNull(sr);
        fr = new CSVFileNilReader(sr);
        assertNotNull(fr);

        f = fr.readFields();        // #1
        assertNotNull(f);
        assertEquals(4, f.size());
        assertEquals("(null)", f.get(0));
        assertEquals("(nil)", f.get(1));
        assertEquals("\"a\"", f.get(2));
        assertEquals("'b'", f.get(3));

        f = fr.readFields();        // EOF
        assertNull(f);
        fr.close();

        // now with encoding
        fr = new CSVFileReader(new ByteArrayInputStream(CONT_03),
          StandardCharsets.ISO_8859_1.name());
        fw = new CSVFileWriter(OUTF(3));
        cpy(fr, fw, OUTF(3), CMPF(3));

        fr = new CSVFileReader(OUTF(3), StandardCharsets.ISO_8859_1.name());
        fw = new CSVFileWriter(OUTF(4));
        cpy(fr, fw, OUTF(4), CMPF(4));

        fr = new CSVFileReader(new ByteArrayInputStream(CONT_05),
          StandardCharsets.ISO_8859_1.name(), ';');
        fw = new CSVFileWriter(OUTF(5), '\t');
        cpy(fr, fw, OUTF(5), CMPF(5));

        fr = new CSVFileReader(new ByteArrayInputStream(CONT_05),
          StandardCharsets.ISO_8859_1.name(), ';', '|');
        fw = new CSVFileWriter(OUTF(6), '\t', '"');
        cpy(fr, fw, OUTF(6), CMPF(6));

        fr = new CSVFileReader(new ByteArrayInputStream(CONT_06),
          StandardCharsets.ISO_8859_1.name(), ';', '|');
        fw = new CSVFileWriter(OUTF(6), '\t', '"');
        cpy(fr, fw, OUTF(6), CMPF(6));

        fr = new CSVFileReader(OUTF(6),
          StandardCharsets.ISO_8859_1.name(), ';');
        fw = new CSVFileWriter(OUTF(7), '\t', '!');
        cpy(fr, fw, OUTF(7), CMPF(7));

        // the old 1.0 CSVFileWriter breaks the CSV spec if the quote character
        // occurs (only once makes it worse) in the middle of a field, see below
        fr = new CSVFileReader(FILE(8),
          StandardCharsets.ISO_8859_1.name(), '\t', '!');
        fw = new CSVFile10Writer(new FileWriter(OUTF(8)), '\t', '!');
        assertNotNull(fr);
        assertNotNull(fw);
        assertEquals(fw.getFieldSeparator(), fr.getFieldSeparator());
        assertEquals(fw.getTextQualifier(), fr.getTextQualifier());
        fw.setFieldSeparator('!');
        fw.setTextQualifier('\t');
        assertEquals(fw.getFieldSeparator(), fr.getTextQualifier());
        assertEquals(fw.getTextQualifier(), fr.getFieldSeparator());
        cpy(fr, fw, OUTF(8), CMPF(8));

        // the new 2.0 CSVFileWriter handles quoting and escaping right
        fr = new CSVFileReader(FILE(8),
          StandardCharsets.ISO_8859_1.name(), '\t', '!');
        fw = new CSVFileWriter(OUTF(9), '\t', '!');
        assertNotNull(fr);
        assertNotNull(fw);
        assertEquals(fw.getFieldSeparator(), fr.getFieldSeparator());
        assertEquals(fw.getTextQualifier(), fr.getTextQualifier());
        fw.setFieldSeparator('!');
        fw.setTextQualifier('\t');
        assertEquals(fw.getFieldSeparator(), fr.getTextQualifier());
        assertEquals(fw.getTextQualifier(), fr.getFieldSeparator());
        cpy(fr, fw, OUTF(9), CMPF(9));

        // same fun but w/o charsets
        fr = new CSVFileReader(OUTF(6), ';');
        fw = new CSVFileWriter(OUTF(10), '\t', '!');
        cpy(fr, fw, OUTF(10), CMPF(10));

        fr = new CSVFileReader(FILE(8), '\t', '!');
        fw = new CSVFileWriter(OUTF(11), '!', '\t');
        assertNotNull(fr);
        assertNotNull(fw);
        assertEquals(fw.getFieldSeparator(), fr.getTextQualifier());
        assertEquals(fw.getTextQualifier(), fr.getFieldSeparator());
        cpy(fr, fw, OUTF(11), CMPF(11));

        // really… identical…
        fr = new CSVFileReader(new FileReader(OUTF(6)), ';');
        fw = new CSVFileWriter(OUTF(10), '\t', '!');
        cpy(fr, fw, OUTF(10), CMPF(10));

        fr = new CSVFileReader(new FileReader(FILE(8)), '\t', '!');
        fw = new CSVFileWriter(OUTF(11), '!', '\t');
        cpy(fr, fw, OUTF(11), CMPF(11));

        // corner case
        sr = new FileReader(FILE(1));
        assertNotNull(sr);
        BufferedReader br = new BufferedReader(sr);
        assertNotNull(br);
        sw = new StringWriter();
        assertNotNull(sw);
        String fl = br.readLine();
        assertEquals("a,b,c", fl);
        fr = new CSVFileReader(br);
        fw = new CSVFileWriter(sw, ',', '"');
        assertNotNull(fr);
        assertNotNull(fw);
        f = fr.readFields(fl);
        assertNotNull(f);
        while (f != null) {
            fw.writeFields(f);
            f = fr.readFields();
        }
        fr.close();
        fw.close();
        assertEquals(fc, sw.toString());

        // SSV
        fr = new CSVFileReader(new FileReader(FILE(1)));
        fw = new SSVFileWriter(OUTF(12));
        cpy(fr, fw, OUTF(12), CMPF(12));

        fr = new SSVFileReader(CMPF(12));
        fw = new CSVFileWriter(OUTF(16));
        cpy(fr, fw, OUTF(16), CMPF(16));

        // quoted strings spanning input lines
        fr = new CSVFileReader(new FileReader(FILE(14)));
        fw = new SSVFileWriter(OUTF(14));
        cpy(fr, fw, OUTF(14), CMPF(14));

        fr = new CSVFileReader(new FileReader(FILE(15)));
        fw = new SSVFileWriter(OUTF(15));
        cpy(fr, fw, OUTF(15), CMPF(15));

        final SSVFileReader nr = new SSVFileReader(CMPF(90));
        fw = new CSVFileWriter(OUTF(91));
        nr.useUnixNewline();
        cpy(nr, fw, OUTF(91), CMPF(91));

        // CRLF
        sr = new StringReader(CONT_CR);
        assertNotNull(sr);
        fr = new CSVFileReader(sr);
        assertNotNull(fr);
        f = fr.readFields();        // #1
        assertNotNull(f);
        assertEquals(2, f.size());
        assertEquals("1", f.get(0));
        assertEquals("foo", f.get(1));
        f = fr.readFields();        // #2
        assertNotNull(f);
        assertEquals(2, f.size());
        assertEquals("2", f.get(0));
        assertEquals("bar", f.get(1));
        f = fr.readFields();        // EOF
        assertNull(f);
        fr.close();
    }

    @Test
    public void testPosCSVFromList()
      throws IllegalAccessException, InvocationTargetException, InstantiationException,
      IOException, NoSuchMethodException {
        doCSVFromList(CSVFileWriter.class, 81);
        doCSVFromList(CSVFile10Writer.class, 80);
    }

    private void doCSVFromList(final Class<? extends CSVFileWriter> clazz, final int cm)
      throws IllegalAccessException, InvocationTargetException, InstantiationException,
      IOException, NoSuchMethodException {
        final StringWriter sw = new StringWriter();
        final CSVFileWriter w = clazz.getConstructor(Writer.class, char.class,
          char.class).newInstance((Writer) sw, ';', '"');
        final List<String> l = new ArrayList<>();
        l.add(null);
        l.add("a;b");
        l.add("c" + LF + "d");
        l.add("e" + CR + "f");
        l.add("\"g");
        w.writeFields(l);
        l.clear();
        l.add("g\"h"); // mishandled by CSVFile10Writer
        w.writeFields(l);
        w.close();
        cmps(CMPF(cm), sw.toString());
    }

    @Test
    public void testPosSSVFromList() throws IOException {
        final StringWriter sw = new StringWriter();
        final SSVFileWriter w = new SSVFileWriter(sw);
        final List<String> l = new ArrayList<>();
        l.add("meow\"miau mio" + CRLF + "mraw,nyan;mewl");
        l.add("🐈");
        w.writeFields(l);
        l.clear();
        l.add("챁");
        l.add("ꊶ");
        w.writeFields(l);
        w.close();
        cmps(CMPF(90), sw.toString());
    }
}
