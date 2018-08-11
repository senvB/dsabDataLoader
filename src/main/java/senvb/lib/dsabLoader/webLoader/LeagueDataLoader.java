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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import senvb.lib.dsabLoader.LeagueData;
import senvb.lib.dsabLoader.LeagueMetaData;
import senvb.lib.dsabLoader.Match;
import senvb.lib.dsabLoader.MatchData;
import senvb.lib.dsabLoader.MatchResult;
import senvb.lib.dsabLoader.Matches;
import senvb.lib.dsabLoader.Players;
import senvb.lib.dsabLoader.Team;
import senvb.lib.dsabLoader.TeamData;
import senvb.lib.dsabLoader.Teams;

/**
 * The league data loader encapsulates a series of loader to get the following infromation for a league:
 * <ul>
 *     <li>Ranking of the teams</li>
 *     <li>Ranking of the players</li>
 *     <li>Game plan</li>
 *     <li>Results</li>
 * </ul>
 *
 * The results of the individual steps are then compiled into league information.
 */
public class LeagueDataLoader {

    private static final Logger LOG = LoggerFactory.getLogger(LeagueDataLoader.class);

    private static final String[] NO_TEAM = {"keine  Mannschaft", "Kein Team"};

    public interface LeagueDataLoaderProgressListener {

        enum Step {
            //TODO the messages need to be localized for the app
            TEAM_RANKING("Teamrangliste"),

            PLAYER_RANKING("Spielerrangliste"),

            GAME_PLAN("Spielplan"),

            CURRENT_RESULTS("Resultate"),

            CHECK_FOR_UPDATE("Teamrangliste/Update check");

            private final String text;

            Step(String msg) {
                this.text = msg;
            }

            public final String getMessage() {
                return this.text;
            }

            public static int getTotalNumberOfSteps() {
                return 4;
            }
        }

        void update(Step step);
    }

    /**
     * Loads league information from the web. This method does not make use of old league information which can be used
     * to increase the speed of the loading from the web and also to preserve the internal team IDs.
     *
     * @param lData legaue meta data for the league information to be loaded
     * @param listener listener for the updates between the different steps
     * @return league information
     * @throws DataLoaderException in case loading of the league information failed
     */
    public static LeagueData loadLeagueData(LeagueMetaData lData, LeagueDataLoaderProgressListener listener) throws DataLoaderException {
        return loadLeagueData(lData, listener, null);
    }

    /**
     * Loads league information from the web and uses old league information which
     * to increase the speed of the loading from the web and also to preserve the internal team IDs.
     *
     * @param lData legaue meta data for the league information to be loaded
     * @param listener listener for the updates between the different steps
     * @param oldData previous information about this league
     * @return league information
     * @throws DataLoaderException in case loading of the league information failed
     */
    public static LeagueData loadLeagueData(LeagueMetaData lData, LeagueDataLoaderProgressListener listener, LeagueData oldData) throws DataLoaderException {
        if (listener != null) {
            if (oldData == null) {
                listener.update(LeagueDataLoaderProgressListener.Step.TEAM_RANKING);
            } else {
                listener.update(LeagueDataLoaderProgressListener.Step.CHECK_FOR_UPDATE);
            }
        }
        //load current team ranking from web
        Map<String, TeamRankingEntry> teamRanking = CurrentTeamRankingLoader.loadCurrentTeamRanking(lData);

        //chekc if the loaded ranking is different from the old ranking (if available)
        if (!UpdateAvailableChecker.isUpdateAvailable(oldData, teamRanking)) {
            return oldData;
        }

        // generate team IDs or take old ones over to have consistency
        Map<String, Integer> internalTeamIdMapping = resolveInternalIDs(oldData, teamRanking);

        //load current player ranking
        if (listener != null) {
            listener.update(LeagueDataLoaderProgressListener.Step.PLAYER_RANKING);
        }
        Players players;
        try {
            players = CurrentPlayerRankingLoader.loadCurrentPlayerRanking(lData, internalTeamIdMapping);
        } catch (DataLoaderException e) {
            LOG.error("Cannot read players ranking when reading from " + e.getUrl(), e);
            players = new Players(Collections.emptyList());
        }

        // update results of the matches
        if (listener != null) {
            listener.update(LeagueDataLoaderProgressListener.Step.CURRENT_RESULTS);
        }
        Map<Integer, MatchResult> results = CurrentResultListLoader.loadCurrentResultList(lData, internalTeamIdMapping);
        LeagueData ld;
        if (oldData == null) {
            if (listener != null) {
                listener.update(LeagueDataLoaderProgressListener.Step.GAME_PLAN);
            }
            GameAndTeamData gamesTeams = GamePlanLoader.loadGamePlan(lData, internalTeamIdMapping);
            Teams teams = resolveTeams(teamRanking, gamesTeams.getTeamData(), internalTeamIdMapping);
            Matches matches = resolveMatches(teams, gamesTeams.getMatchData(), results);
            ld = new LeagueData(lData, players, matches, teams);
        } else {
            Matches matches = resolveMatches(oldData , results);
            Teams teams = resolveTeams(teamRanking, oldData.getTeams(), internalTeamIdMapping);
            ld = new LeagueData(lData, players, matches, teams);
        }
        return ld;
    }

