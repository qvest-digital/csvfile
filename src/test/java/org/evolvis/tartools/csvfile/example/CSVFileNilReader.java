package org.evolvis.tartools.csvfile.example;

import org.evolvis.tartools.csvfile.CSVFileReader;

import java.io.IOException;
import java.io.Reader;

/**
 * Test helper class for subclassing methods.
 * Feel free to reuse the concepts in your own projects;
 * see also {@link CSVFileProperWriter}.
 *
 * @author mirabilos (t.glaser@tarent.de)
 */
public class CSVFileNilReader extends CSVFileReader {
    /**
     * Handles a quoted field.
     *
     * @param sb the {@link StringBuffer} to add the resulting field into
     * @param i  the offset of the first supposed character of the field (past the quote)
     * @return index of next separator
     * @throws IOException if input cannot be read
     */
    @Override
    protected int handleQuotedField(final StringBuffer sb, final int i) throws IOException {
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
     * @param sb the {@link StringBuffer} to add the resulting field into
     * @param i  the offset of the first supposed character of the field
     * @return index of next separator
     */
    @Override
    protected int handlePlainField(final StringBuffer sb, final int i) {
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
