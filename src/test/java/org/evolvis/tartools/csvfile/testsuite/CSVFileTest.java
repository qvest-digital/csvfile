package org.evolvis.tartools.csvfile.testsuite;

import org.evolvis.tartools.csvfile.CSVFileReader;
import org.evolvis.tartools.csvfile.CSVFileWriter;
import org.junit.Test;

import java.io.IOException;
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

    @Test
    public void testPos() throws IOException {
        String fc = new String(Files.readAllBytes(Paths.get(FILE_01)), StandardCharsets.UTF_8);
        StringWriter sw = new StringWriter();
        // comma and double quote
        CSVFileReader fr = new CSVFileReader(FILE_01);
        CSVFileWriter fw = new CSVFileWriter(sw, ',', '"');
        assertNotNull(fr);
        assertNotNull(fw);
        // ensure reader matches expectation
        assertEquals(fw.getFieldSeparator(), fr.getFieldSeparator());
        assertEquals(fw.getTextQualifier(), fr.getTextQualifier());
        // ensure test file contents match expectation
        List f = fr.readFields();   // #1 headline
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
        System.err.println(String.format("have<%s>\nneed<%s>\n", sw.toString(), fc));
        assertEquals(fc, sw.toString());
    }
}
