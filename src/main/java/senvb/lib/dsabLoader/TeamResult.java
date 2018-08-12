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
 * Dynamic information about a team. The data changes over time during a season.
 */
public class TeamResult implements Serializable {

    /** games lost */
    private final int gamesNeg;
    /** games won */
    private final int gamesPos;
    /** matches play in total */
    private final int matchesPlayed;
    /** points won during the season */
    private final int points;
    /** current rank in the league */
    private final int rank;
    /** sets lost */
    private final int setsNeg;
    /** sets won */
    private final int setsPos;

    public TeamResult(int matchesPlayed, int gamesPos, int gamesNeg, int setsPos, int setsNeg, int points, int rank) {
        this.matchesPlayed = matchesPlayed;
        this.gamesPos = gamesPos;
        this.gamesNeg = gamesNeg;
        this.setsPos = setsPos;
        this.setsNeg = setsNeg;
        this.points = points;
        this.rank = rank;
    }

    int getMatchesPlayed() {
        return this.matchesPlayed;
    }

    int getGamesPos() {
        return this.gamesPos;
    }

    int getGamesNeg() {
        return this.gamesNeg;
    }

    int getSetsPos() {
        return this.setsPos;
    }

    int getSetsNeg() {
        return this.setsNeg;
    }

    int getPoints() {
        return this.points;
    }

    final int getRank() {
        return this.rank;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TeamResult other = (TeamResult) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.matchesPlayed, other.matchesPlayed)
                .append(this.gamesPos, other.gamesPos)
                .append(this.gamesNeg, other.gamesNeg)
                .append(this.setsPos, other.setsPos)
                .append(this.setsNeg, other.setsNeg)
                .append(this.points, other.points)
                .append(this.rank, other.rank);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.matchesPlayed)
                .append(this.gamesPos)
                .append(this.gamesNeg)
                .append(this.setsPos)
                .append(this.setsNeg)
                .append(this.points)
                .append(this.rank);
        return builder.build();
    }

}
