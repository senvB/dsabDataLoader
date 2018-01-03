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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Collection of all teams in a league.
 */
public class Teams implements Serializable {

    private static final long serialVersionUID = 1;

    private static final Comparator<Team> TEAM_RANKING = new TeamRankingSorter();

    private final List<Team> teams = new ArrayList<>();

    public Teams(List<Team> teams) {
        this.teams.addAll(teams);
        this.teams.sort(TEAM_RANKING);
    }

    /**
     * Gets the team by its name.
     * @param teamName the name
     * @return the team or null if not found
     */
    Team getTeamByName(String teamName) {
        for (Team t : this.teams) {
            if (t.getName().equals(teamName)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Gets the team by its internal ID.
     * @param id the internal ID
     * @return the team or null if not found
     */
    public Team getTeamByNumber(int id) {
        for (Team t : this.teams) {
            if (t.getTeamID() == id) {
                return t;
            }
        }
        return null;
    }

//    public final int getNumberOfTeams() {
//        return this.teams.size();
//    }

    /**
     * Gets the team ID by its name.
     * @param teamName the name
     * @return the team ID or null if not found
     */
//    public int getTeamIDByName(String teamName) {
//        for (Team t : this.teams) {
//            if (t.getName().equals(teamName)) {
//                return t.getTeamID();
//            }
//        }
//        return -1;
//    }

    /**
     * Gets all team names.
     * @return all team names
     */
//    public Set<String> getTeamNames() {
//        Set<String> names = new HashSet<>();
//        for (Team t : this.teams) {
//            names.add(t.getName());
//        }
//        return names;
//    }

    /**
     * Returns a list of all teams ordered according to the current rank in the league.
     * @return ordered list of the teams
     */
    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    private static class TeamRankingSorter implements Comparator<Team> {

        public int compare(Team t1, Team t2) {
            if (t1.getRank() < t2.getRank()) {
                return -1;
            }
            if (t1.getRank() > t2.getRank()) {
                return 1;
            }
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Teams other = (Teams) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.teams, other.teams);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.teams);
        return builder.build();
    }
}
