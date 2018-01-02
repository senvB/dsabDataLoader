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

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import senvb.lib.dsabLoader.LeagueMetaData;
import senvb.lib.dsabLoader.Season;
import senvb.lib.dsabLoader.helper.TestDataReader;

@RunWith(DataProviderRunner.class)
public class LeagueMetaDataJSONTest {

    @DataProvider
    public static Object[][] leagueMetaDataDataprovider() throws IOException {
        List<LeagueMetaData> leagueMetaData1 = new ArrayList<>();
        Season season = new Season("season", new Date(123456788), new Date(123456789), 42, 1896, "region");
        LeagueMetaData lmd1 = new LeagueMetaData(season, 2, "season");
        leagueMetaData1.add(lmd1);
        String expectedData1 = TestDataReader.readData("leagueMetaData-1.json");
        return new Object[][]{{leagueMetaData1, expectedData1}
        };
    }

    @Test
    @UseDataProvider("leagueMetaDataDataprovider")
    public void testMetaDataWrite(List<LeagueMetaData> leagueMetaData, String expectedData) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        LeagueMetaDataJSON.write(os, leagueMetaData);
        Assert.assertEquals(expectedData, new String(os.toByteArray()));
    }

    @Test
    @UseDataProvider("leagueMetaDataDataprovider")
    public void testMetaDataRead(List<LeagueMetaData> leagueMetaData, String expectedBytes) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(expectedBytes.getBytes());
        List<LeagueMetaData> leagueMetaDataRead = LeagueMetaDataJSON.read(is);
        Assert.assertEquals(leagueMetaData, leagueMetaDataRead);
    }

}