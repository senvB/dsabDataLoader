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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import senvb.lib.dsabLoader.LeagueMetaData;
import senvb.lib.dsabLoader.Player;
import senvb.lib.dsabLoader.PlayerData;
import senvb.lib.dsabLoader.PlayerResult;
import senvb.lib.dsabLoader.Players;
import senvb.lib.dsabLoader.webLoader.DataLoaderException.LoadingStage;

/**
 * Loads the current player ranking for a league.
 */
class CurrentPlayerRankingLoader {

    private static final Logger LOG = LoggerFactory.getLogger(CurrentPlayerRankingLoader.class);

    private CurrentPlayerRankingLoader() {
        throw new UnsupportedOperationException();
    }

    static Players loadCurrentPlayerRanking(LeagueMetaData lmd, Map<String, Integer> mappingTeamID) throws DataLoaderException {
        List<Player> players = new ArrayList<>();
        String url = "";
        try {
            url = resolveCurrentPlayerRankingUrl(lmd).toString();
            Element rankingTable = LoaderUtil.loadHtmlSource(url, "table").last();
            //header row
            Element rankingRow = rankingTable.select("tbody tr").first();
            if (rankingRow != null) {
                //iterate over the data rows
                while ((rankingRow = rankingRow.nextElementSibling()) != null) {
                    players.add(resolvePlayer(rankingRow, mappingTeamID));
                }
            }
        } catch (IOException e) {
            throw new DataLoaderException(LoadingStage.CURRENT_PLAYER_RANKING, DataLoaderException.ExceptionType.IO, e, url);
        }
        return new Players(players);
    }

    private static Player resolvePlayer(Element rankingRow, Map<String, Integer> mappingTeamID) {
        String teamString = rankingRow.child(2).text();
        if (teamString.contains("(kein Essen)") || teamString.contains("(Kein Essen)")) {
            teamString = teamString.replace("(kein Essen)", "").replace("(Kein Essen)", "");
        }
        String rankString = rankingRow.child(0).text();
        String name = rankingRow.child(1).text();

        int rank = -1;
        try {
            rank = Integer.parseInt(rankString.replace(".", ""));
        } catch (NumberFormatException e) {
            LOG.warn("Cannot resolve the rank from a string: " + rankString);
        }
        int gamesPos = resolveNumber(rankingRow.child(3), "games pos");
        int gamesNeg = resolveNumber(rankingRow.child(5), "games neg");
        int setsPos = resolveNumber(rankingRow.child(6), "sets pos");
        int setsNeg = resolveNumber(rankingRow.child(8), "sets neg");

        int teamID = mappingTeamID.get(teamString);
        return new Player(new PlayerData(name, teamID), new PlayerResult(rank, gamesPos, gamesNeg, setsPos, setsNeg));
    }

    private static int resolveNumber(Element numberElement, String type) {
        try {
            return Integer.parseInt(numberElement.text().replace(".", ""));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            LOG.warn("Cannot resolve " + type + " from " + numberElement);
        }
        return -1;
    }

    private static URL resolveCurrentPlayerRankingUrl(LeagueMetaData lmd) throws MalformedURLException {
        String url = Constants.BASE_URL + Constants.LEAGUE_DATA_URL_PRE + lmd.getLeagueID() +
        Constants.PLAYER_URL_POST + lmd.getSeasonID();
        return new URL(url);
    }

}
