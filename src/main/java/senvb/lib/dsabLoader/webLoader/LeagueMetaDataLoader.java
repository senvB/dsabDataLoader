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
package senvb.lib.dsabLoader.webLoader;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import senvb.lib.dsabLoader.LeagueMetaData;
import senvb.lib.dsabLoader.Season;
import senvb.lib.dsabLoader.webLoader.DataLoaderException.LoadingStage;

/**
 * Loader for leage meta data information. Collects basic information for all leagues
 * in a season.
 */
public final class LeagueMetaDataLoader {

    private static final Logger LOG = LoggerFactory.getLogger(LeagueMetaDataLoader.class);

    private LeagueMetaDataLoader() {
        throw new UnsupportedOperationException();
    }

    public static List<LeagueMetaData> loadLeagues(Season season) throws DataLoaderException {
        List<LeagueMetaData> leagues = new ArrayList<>();
        int seasonID = season.getSeasonID();
        String url = "";
        try {
            url = resolveLeagueMetaDataUrl(seasonID).toString();
            Elements leaguesTable = LoaderUtil.loadHtmlSource(url, "table tbody tr");
            //header row
            Element leagueRow = leaguesTable.first();
            if (leagueRow != null) {
                //iterate over the data rows
                while ((leagueRow = leagueRow.nextElementSibling()) != null) {
                    leagues.add(resolveLeague(leagueRow, season));
                }
            }
            return leagues;
        } catch (IOException e) {
            throw new DataLoaderException(LoadingStage.LEAGUE_META_DATA, DataLoaderException.ExceptionType.IO, e, url);
        }
    }

    private static LeagueMetaData resolveLeague(Element leagueRow, Season season) {
        String name = leagueRow.child(0).text();
        int leagueID = resolveLeagueID(leagueRow.child(2), season.getSeasonID());
        return new LeagueMetaData(season, leagueID, name);
    }

    private static int resolveLeagueID(Element idElement, int seasonID) {
        return resolveLeagueID(idElement.childNode(1).attributes(), seasonID);
    }

    private static int resolveLeagueID(Attributes attributes, int seasonID) {
        String idString = attributes.get("href").replace(Constants.LEAGUE_URL_POST + seasonID, "").replace(Constants.LEAGUE_DATA_URL_PRE, "");
        int id = -1;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            LOG.warn("Cannot resolve id from " + idString);
        }
        return id;
    }

    private static URL resolveLeagueMetaDataUrl(int seasonID) throws MalformedURLException {
        String url = Constants.BASE_URL + Constants.SEASON_URL + seasonID;
        return new URL(url);
    }
}
