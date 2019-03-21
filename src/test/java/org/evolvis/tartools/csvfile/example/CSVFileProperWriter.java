package org.evolvis.tartools.csvfile.example;

import org.evolvis.tartools.csvfile.CSVFileWriter;

import java.io.IOException;

/**
 * {@link CSVFileWriter} that handles fields whose content contains the quote char properly.
 *
 * @author mirabilos (t.glaser@tarent.de)
 */
public class CSVFileProperWriter extends CSVFileWriter {
    /**
     * CSVFileWriter constructor with given field separator and text qualifier.
     *
     * @param outputFileName The name of the CSV file to be opened for writing
     * @param sep            The field separator to be used; overwrites the default one
     * @param qual           The text qualifier to be used; overwrites the default one
     * @throws IOException if an error occurs while creating the file
     */
    public CSVFileProperWriter(final String outputFileName, final char sep, final char qual)
      throws IOException {
        super(outputFileName, sep, qual);
    }

    /**
     * Prepares a field for output by stringifying the passed object and quoting it.
     *
     * @param field to prepare
     * @return quoted string
     */
    @Override
    protected String prepareField(final Object field) {
        final String fieldString = field == null ? "" : field.toString();
        if (fieldString.indexOf(fieldSeparator) >= 0 ||
          fieldString.indexOf('\n') >= 0 ||
          fieldString.indexOf('\r') >= 0 ||
          fieldString.indexOf(textQualifier) >= 0) {
            return textQualifier + fieldString.replaceAll(String.valueOf(textQualifier),
              new String(new char[] { textQualifier, textQualifier })) + textQualifier;
        }
        return fieldString;
    }
}
