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
 * The results of a match played in a league
 */
public class MatchResult implements Serializable {

    /** the games won by the away team */
    private final int awayGames;
    /** the points for the away team */
    private final int awayPoints;
    /** the sets won by the away team */
    private final int awaySets;
    /** the gaymes won by the home team */
    private final int homeGames;
    /** the points for the home team */
    private final int homePoints;
    /** the sets won by the home team */
    private final int homeSets;


    public MatchResult(int homePoints, int awayPoints, int homeGames, int awayGames, int homeSets, int awaySets) {
        this.homePoints = homePoints;
        this.awayPoints = awayPoints;
        this.homeGames = homeGames;
        this.awayGames = awayGames;
        this.homeSets = homeSets;
        this.awaySets = awaySets;
    }

    final int getHomePoints() {
        return homePoints;
    }

    final int getAwayPoints() {
        return awayPoints;
    }

    final int getHomeGames() {
        return homeGames;
    }

    final int getAwayGames() {
        return awayGames;
    }

    final int getHomeSets() {
        return homeSets;
    }

    final int getAwaySets() {
        return awaySets;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MatchResult other = (MatchResult) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.homePoints, other.homePoints)
                .append(this.awayPoints, other.awayPoints)
                .append(this.homeGames, other.homeGames)
                .append(this.awayGames, other.awayGames)
                .append(this.homeSets, other.homeSets)
                .append(this.awaySets, other.awaySets);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.homePoints)
                .append(this.awayPoints)
                .append(this.homeGames)
                .append(this.awayGames)
                .append(this.homeSets)
                .append(this.awaySets);
        return builder.build();
    }

    boolean isPlayed() {
        return homePoints != 0 || awayPoints != 0;
    }
}
