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
package senvb.lib.dsabLoader;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import senvb.lib.dsabLoader.json.LeagueDataJSON;
import senvb.lib.dsabLoader.json.RegionJSON;
import senvb.lib.dsabLoader.json.SeasonJSON;
import senvb.lib.dsabLoader.webLoader.Constants;
import senvb.lib.dsabLoader.webLoader.DataLoaderException;
import senvb.lib.dsabLoader.webLoader.LeagueDataLoader;
import senvb.lib.dsabLoader.webLoader.LeagueMetaDataLoader;
import senvb.lib.dsabLoader.webLoader.RegionLoader;
import senvb.lib.dsabLoader.webLoader.SeasonOverviewLoader;

public class LoaderTest {

    private static final int BERLIN_REGION_ID = 50;
    private static final String BERLIN_REGION_NAME = "Berlin";
    private static final int SEASON_ID = 1917;

    @Test
    public void loadRegionsTest() throws DataLoaderException, IOException {
        for (String state : Constants.getStates()) {
            List<Region> regions = RegionLoader.loadRegionsForState(state);
            System.out.println("Regions for: " + state + " (" + regions.size() + ")");
            File tempFile = File.createTempFile("dartsViewer-regions", "tmp");
            try (OutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile))) {
                RegionJSON.write(os, regions);
            }
            final List<Region> r2;
            try (FileInputStream fis = new FileInputStream(tempFile)) {
                r2 = RegionJSON.read(fis);
            }
            Assert.assertEquals(regions, r2);
        }
    }

    @Test
    public void loadSeasonsTest() throws DataLoaderException, IOException {
        System.out.println("Load Seasons for " + BERLIN_REGION_NAME);
        List<Season> seasons = SeasonOverviewLoader.loadSeasons(BERLIN_REGION_ID, BERLIN_REGION_NAME);
        File tempFile = File.createTempFile("dartsViewer-seasons", "tmp");
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile))) {
            SeasonJSON.write(os, seasons);
        }
        final List<Season> s2;
        try (FileInputStream fis = new FileInputStream(tempFile)) {
            s2 = SeasonJSON.read(fis);
        }
        Assert.assertEquals(seasons, s2);
    }

    @Test
    public void loadLeaguesTest() throws DataLoaderException, IOException {
        System.out.println("Load Leagues for season ID " + SEASON_ID);
        Season season = new Season("seasonName", new Date(), new Date(), SEASON_ID, BERLIN_REGION_ID, BERLIN_REGION_NAME);
        List<LeagueMetaData> leagueMetaData = LeagueMetaDataLoader.loadLeagues(season);
        System.out.println("  number of leagues: " + leagueMetaData.size());
        for (LeagueMetaData lmd : leagueMetaData) {
            System.out.println("Load LeagueData " + lmd.getLeagueID());
            LeagueData leagueData = LeagueDataLoader.loadLeagueData(lmd, new TestProgressListener());
            String fName = "leaguesDataInSeason-" + lmd.getRegionID() + "-"
                    + lmd.getSeasonID() + "-" + lmd.getLeagueID();
            File tempFile = File.createTempFile(fName, "tmp");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                LeagueDataJSON.write(fos, leagueData);
            }
            final LeagueData leagueData2;
            try (FileInputStream fis = new FileInputStream(tempFile)) {
                leagueData2 = LeagueDataJSON.read(fis);
            }
            Assert.assertEquals(leagueData, leagueData2);
        }
    }

    private static class TestProgressListener implements LeagueDataLoader.LeagueDataLoaderProgressListener {

        @Override
        public void update(Step step) {
            System.out.println("  Loading: " + step.name());
        }
    }

}
