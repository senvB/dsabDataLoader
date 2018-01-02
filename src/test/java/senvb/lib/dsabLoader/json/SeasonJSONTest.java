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

import senvb.lib.dsabLoader.Season;
import senvb.lib.dsabLoader.helper.TestDataReader;

@RunWith(DataProviderRunner.class)
public class SeasonJSONTest {

    @DataProvider
    public static Object[][] seasonDataprovider() throws IOException {
        List<Season> seasonList1 = new ArrayList<>();
        Season season1 = new Season("name", new Date(12345678), new Date(123456789), 42, 1896, "region");
        seasonList1.add(season1);
        String expectedData1 = TestDataReader.readData("season-1.json");
        return new Object[][]{{seasonList1, expectedData1}
        };
    }


    @Test
    @UseDataProvider("seasonDataprovider")
    public void write(List<Season> seasonData, String expectedData) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        SeasonJSON.write(os, seasonData);
        Assert.assertEquals(expectedData, new String(os.toByteArray()));
    }

    @Test
    @UseDataProvider("seasonDataprovider")
    public void read(List<Season> seasonData, String expectedBytes) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(expectedBytes.getBytes());
        List<Season> seasonDataRead = SeasonJSON.read(is);
        Assert.assertEquals(seasonData, seasonDataRead);
    }

}