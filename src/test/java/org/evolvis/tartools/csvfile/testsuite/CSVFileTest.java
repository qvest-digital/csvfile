package org.evolvis.tartools.csvfile.testsuite;

import org.evolvis.tartools.csvfile.CSVFileReader;
import org.evolvis.tartools.csvfile.CSVFileWriter;
import org.evolvis.tartools.csvfile.example.CSVFileNilReader;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CSVFileTest {
    private static final String FILE_01 = "src/test/resources/01.csv";
    private static final String CONT_02 = "\"\",,a,\"b\"";
    private static final byte[] CONT_03 = { 'm', (byte) 0xE4, 'h' };

    private static String CMPF(final int nr) {
        return String.format("src/test/resources/%02d.csv", nr);
    }

    private static String OUTF(final int nr) {
        return String.format("target/%02d.csv", nr);
    }

    private void cpy(final CSVFileReader r, final CSVFileWriter w, final String of, final String cmpf)
      throws IOException {
        List<String> fields = r.readFields();
        while (fields != null) {
            w.writeFields(fields);
            fields = r.readFields();
        }
        r.close();
        w.close();

        String os = new String(Files.readAllBytes(Paths.get(of)), StandardCharsets.UTF_8);
        String cs = new String(Files.readAllBytes(Paths.get(cmpf)), StandardCharsets.UTF_8);
        assertEquals(cs, os);
    }

    @Test
    public void testPos() throws IOException {
        String fc = new String(Files.readAllBytes(Paths.get(FILE_01)), StandardCharsets.UTF_8);
        assertNotNull(fc);
        StringWriter sw = new StringWriter();
        assertNotNull(sw);
        // comma and double quote
        CSVFileReader fr = new CSVFileReader(FILE_01);
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
        fc = fc.replace("\"\"", "\01").replace("\"", "").replace('\01', '"');
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
        fr = new CSVFileReader(new ByteArrayInputStream(CONT_03), StandardCharsets.ISO_8859_1.name());
        fw = new CSVFileWriter(OUTF(3));
        cpy(fr, fw, OUTF(3), CMPF(3));

        fr = new CSVFileReader(OUTF(3), StandardCharsets.ISO_8859_1.name());
        fw = new CSVFileWriter(OUTF(4));
        cpy(fr, fw, OUTF(4), CMPF(4));
    }
}
