package org.evolvis.tartools.csvfile.example;

/*-
 * The CSVFile tools are a collection of classes to deal with CSV files
 * (comma-separated values). They have quite a history. This edition is
 * developed by ⮡ tarent and published under the terms and conditions of
 * the GNU LGPL (Lesser or Library General Public License), any version,
 * as published by the Free Software Foundation. Some individual classes
 * are available as well under more liberal terms, this isn’t.
 * Licensor is tarent solutions GmbH, http://www.tarent.de/
 *
 * Copyright holders and contributors:
 *
 * Copyright © 2018, 2019 mirabilos <t.glaser@tarent.de>
 * Licensor is tarent solutions GmbH, http://www.tarent.de/
 *
 * Formerly developed as part of VerA.web:
 * Copyright © 2016 Атанас Александров <a.alexandrov@tarent.de>
 * Copyright © 2006 Christoph Jerolimov <jerolimov@gmx.de>
 * Copyright © 2008 Carsten Klein <c.klein@tarent.de>
 * Copyright © 2005 Michael Klink <m.klink@tarent.de>
 * Copyright © 2013, 2015, 2018 mirabilos <t.glaser@tarent.de>
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

import org.evolvis.tartools.csvfile.CSVFileWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * {@link CSVFileWriter} that handles fields whose content contains the quote char
 * in the same broken way CSVFile 1.0 did.
 *
 * @author mirabilos (t.glaser@tarent.de)
 */
public class CSVFile10Writer extends CSVFileWriter {
    /**
     * CSVFileWriter constructor with given field separator and text qualifier.
     *
     * @param outputFileName The name of the CSV file to be opened for writing
     * @param sep            The field separator to be used; overwrites the default one
     * @param qual           The text qualifier to be used; overwrites the default one
     * @throws IOException if an error occurs while creating the file
     */
    public CSVFile10Writer(final String outputFileName, final char sep, final char qual)
      throws IOException {
        this(new FileWriter(outputFileName), sep, qual);
    }

    /**
     * CSVFileWriter constructor with given field separator and text qualifier.
     *
     * @param writer The Writer to be opened for writing
     * @param sep    The field separator to be used; overwrites the default one
     * @param qual   The text qualifier to be used; overwrites the default one
     */
    @SuppressWarnings("WeakerAccess")
    public CSVFile10Writer(final Writer writer, final char sep, final char qual) {
        super(writer, sep, qual);
        // just so IntelliJ does not think access could be package-private
        out.flush();
    }

    /**
     * Prepares a field for output by stringifying the passed object and quoting it.
     *
     * ⚠ WARNING ⚠ handles field content containing separators wrong!
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
          fieldString.indexOf(textQualifier) == 0) {
            return textQualifier + fieldString.replaceAll(String.valueOf(textQualifier),
              new String(new char[] { textQualifier, textQualifier })) + textQualifier;
        }
        return fieldString;
    }
}
