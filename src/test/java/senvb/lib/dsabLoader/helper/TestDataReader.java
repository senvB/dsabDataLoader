/**
 *  The DSAB data loader library allows to parse information for DSAB dart leagues.
 *  Copyright (C) 2017-2018  Sven Baselau
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package senvb.lib.dsabLoader.helper;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public final class TestDataReader {

    private TestDataReader() {
        throw new UnsupportedOperationException();
    }

    public static String readData(String fileName) throws IOException {
        InputStream resourceStream = TestDataReader.class.getClassLoader().getResourceAsStream(fileName);
        if (resourceStream == null) {
            throw new IOException("Cannot read test resource: " + fileName);
        }
        return IOUtils.toString(resourceStream, Charset.defaultCharset());
    }
}
