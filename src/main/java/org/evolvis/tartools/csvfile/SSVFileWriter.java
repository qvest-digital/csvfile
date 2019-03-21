package org.evolvis.tartools.csvfile;

import java.io.IOException;
import java.io.Writer;

/**
 * {@link CSVFileWriter} subclass to write the SSV format:
 *
 * <ul>
 * <li>Line feed (0x0A) is row separator</li>
 * <li>Field separator (0x1C) is column separator</li>
 * <li>No quote or escape characters are used</li>
 * <li>Carriage return (0x0D) represents embedded newline</li>
 * <li>Cell content is arbitrary binary except 0x0A, 0x1C (and NUL)</li>
 * </ul>
 *
 * @author mirabilos (t.glaser@tarent.de)
 * @see <a
 * href="https://evolvis.org/plugins/scmgit/cgi-bin/gitweb.cgi?p=shellsnippets/shellsnippets.git;a=tree;f=mksh/ssv;hb=HEAD">SSV
 * at shellsnippets @ Evolvis</a>
 */
public class SSVFileWriter extends CSVFileWriter {
    static final String CR = "" + (char) 0x0D;
    static final String CRLF = CR + (char) 0x0A;

    /**
     * SSVFileWriter constructor just need the name of the SSV file that will be written.
     *
     * @param outputFileName The name of the SSV file to be opened for writing
     * @throws IOException if an error occurs while creating the file
     */
    public SSVFileWriter(final String outputFileName) throws IOException {
        super(outputFileName, (char) 0x1C, (char) 0);
    }

    /**
     * SSVFileWriter constructor.
     *
     * @param writer The {@link Writer} to be opened for writing
     */
    public SSVFileWriter(final Writer writer) {
        super(writer, (char) 0x1C, (char) 0xFFFF);
    }

    /**
     * Throws an exception, the field separator is constant for SSV.
     *
     * @param sep The new field separator to be ignored
     */
    @Override
    public void setFieldSeparator(final char sep) {
        throw new UnsupportedOperationException("SSV does not allow changing the field separator");
    }

    /**
     * Throws an exception, SSV has no quote character.
     *
     * @param qual The new text qualifier to be ignored
     */
    @Override
    public void setTextQualifier(final char qual) {
        throw new UnsupportedOperationException("SSV does not have a quote character");
    }

    /**
     * Throws an exception, SSV has no quote character.
     */
    @Override
    public char getTextQualifier() {
        throw new UnsupportedOperationException("SSV does not have a quote character");
    }

    /**
     * Called after a list of fields has been output.
     */
    @Override
    protected void emitRowSeparator() {
        out.print((char) 0x0A);
    }

    /**
     * Prepares a field for output by stringifying the passed object according to SSV rules.
     *
     * @param field to prepare
     * @return string for output
     */
    @Override
    protected String prepareField(final Object field) {
        final String fieldString = field == null ? "" : field.toString();
        if (fieldString.indexOf(0x00) != -1) {
            throw new IllegalArgumentException(String.format("%s (\\x%02X) found in field: %s",
              "NUL", 0, fieldString));
        }
        if (fieldString.indexOf(0x1C) != -1) {
            throw new IllegalArgumentException(String.format("%s (\\x%02X) found in field: %s",
              "FS", 0x1C, fieldString));
        }
        if (fieldString.indexOf(0x0D) == -1 && fieldString.indexOf(0x0A) == -1) {
            return fieldString;
        }
        return fieldString.replace(CRLF, CR).replace((char) 0x0A, (char) 0x0D);
    }
}