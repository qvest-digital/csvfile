package org.evolvis.tartools.csvfile;

/*-
 * The CSVFile tools are a collection of classes to deal with CSV files
 * (comma-separated values). They have quite a history. This current
 * edition, developed alongside VerA.web by ⮡ tarent, is published under
 * the terms of the GNU Lesser or Library General Public License, any
 * version published by the Free Software Foundation.
 * Licensor is tarent solutions GmbH, http://www.tarent.de/
 *
 * Copyright holders and contributors:
 *
 * Copyright © 2016 Атанас Александров (a.alexandrov@tarent.de)
 * Copyright © 2006 Christoph Jerolimov (jerolimov@gmx.de)
 * Copyright © 2008 Carsten Klein (c.klein@tarent.de)
 * Copyright © 2005 Michael Klink (m.klink@tarent.de)
 * Copyright © 2013, 2015, 2018 mirabilos (t.glaser@tarent.de)
 * and older code, Copyright © 2004–2008 ⮡ tarent GmbH and contributors.
 *
 * Copyright © 2005 Fabrizio Fazzino (under GNU LGPL) — also on
 * http://sourceforge.net/projects/csvfile
 *
 * Published as part of Ian’s Java CookBook, 2002 (under 2-clause BSD):
 *
 * Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 1996–2001
 * Copyright (c) Ben Ballard, ca. 2001
 * All rights reserved. Software written by Ian F. Darwin and others.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Java, the Duke mascot, and all variants of Sun's Java "steaming coffee
 * cup" logo are trademarks of Sun Microsystems. Sun's, and James Gosling's,
 * pioneering role in inventing and promulgating (and standardizing) the Java
 * language and environment is gratefully acknowledged.
 *
 * The pioneering role of Dennis Ritchie and Bjarne Stroustrup, of AT&T, for
 * inventing predecessor languages C and C++ is also gratefully acknowledged.
 *
 * Copyright (C) 1999 Lucent Technologies
 * Adapted from a C++ original excerpted from “The Practice of Programming”
 * by Brian Kernighan and Rob Pike. Included by permission of the <a
 * href="http://www.informit.com/store/practice-of-programming-9780201615869"
 * title="http://tpop.awl.com/">Addison-Wesley</a> web site, which says:
 * <cite>“You may use this code for any purpose, as long as you leave the
 * copyright notice and book citation attached.”</cite> I have done so.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVFileReader is a class derived from {@link CSVFile}
 * used to parse an existing CSV file.
 *
 * @author Brian Kernighan and Rob Pike (C++ original)
 * @author Ian F. Darwin (translation into Java and removal of I/O)
 * @author Ben Ballard (double quote handling and readability)
 * @author Fabrizio Fazzino (CSVFile integration, textQualifier handling, Vectors)
 * @author Michael “Mikel” Klink (de-genericisation, etc.)
 */
public class CSVFileReader extends CSVFile {
    /**
     * The buffered reader linked to the CSV file to be read.
     */
    private final BufferedReader in;

    /**
     * Die aktuell gelesene Zeile.
     */
    private String line = null;

    /**
     * CSVFileReader constructor just needing the name of the existing CSV file to read.
     *
     * @param inputFileName The name of the CSV file to be opened for reading
     * @throws FileNotFoundException if the file to be read does not exist
     */
    public CSVFileReader(final String inputFileName)
      throws FileNotFoundException {
        this(inputFileName, DEFAULT_FIELD_SEPARATOR);
    }

    /**
     * CSVFileReader constructor just needing a reader for the CSV data that will be read.
     *
     * @param reader The Reader for reading CSV data
     */
    public CSVFileReader(final Reader reader) {
        this(reader, DEFAULT_FIELD_SEPARATOR);
    }

    /**
     * CSVFileReader constructor just needing the name and encoding of the
     * existing CSV file that will be read.
     *
     * @param inputFileName The name of the CSV file to be opened for reading
     * @param charsetName   The name of a supported charset
     * @throws FileNotFoundException        if the file to be read does not exist
     * @throws UnsupportedEncodingException if the named charset is not supported
     */
    public CSVFileReader(final String inputFileName, final String charsetName)
      throws FileNotFoundException, UnsupportedEncodingException {
        this(inputFileName, charsetName, DEFAULT_FIELD_SEPARATOR);
    }