    private static Matches resolveMatches(LeagueData oldData, Map<Integer, MatchResult> results) {
        Matches gamesData = oldData.getMatches();
        List<Match> matches = new ArrayList<>();
        for (int i : results.keySet()) {
            MatchData md = gamesData.getMatchesByID(i).getMatchData();
            if (md == null) {
                LOG.error("Incomplete match data!!");
            } else {
                Team home = oldData.getTeamByNumber(md.getHome()).get();
                Team away = oldData.getTeamByNumber(md.getAway()).get();
                matches.add(new Match(md, results.get(i), isRealMatch(home, away)));
            }
        }
        return new Matches(matches);
    }

    private static boolean isRealMatch(Team home, Team away) {
        return isRealTeam(home.getName()) && isRealTeam(away.getName());
    }

    private static boolean isRealTeam(String teamName) {
        for (String noTeam : NO_TEAM) {
            if (teamName.toLowerCase().contains(noTeam.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    private static Matches resolveMatches(Teams teams, Map<Integer, MatchData> gamesData, Map<Integer, MatchResult> results) {
        List<Match> matches = new ArrayList<>();
        for (int i : results.keySet()) {
            MatchData md = gamesData.get(i);
            if (md == null) {
                LOG.error("Incomplete match data!!");
            } else {
                Team home = teams.getTeamByNumber(md.getHome()).get();
                Team away = teams.getTeamByNumber(md.getAway()).get();
                matches.add(new Match(md, results.get(i), isRealMatch(home, away)));
            }
        }
        return new Matches(matches);
    }

    private static Map<String, Integer> resolveInternalIDs(LeagueData oldData, Map<String, TeamRankingEntry> teamRanking) {
        Map<String, Integer> mapping = new HashMap<>();
        if (oldData == null) {
            int idCounter = 0;
            for (String name : teamRanking.keySet()) {
                mapping.put(name, idCounter++);
            }
        } else {
            for (Team t : oldData.getTeams().getTeams()) {
                mapping.put(t.getName(), (t.getTeamID()));
            }
        }
        return mapping;
    }

    private static Teams resolveTeams(Map<String, TeamRankingEntry> teamRanking, Teams teamData, Map<String, Integer> mappingTeamID) {
        List<Team> teams = new ArrayList<>();
        for (Entry<String, TeamRankingEntry> entry : teamRanking.entrySet()) {
            String teamName = entry.getKey();
            int teamID = mappingTeamID.get(teamName);
            TeamRankingEntry teamRank = entry.getValue();
            TeamData td = teamData.getTeamByNumber(teamID).get().getTeamData();
            teams.add(new Team(td, teamRank.getResults()));
        }
        return new Teams(teams);
    }


    private static Teams resolveTeams(Map<String, TeamRankingEntry> teamRanking, Map<String, TeamAddressData> teamAddress, Map<String, Integer> mappingTeamID) {
        List<Team> teams = new ArrayList<>();
        for (Entry<String, TeamRankingEntry> entry : teamRanking.entrySet()) {
            String teamName = entry.getKey();
            int teamID = mappingTeamID.get(teamName);
            TeamRankingEntry teamRank = entry.getValue();
            TeamAddressData teamAddr = teamAddress.get(teamName);
            String addr = "";
            String phone = "";
            String venue = "";
            if (teamAddr != null) {
                addr = teamAddr.resolveAddress();
                phone = teamAddr.getPhone();
                venue = teamAddr.getVenue();
            }
            TeamData td = new TeamData(teamID, teamName, teamRank.getCaptain(), addr, phone, venue);
            teams.add(new Team(td, teamRank.getResults()));
        }
        return new Teams(teams);
    }
}
