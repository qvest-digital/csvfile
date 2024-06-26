package org.evolvis.tartools.csvfile;

/*-
 * The CSVFile tools are a collection of classes to deal with CSV files
 * (comma-separated values). They have quite a history. This edition is
 * developed by ⮡ tarent and published under the terms and conditions of
 * the GNU LGPL (Lesser or Library General Public License), any version,
 * as published by the Free Software Foundation. Some individual classes
 * are available as well under more liberal terms, this isn’t.
 * Licensor: Qvest Digital AG, Bonn, Germany
 *
 * Copyright holders and contributors:
 *
 * Copyright © 2018, 2019 mirabilos <t.glaser@qvest-digital.com>
 * Licensor: Qvest Digital AG, Bonn, Germany
 *
 * Formerly developed as part of VerA.web:
 * Copyright © 2016 Атанас Александров <a.alexandrov@tarent.de>
 * Copyright © 2006 Christoph Jerolimov <jerolimov@gmx.de>
 * Copyright © 2008 Carsten Klein <c.klein@tarent.de>
 * Copyright © 2005 Michael Klink <m.klink@tarent.de>
 * Copyright © 2013, 2015, 2018 mirabilos <t.glaser@qvest-digital.com>
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
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS “AS IS” AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Java, the Duke mascot, and all variants of Sun’s Java “steaming coffee
 * cup” logo are trademarks of Sun Microsystems. Sun’s and James Gosling’s
 * pioneering role in inventing and promulgating (and standardising) the
 * Java language and environment is gratefully acknowledged.
 *
 * The pioneering role of Dennis Ritchie and Bjarne Stroustrup, of AT&T,
 * for inventing predecessor languages C and C++ is also gratefully
 * acknowledged.
 *
 * Copyright (C) 1999 Lucent Technologies
 * Adapted from a C++ original excerpted from “The Practice of Programming”
 * by Brian Kernighan and Rob Pike. Included by permission of the TPOP
 * http://www.informit.com/store/practice-of-programming-9780201615869
 * (at http://tpop.awl.com/ formerly) Addison-Wesley web site, which says:
 * <cite>“You may use this code for any purpose, as long as you leave the
 * copyright notice and book citation attached.”</cite> I have done so.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * CSVFileWriter is a class derived from {@link CSVFile}
 * used to format some fields into a new CSV file.
 *
 * As of CSVFile 2.0, this will handle escaping fields whose contents contain the
 * field separator, text qualifier, carriage return and/or line feed properly.
 *
 * @author Fabrizio Fazzino
 */
public class CSVFileWriter extends CSVFile {
    /**
     * The print writer linked to the CSV file to be written.
     */
    protected final PrintWriter out;

    /**
     * Separator between rows (default: LF)
     */
    protected String rowSeparator = LF;

    /**
     * CSVFileWriter constructor just needing the name of the CSV file that will be written.
     *
     * @param outputFileName The name of the CSV file to be opened for writing
     * @throws IOException if an error occurs while creating the file
     */
    public CSVFileWriter(final String outputFileName)
      throws IOException {
        this(new FileWriter(outputFileName));
    }

    /**
     * CSVFileWriter constructor with default values.
     *
     * @param writer The {@link Writer} to be opened for writing
     */
    public CSVFileWriter(final Writer writer) {
        this(writer, DEFAULT_FIELD_SEPARATOR, DEFAULT_TEXT_QUALIFIER);
    }

    /**
     * CSVFileWriter constructor with default values.
     *
     * The CSV file will be written in UTF-8 encoding.
     *
     * @param stream The {@link OutputStream} to be opened for writing
     */
    public CSVFileWriter(final OutputStream stream) {
        this(stream, DEFAULT_FIELD_SEPARATOR, DEFAULT_TEXT_QUALIFIER);
    }

    /**
     * CSVFileWriter constructor with a given field separator.
     *
     * @param outputFileName The name of the CSV file to be opened for writing
     * @param sep            The field separator to be used; overwrites the default one
     * @throws IOException if an error occurs while creating the file
     */
    public CSVFileWriter(final String outputFileName, final char sep)
      throws IOException {
        this(outputFileName, sep, DEFAULT_TEXT_QUALIFIER);
    }

    /**
     * CSVFileWriter constructor with given field separator and text qualifier.
     *
     * @param outputFileName The name of the CSV file to be opened for writing
     * @param sep            The field separator to be used; overwrites the default one
     * @param qual           The text qualifier to be used; overwrites the default one
     * @throws IOException if an error occurs while creating the file
     */
    public CSVFileWriter(final String outputFileName, final char sep, final char qual)
      throws IOException {
        this(new FileWriter(outputFileName), sep, qual);
    }

    /**
     * CSVFileWriter constructor with given field separator and text qualifier.
     *
     * The CSV file will be written in UTF-8 encoding.
     *
     * @param stream The {@link OutputStream} to be opened for writing
     * @param sep    The field separator to be used; overwrites the default one
     * @param qual   The text qualifier to be used; overwrites the default one
     */
    public CSVFileWriter(final OutputStream stream, final char sep, final char qual) {
        this(new OutputStreamWriter(stream, StandardCharsets.UTF_8), sep, qual);
    }

    /**
     * CSVFileWriter constructor with given field separator and text qualifier.
     *
     * @param writer The {@link Writer} to be opened for writing
     * @param sep    The field separator to be used; overwrites the default one
     * @param qual   The text qualifier to be used; overwrites the default one
     */
    public CSVFileWriter(final Writer writer, final char sep, final char qual) {
        super(sep, qual);
        out = new PrintWriter(new BufferedWriter(writer));
    }

    /**
     * Gets the current row separator
     *
     * @return row separator (CR, LF or CRLF) as String
     */
    public String getRowSeparator() {
        return rowSeparator;
    }

    /**
     * Sets the row separator to an arbitrary string.
     *
     * However, setting it to anything other than CR, LF, or a combination of
     * these will be ignored by the field escape code, and is therefore undefined.
     * Setting it to {@link System#lineSeparator()} might be sensible.
     *
     * @param separator to use (default: "\n")
     */
    public void setRowSeparator(final String separator) {
        rowSeparator = separator;
    }

    /**
     * Closes the output CSV file.
     */
    public void close() {
        out.flush();
        out.close();
    }

    /**
     * Joins the fields and writes them as a new line to the CSV file.
     *
     * @param fields The list of strings containing the fields
     */
    public void writeFields(final Iterable<?> fields) {
        boolean first = true;
        for (final Object field : fields) {
            if (first) {
                first = false;
            } else {
                out.print(fieldSeparator);
            }
            out.print(prepareField(field));
        }
        out.print(rowSeparator);
    }

    /**
     * Prepares a field for output by stringifying the passed object and quoting it.
     *
     * @param field to prepare
     * @return quoted string
     */
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
