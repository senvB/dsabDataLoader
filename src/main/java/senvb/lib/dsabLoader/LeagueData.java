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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Optional;

import senvb.lib.dsabLoader.utils.RankingUtil;

/**
 * Information for a league during a season in a region and state.
 * The league contains meta data information as well as the teams, the player and the matches.
 */
public class LeagueData implements Serializable {

    public enum RankingType {
        FULL,

        HOME,

        AWAY,

        FIRST_HALF,

        SECOND_HALF
    }

    /** all matches in this league */
    private final Matches matches;
    /** the league meta data */
    private final LeagueMetaData metaData;
    /** all players in this league */
    private final Players players;
    /** all teams playing in this league */
    private final Teams teams;

    public LeagueData(LeagueMetaData lmd, Players player, Matches match, Teams team) {
        metaData = lmd;
        players = player;
        teams = team;
        matches = match;
    }

    public final int getRegionID() {
        return metaData.getRegionID();
    }

    public final int getSeasonID() {
        return metaData.getSeasonID();
    }

    public final int getLeagueID() {
        return metaData.getLeagueID();
    }

    public final Teams getTeams() {
        return teams;
    }

    public final Teams getTeamsWithRanking(RankingType rt) {
        switch (rt) {
            case AWAY:
                return RankingUtil.calculateAwayRanking(teams, matches);
            case FIRST_HALF:
                return RankingUtil.calculateFirstHalfRanking(teams, matches);
            case HOME:
                return RankingUtil.calculateHomeRanking(teams, matches);
            case SECOND_HALF:
                return RankingUtil.calculateSecondHalfRanking(teams, matches);
            case FULL:
            default:
                return getTeams();
        }
    }

    public final Matches getMatches() {
        return matches;
    }

    public final Players getPlayers() {
        return players;
    }

    public final Optional<Team> getTeamByName(String teamName) {
        return teams.getTeamByName(teamName);
    }

    public final Optional<Team> getTeamByNumber(int id) {
        return teams.getTeamByNumber(id);
    }

    public final String getName() {
        return metaData.getName();
    }

    public final LeagueMetaData getLeagueMetaData() {
        return metaData;
    }

    public final boolean hasPlayers() {
        return players.getNumberOfPlayers() > 0;
    }

//    public final List<Match> getMatchesForTeam(int teamID) {
//        return Collections.unmodifiableList(matches.getMatchesForTeam(teamID));
//    }
//
//    final int getTeamIDByName(String teamName) {
//        return this.teams.getTeamIDByName(teamName);
//    }
//
//    final Set<String> getTeamNames() {
//        return Collections.unmodifiableSet(this.teams.getTeamNames());
//    }
//
//    final int getNumberOfTeams() {
//        return this.teams.getNumberOfTeams();
//    }
//
//    final Match getMatchByID(int mID) {
//        return matches.getMatchesByID(mID);
//    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LeagueData other = (LeagueData) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(metaData, other.metaData)
                .append(players, other.players)
                .append(teams, other.teams)
                .append(matches, other.matches);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(metaData)
                .append(players)
                .append(teams)
                .append(matches);
        return builder.build();
    }
}
