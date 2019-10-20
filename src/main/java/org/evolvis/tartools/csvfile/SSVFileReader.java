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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * {@link CSVFileReader} subclass to read the SSV format:
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
public class SSVFileReader extends CSVFileReader {
    /**
     * SSVFileReader constructor just needing the name of the existing SSV file to read.
     *
     * The SSV file is assumed to be in, and will be read using, UTF-8 encoding.
     *
     * @param inputFileName The name of the CSV file to be opened for reading
     * @throws FileNotFoundException if the file to be read does not exist
     */
    public SSVFileReader(final String inputFileName) throws FileNotFoundException {
        this(new FileInputStream(inputFileName));
    }

    /**
     * SSVFileReader constructor just needing an InputStream for the data to read.
     *
     * The SSV file is assumed to be in, and will be read using, UTF-8 encoding.
     *
     * @param stream The {@link InputStream} for reading CSV data
     */
    public SSVFileReader(final InputStream stream) {
        this(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    /**
     * SSVFileReader constructor just needing a reader for the SSV data to read.
     *
     * @param reader The {@link Reader} for reading CSV data, which MUST be using an
     *               ASCII-compatible charset (such as UTF-8); we sadly cannot test that
     */
    public SSVFileReader(final Reader reader) {
        super(reader, (char) 0x1C, (char) 0);
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
     * SSV does not have any quoted fields.
     *
     * @param i Offset into the input line denoting start of field
     * @return false
     */
    @Override
    protected boolean fieldIsQuoted(final int i) {
        return false;
    }
}
