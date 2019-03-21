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

/**
 * CSVFile is a Java™ class used to handle <a
 * href="https://en.wikipedia.org/wiki/Comma-separated_values">comma-separated
 * value</a> files. It is an abstract base class for {@link CSVFileReader}
 * and {@link CSVFileWriter}, so you should use one of these (or both),
 * according on what you need to do.
 *
 * The simplest example for using the classes contained in this package is
 * the following example that simply converts one CSV file into another one
 * that makes use of a different notation for field separator and text
 * qualifier. It just comprises the following lines:
 *
 * <pre>
 * package org.evolvis.tartools.csvfile;
 *
 * import java.io.IOException;
 * import java.util.List;
 *
 * public class CSVFileExample {
 *     public static void main(String[] args) throws IOException {
 *         CSVFileReader in = new CSVFileReader(&quot;csv_in.txt&quot;, ';', '&quot;');
 *         CSVFileWriter out = new CSVFileWriter(&quot;csv_out.txt&quot;, ',', '\'');
 *
 *         List fields = in.readFields();
 *         while (fields != null) {
 *             out.writeFields(fields);
 *             fields = in.readFields();
 *         }
 *
 *         in.close();
 *         out.close();
 *     }
 * }
 * </pre>
 *
 * @author Fabrizio Fazzino
 */
public abstract class CSVFile {
    /**
     * The default char used as field separator.
     */
    protected static final char DEFAULT_FIELD_SEPARATOR = ',';

    /**
     * The default char used as text qualifier
     */
    protected static final char DEFAULT_TEXT_QUALIFIER = '"';

    /**
     * The current char used as field separator.
     */
    protected char fieldSeparator;

    /**
     * The current char used as text qualifier.
     */
    protected char textQualifier;

    /**
     * CSVFile constructor with the default field separator and text qualifier.
     */
    public CSVFile() {
        this(DEFAULT_FIELD_SEPARATOR, DEFAULT_TEXT_QUALIFIER);
    }

    /**
     * CSVFile constructor with a given field separator and the default text qualifier.
     *
     * @param sep The field separator to be used; overwrites the default one
     */
    public CSVFile(final char sep) {
        this(sep, DEFAULT_TEXT_QUALIFIER);
    }

    /**
     * CSVFile constructor with given field separator and text qualifier.
     *
     * @param sep  The field separator to be used; overwrites the default one
     * @param qual The text qualifier to be used; overwrites the default one
     */
    public CSVFile(final char sep, final char qual) {
        setFieldSeparator(sep);
        setTextQualifier(qual);
    }

    /**
     * Set the current field separator.
     *
     * @param sep The new field separator to be used; overwrites the old one
     */
    public void setFieldSeparator(final char sep) {
        fieldSeparator = sep;
    }

    /**
     * Set the current text qualifier.
     *
     * @param qual The new text qualifier to be used; overwrites the old one
     */
    public void setTextQualifier(final char qual) {
        textQualifier = qual;
    }

    /**
     * Get the current field separator.
     *
     * @return The char containing the current field separator
     */
    public char getFieldSeparator() {
        return fieldSeparator;
    }

    /**
     * Get the current text qualifier.
     *
     * @return The char containing the current text qualifier
     */
    public char getTextQualifier() {
        return textQualifier;
    }
}