    /**
     * CSVFileReader constructor just needing an InputStream and encoding for the
     * CSV data that will be read.
     *
     * @param stream      The InputStream for reading CSV data
     * @param charsetName The name of a supported charset
     * @throws UnsupportedEncodingException if the named charset is not supported
     */
    public CSVFileReader(final InputStream stream, final String charsetName)
      throws UnsupportedEncodingException {
        this(stream, charsetName, DEFAULT_FIELD_SEPARATOR);
    }

    /**
     * CSVFileReader constructor with a given field separator.
     *
     * @param inputFileName The name of the CSV file to be opened for reading
     * @param sep           The field separator to be used; overwrites the default one
     * @throws FileNotFoundException if the file to be read does not exist
     */
    public CSVFileReader(final String inputFileName, final char sep)
      throws FileNotFoundException {
        this(inputFileName, sep, DEFAULT_TEXT_QUALIFIER);
    }

    /**
     * CSVFileReader constructor with a given field separator.
     *
     * @param reader The Reader for reading CSV data
     * @param sep    The field separator to be used; overwrites the default one
     */
    public CSVFileReader(final Reader reader, final char sep) {
        this(reader, sep, DEFAULT_TEXT_QUALIFIER);
    }

    /**
     * CSVFileReader constructor with a given field separator.
     *
     * @param inputFileName The name of the CSV file to be opened for reading
     * @param charsetName   The name of a supported charset
     * @param sep           The field separator to be used; overwrites the default one
     * @throws FileNotFoundException        if the file to be read does not exist
     * @throws UnsupportedEncodingException if the named charset is not supported
     */
    public CSVFileReader(final String inputFileName, final String charsetName, final char sep)
      throws FileNotFoundException, UnsupportedEncodingException {
        this(inputFileName, charsetName, sep, DEFAULT_TEXT_QUALIFIER);
    }

    /**
     * CSVFileReader constructor with a given field separator.
     *
     * @param stream      The stream for reading CSV data
     * @param charsetName The name of a supported charset
     * @param sep         The field separator to be used; overwrites the default one
     * @throws UnsupportedEncodingException if the named charset is not supported
     */
    public CSVFileReader(final InputStream stream, final String charsetName, final char sep)
      throws UnsupportedEncodingException {
        this(stream, charsetName, sep, DEFAULT_TEXT_QUALIFIER);
    }

    /**
     * CSVFileReader constructor with given field separator and text qualifier.
     *
     * @param inputFileName The name of the CSV file to be opened for reading
     * @param sep           The field separator to be used; overwrites the default one
     * @param qual          The text qualifier to be used; overwrites the default one
     * @throws FileNotFoundException if the file to be read does not exist
     */
    public CSVFileReader(final String inputFileName, final char sep, final char qual)
      throws FileNotFoundException {
        this(new FileReader(inputFileName), sep, qual);
    }

    /**
     * CSVFileReader constructor with given field separator and text qualifier.
     *
     * @param reader The Reader for reading CSV data
     * @param sep    The field separator to be used; overwrites the default one
     * @param qual   The text qualifier to be used; overwrites the default one
     */
    public CSVFileReader(final Reader reader, final char sep, final char qual) {
        super(sep, qual);
        in = reader instanceof BufferedReader ? (BufferedReader) reader :
          new BufferedReader(reader);
    }

    /**
     * CSVFileReader constructor with given field separator and text qualifier.
     *
     * @param inputFileName The name of the CSV file to be opened for reading
     * @param charsetName   The name of a supported charset
     * @param sep           The field separator to be used; overwrites the default one
     * @param qual          The text qualifier to be used; overwrites the default one
     * @throws FileNotFoundException        if the file to be read does not exist
     * @throws UnsupportedEncodingException if the named charset is not supported
     */
    public CSVFileReader(final String inputFileName, final String charsetName,
      final char sep, final char qual)
      throws FileNotFoundException, UnsupportedEncodingException {
        this(new FileInputStream(inputFileName), charsetName, sep, qual);
    }

