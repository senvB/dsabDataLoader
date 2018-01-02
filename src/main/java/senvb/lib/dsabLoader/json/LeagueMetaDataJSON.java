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
import com.squareup.moshi.Types;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;

import senvb.lib.dsabLoader.LeagueMetaData;

/**
 * Reads and write LeagueMetaData information.
 * This implementation uses JSON as data format.
 */
public final class LeagueMetaDataJSON {

    private static final Type LEAGUE_META_DATA_TYPE = Types.newParameterizedType(List.class, LeagueMetaData.class);

    private LeagueMetaDataJSON() {
        throw new UnsupportedOperationException();
    }

    /**
     * Writes a list of LeagueMetaData information to an output stream.
     *
     * @param os the output stream
     * @param leagueMetaData list of league meta data information
     * @throws IOException thrown in case of a problem while writing the data
     */
    public static void write(OutputStream os, List<LeagueMetaData> leagueMetaData) throws IOException {
        JsonAdapter<List<LeagueMetaData>> leagueMetaDataAdapter = JsonHelper.getMoshi().adapter(LEAGUE_META_DATA_TYPE);
        String json = leagueMetaDataAdapter.toJson(leagueMetaData);
        IOUtils.write(json, os, Charset.defaultCharset());
    }

    /**
     * Reads a list of league meta data information from an input stream.
     *
     * @param is the input stream
     * @return the list of league meta data information
     * @throws IOException thrown in case of a problem while reading the data
     */
    public static List<LeagueMetaData> read(InputStream is) throws IOException {
        String json = IOUtils.toString(is, Charset.defaultCharset());
        JsonAdapter<List<LeagueMetaData>> leagueMetaDataAdapter = JsonHelper.getMoshi().adapter(LEAGUE_META_DATA_TYPE);
        return leagueMetaDataAdapter.fromJson(json);
    }
}
