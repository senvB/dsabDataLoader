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

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Map;

import senvb.lib.dsabLoader.LeagueData;
import senvb.lib.dsabLoader.Team;
import senvb.lib.dsabLoader.TeamResult;
import senvb.lib.dsabLoader.Teams;

public class UpdateAvailableChecker {

    public static boolean isUpdateAvailable(LeagueData oldData, Map<String, TeamRankingEntry> teamRanking) {
        return oldData == null || generateTeamRankingHash(oldData.getTeams()) != generateTeamRankingHash(teamRanking);
    }

    private static int generateTeamRankingHash(Teams teams) {
        HashCodeBuilder builder = new HashCodeBuilder();
        for (Team t : teams.getTeams()) {
            builder.append(generateTeamRankingHash(t.getName(), t.getTeamResult()));
        }
        return builder.toHashCode();
    }

    private static int generateTeamRankingHash(Map<String, TeamRankingEntry> teamRanking) {
        HashCodeBuilder builder = new HashCodeBuilder();
        for (TeamRankingEntry tre : teamRanking.values()) {
            builder.append(generateTeamRankingHash(tre.getTeamName(), tre.getResults()));
        }
        return builder.toHashCode();
    }


    private static int generateTeamRankingHash(String teamName, TeamResult result) {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(teamName).append(result);
        return builder.toHashCode();
    }
}
