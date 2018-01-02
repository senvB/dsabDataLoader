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
package senvb.lib.dsabLoader.json;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import senvb.lib.dsabLoader.LeagueData;

/**
 * Reads and write league information.
 * This implementation uses JSON as data format.
 */
public final class LeagueDataJSON {

    private LeagueDataJSON() {
        throw new UnsupportedOperationException();
    }

    /**
     * Writes league information to an output stream.
     *
     * @param os the output stream
     * @param leagueData league information
     * @throws IOException thrown in case of a problem while writing the data
     */
    public static void write(OutputStream os, LeagueData leagueData) throws IOException {
        JsonAdapter<LeagueData> leagueDataAdapter = JsonHelper.getMoshi().adapter(LeagueData.class);
        String json = leagueDataAdapter.toJson(leagueData);
        IOUtils.write(json, os, Charset.defaultCharset());
    }

    /**
     * Reads league information from an input stream.
     *
     * @param is the input stream
     * @return the league information
     * @throws IOException thrown in case of a problem while reading the data
     */
    public static LeagueData read(InputStream is) throws IOException, JsonDataException {
        String json = IOUtils.toString(is, Charset.defaultCharset());
        JsonAdapter<LeagueData> leagueDataAdapter = JsonHelper.getMoshi().adapter(LeagueData.class);
        return leagueDataAdapter.fromJson(json);
    }
}