    /**
     * CSVFileReader constructor with given field separator and text qualifier.
     *
     * @param stream      The InputStream for reading CSV data
     * @param charsetName The name of a supported charset
     * @param sep         The field separator to be used; overwrites the default one
     * @param qual        The text qualifier to be used; overwrites the default one
     * @throws UnsupportedEncodingException if the named charset is not supported
     */
    public CSVFileReader(final InputStream stream, final String charsetName,
      final char sep, final char qual)
      throws UnsupportedEncodingException {
        super(sep, qual);
        in = new BufferedReader(new InputStreamReader(stream, charsetName));
    }

    /**
     * Splits the next line of the input CSV file into fields.
     *
     * This is currently the most important function of the package.
     * It can read a subsequent line from the input stream if necessary
     * due to a newline inside a quoted field.
     *
     * @return List of strings containing each field from the next line of the file
     * @throws IOException if an error occurs while reading the new line from the file
     */
    public List readFields() throws IOException {
        return readFields(in.readLine());
    }

    /**
     * Splits the next line of the input CSV file into fields.
     *
     * This is currently the most important function of the package.
     * It can read a subsequent line from the input stream if necessary
     * due to a newline inside a quoted field.
     *
     * @param firstLine result of in.readLine() if called by parent for preparation
     *                  already (can only happen if instantiated with a BufferedReader)
     * @return List of strings containing each field from the next line of the file
     * @throws IOException if an error occurs while reading the new line from the file
     */
    public List readFields(final String firstLine) throws IOException {
        List fields = new ArrayList();
        StringBuffer sb = new StringBuffer();
        line = firstLine;
        if (line == null) {
            return null;
        }

        if (line.length() == 0) {
            fields.add(line);
            return fields;
        }

        int i = 0;
        do {
            sb.setLength(0);
            if (i < line.length() && line.charAt(i) == textQualifier) {
                i = handleQuotedField(sb, /* skip quote */ ++i);
            } else {
                i = handlePlainField(sb, i);
            }
            fields.add(sb.toString());
            i++;
        } while (i < line.length());

        return fields;
    }

    /**
     * Closes the input CSV file.
     *
     * @throws IOException if an error occurs while closing the file
     */
    public void close() throws IOException {
        in.close();
    }

    /**
     * Handles a quoted field.
     *
     * TODO: Sehr empfindlich gegen Füllzeichen zwischen
     * TODO: schließendem Quote und Feld- oder Zeilenende
     *
     * @return index of next separator
     * @throws IOException if input cannot be read
     */
    protected int handleQuotedField(final StringBuffer sb, final int i)
      throws IOException {
        int j;
        int len = line.length();
        for (j = i; j < len; j++) {
            if (line.charAt(j) == textQualifier) {
                // end quotes at end of line?
                if (j + 1 == len) {
                    // done
                    break;
                } else if (line.charAt(j + 1) == textQualifier) {
                    // skip escape char
                    j++;
                } else if (line.charAt(j + 1) == fieldSeparator) {
                    // next delimiter: skip end quotes
                    j++;
                    break;
                }
            }
            // regular character
            sb.append(line.charAt(j));
        }
        if (j >= len) {
            line = in.readLine();
            if (line == null) {
                line = String.valueOf(textQualifier);
                return 0;
            }
            sb.append('\n');
            return handleQuotedField(sb, 0);
        }
        return j;
    }

    /**
     * Handles an unquoted field.
     *
     * @return index of next separator
     */
    protected int handlePlainField(final StringBuffer sb, final int i) {
        // look for separator
        int j = line.indexOf(fieldSeparator, i);
        if (j == -1) {
            // none found
            sb.append(line.substring(i));
            return line.length();
        } else {
            sb.append(line, i, j);
            return j;
        }
    }
}
