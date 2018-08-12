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
import java.util.Optional;

/**
 * Collection of all teams in a league.
 */
public class Teams implements Serializable {

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
    Optional<Team> getTeamByName(String teamName) {
        return teams.stream().filter(t -> t.getName().equals(teamName)).findFirst();
    }

    /**
     * Gets the team by its internal ID.
     * @param id the internal ID
     * @return the team with this internal ID
     */
    public Optional<Team> getTeamByNumber(int id) {
        return teams.stream().filter(t -> t.getTeamID() == id).findFirst();
    }

    /**
     * Returns a list of all teams ordered according to the current rank in the league.
     * @return ordered list of the teams
     */
    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    private static class TeamRankingSorter implements Comparator<Team> {

        public int compare(Team t1, Team t2) {
            return Integer.compare(t1.getRank(), t2.getRank());
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
