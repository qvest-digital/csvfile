package org.evolvis.tartools.csvfile;

/*-
 * Copyright © 2015, 2017, 2019
 *      mirabilos <mirabilos@evolvis.org>
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

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
     * SSVFileWriter constructor just needing the name of the SSV file that will be written.
     *
     * The SSV file will be written in UTF-8 encoding.
     *
     * @param outputFileName The name of the SSV file to be opened for writing
     * @throws IOException if an error occurs while creating the file
     */
    public SSVFileWriter(final String outputFileName) throws IOException {
        super(new OutputStreamWriter(new FileOutputStream(outputFileName),
          StandardCharsets.UTF_8), (char) 0x1C, (char) 0);
    }

    /**
     * SSVFileWriter constructor.
     *
     * @param writer The {@link Writer} to be opened for writing, which MUST be using an
     *               ASCII-compatible charset (such as UTF-8); we sadly cannot test that
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
