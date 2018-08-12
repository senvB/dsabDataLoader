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

/**
 * Information about a team in a league. The data is stored in static data like the team name and the venus
 * and in dynamic data as the number of matches played and won and the current rank of the team in the league.
 */
public class Team implements Serializable {

    /** the static team data */
    private final TeamData teamData;
    /** the dynamic team data */
    private final TeamResult teamResult;

    public Team(TeamData teamData, TeamResult teamResult) {
        this.teamData = teamData;
        this.teamResult = teamResult;
    }

    public final TeamData getTeamData() {
        return teamData;
    }

    public final TeamResult getTeamResult() {
        return teamResult;
    }

    public final int getRank() {
        return this.teamResult.getRank();
    }

    public final String getName() {
        return this.teamData.getName();
    }

    public final String getAddress() {
        return this.teamData.getAddress();
    }

    public final String getVenue() {
        return this.teamData.getVenue();
    }

    public final String getCaptain() {
        return this.teamData.getCaptain();
    }

    public final String getPhone() {
        return this.teamData.getPhoneNumber();
    }

    public final int getTeamID() {
        return this.teamData.getTeamID();
    }

    public final int getSetsPos() {
        return this.teamResult.getSetsPos();
    }

    public final int getSetsNeg() {
        return this.teamResult.getSetsNeg();
    }

    public final int getGamesPos() {
        return this.teamResult.getGamesPos();
    }

    public final int getGamesNeg() {
        return this.teamResult.getGamesNeg();
    }

    public final int getMatchesPlayed() {
        return this.teamResult.getMatchesPlayed();
    }

    public final int getPoints() {
        return this.teamResult.getPoints();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Team other = (Team) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.teamData, other.teamData)
                .append(this.teamResult, other.teamResult);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.teamData)
                .append(this.teamResult);
        return builder.build();
    }
}
