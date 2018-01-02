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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import senvb.lib.dsabLoader.LeagueData;
import senvb.lib.dsabLoader.LeagueMetaData;
import senvb.lib.dsabLoader.Matches;
import senvb.lib.dsabLoader.Players;
import senvb.lib.dsabLoader.Teams;
import senvb.lib.dsabLoader.helper.TestDataReader;

@RunWith(DataProviderRunner.class)
public class LeagueDataJSONTest {

    @DataProvider
    public static Object[][] leagueDataprovider() throws IOException {
        LeagueMetaData lmd = null;
        Players player = null;
        Matches matches = null;
        Teams teams = null;
        //TODO requires fake data to run the test
        LeagueData league1 = new LeagueData(lmd, player, matches, teams);
        String expectedData1 = TestDataReader.readData("league-1.json");
        return new Object[][]{{league1, expectedData1}
        };
    }

    @Ignore
    @Test
    @UseDataProvider("leagueDataprovider")
    public void write(LeagueData leagueData, String expectedData) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        LeagueDataJSON.write(os, leagueData);
        Assert.assertEquals(expectedData, new String(os.toByteArray()));
    }

    @Ignore
    @Test
    @UseDataProvider("leagueDataprovider")
    public void read(LeagueData leagueData, String expectedBytes) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(expectedBytes.getBytes());
        LeagueData leagueDataRead = LeagueDataJSON.read(is);
        Assert.assertEquals(leagueData, leagueDataRead);
    }

}