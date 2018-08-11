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

import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.ToJson;

import java.io.IOException;
import java.util.Date;


public class JsonHelper {

    /**
     * Generates and returns Moshi JSON marshaller/unmarshaller including relevant adapter.
     *
     * @return Moshit JSON marshaller/unmarshaller
     */
    public static Moshi getMoshi() {
        return new Moshi.Builder().add(new DateJsonAdapter()).build();
    }

    /**
     * Adapter to convert a date to and from JSON.
     */
    private static class DateJsonAdapter extends JsonAdapter<Date> {

        @FromJson
        @Override
        public Date fromJson(JsonReader reader) throws IOException {
            return new Date(Long.parseLong(reader.nextString()));
        }

        @ToJson
        @Override
        public void toJson(JsonWriter writer, Date date) throws IOException {
            writer.value(date.getTime());
        }
    }
}
