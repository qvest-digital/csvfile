package org.evolvis.tartools.csvfile.example;

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

import java.io.IOException;
import java.io.Reader;

/**
 * Test helper class for subclassing methods.
 * Feel free to reuse the concepts in your own projects.
 *
 * @author mirabilos (t.glaser@tarent.de)
 */
public class CSVFileNilReader extends CSVFileReader {
    /**
     * Handles a quoted field.
     *
     * @param sb the {@link StringBuilder} to add the resulting field into
     * @param i  the offset of the first supposed character of the field (past the quote)
     * @return index of next separator
     * @throws IOException if input cannot be read
     */
    @Override
    protected int handleQuotedField(final StringBuilder sb, final int i) throws IOException {
        assert (textQualifier == DEFAULT_TEXT_QUALIFIER);
        sb.append('\'');
        final int rv = super.handleQuotedField(sb, i);
        if (sb.length() == 1) {
            sb.setLength(0);
            sb.append("(null)");
        } else {
            sb.append('\'');
        }
        return rv;
    }

    /**
     * Handles an unquoted field.
     *
     * @param sb the {@link StringBuilder} to add the resulting field into
     * @param i  the offset of the first supposed character of the field
     * @return index of next separator
     */
    @Override
    protected int handlePlainField(final StringBuilder sb, final int i) {
        /* just for IntelliJ */
        assert (line != null);

        assert (textQualifier == DEFAULT_TEXT_QUALIFIER);
        sb.append('"');
        final int rv = super.handlePlainField(sb, i);
        if (sb.length() == 1) {
            sb.setLength(0);
            sb.append("(nil)");
        } else {
            sb.append('"');
        }
        return rv;
    }

    /**
     * CSVFileReader constructor just needing a reader for the CSV data that will be read.
     *
     * @param reader The Reader for reading CSV data
     */
    public CSVFileNilReader(final Reader reader) {
        super(reader);
        assert (fieldSeparator == DEFAULT_FIELD_SEPARATOR);
    }
}
