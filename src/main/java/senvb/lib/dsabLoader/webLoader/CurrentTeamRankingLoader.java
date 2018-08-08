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

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import senvb.lib.dsabLoader.LeagueMetaData;
import senvb.lib.dsabLoader.TeamResult;
import senvb.lib.dsabLoader.webLoader.DataLoaderException.ExceptionType;

/**
 * Loads the team ranking page from the web and returns the teams with its current results and ranking.
 */
class CurrentTeamRankingLoader {

    private static final Logger LOG = LoggerFactory.getLogger(CurrentTeamRankingLoader.class);

    private CurrentTeamRankingLoader() {
        throw new UnsupportedOperationException();
    }

    static Map<String, TeamRankingEntry> loadCurrentTeamRanking(LeagueMetaData lmd) throws DataLoaderException {
        Map<String, TeamRankingEntry> ranking = new HashMap<>();
        String url = "";
        try {
            url = resolveCurrentTeamRankingUrl(lmd).toString();
            Element tables = LoaderUtil.loadHtmlSource(url, "table").last();
            //header row
            Element rankingRow = tables.select("tbody tr").first();
            if (rankingRow != null) {
                //iterate over the data rows
                while ((rankingRow = rankingRow.nextElementSibling()) != null) {
                    TeamRankingEntry tre = resolveTeamRanking(rankingRow);
                    ranking.put(tre.getTeamName(), tre);
                }
            }
            return ranking;
        } catch (IOException e) {
            throw new DataLoaderException(ExceptionType.TEAM_RANKING, e, url);
        }
    }

    private static TeamRankingEntry resolveTeamRanking(Element rankingRow) {
        int rank = resolveNumber(rankingRow.child(0), "rank");
        String teamString = rankingRow.child(1).text();
        if (teamString.contains("Spielfrei")) {
            return new TeamRankingEntry(teamString, "", new TeamResult(0, 0, 0, 0, 0, 0, 0));
        }
        String captain = rankingRow.child(2).text();
        int matches = resolveNumber(rankingRow.child(3), "matches");
        int points = resolveNumber(rankingRow.child(4), "points");
        int gamesPos = resolveNumber(rankingRow.child(5), "gamesPos");
        int gamesNeg = resolveNumber(rankingRow.child(7), "gamesNeg");
        int setsPos = resolveNumber(rankingRow.child(8), "setsPos");
        int setsNeg = resolveNumber(rankingRow.child(10), "setsNeg");
        TeamResult result = new TeamResult(matches, gamesPos, gamesNeg, setsPos, setsNeg, points, rank);
        return new TeamRankingEntry(teamString, captain, result);
    }

    private static URL resolveCurrentTeamRankingUrl(LeagueMetaData lmd) throws MalformedURLException {
        StringBuilder url = new StringBuilder();
        int leagueID = lmd.getLeagueID();
        url.append(Constants.BASE_URL).append(Constants.LEAGUE_DATA_URL_PRE).append(leagueID).append(Constants.LEAGUE_URL_POST).append(lmd.getSeasonID());
        return new URL(url.toString());
    }

    private static int resolveNumber(Element numberElement, String type) {
        try {
            return Integer.parseInt(numberElement.text().replace(".", ""));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            LOG.warn("Cannot resolve " + type + " from " + numberElement);
        }
        return -1;
    }
}
