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
import java.util.List;

import senvb.lib.dsabLoader.Region;
import senvb.lib.dsabLoader.helper.TestDataReader;


@RunWith(DataProviderRunner.class)
public class RegionJSONTest {

    @DataProvider
    public static Object[][] regionDataprovider() throws IOException {
        List<Region> regionList1 = new ArrayList<>();
        Region region1 = new Region("name", 42, "manager", "email", "homepage");
        regionList1.add(region1);
        String expectedData1 = TestDataReader.readData("region-1.json");
        return new Object[][]{{regionList1, expectedData1}
        };
    }


    @Test
    @UseDataProvider("regionDataprovider")
    public void write(List<Region> regionData, String expectedData) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        RegionJSON.write(os, regionData);
        Assert.assertEquals(expectedData, new String(os.toByteArray()));

    }

    @Test
    @UseDataProvider("regionDataprovider")
    public void read(List<Region> regionData, String expectedBytes) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(expectedBytes.getBytes());
        List<Region> regionDataRead = RegionJSON.read(is);
        Assert.assertEquals(regionData, regionDataRead);

    }

}