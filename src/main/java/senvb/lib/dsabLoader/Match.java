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
import java.util.Date;

/**
 * A match in a league. Consists of general data about the teams playing this match and the date
 * and finally the results.
 */
public class Match implements Serializable {


    private final MatchData matchData;
    private final MatchResult matchResult;
    private final boolean isRealMatch;


    public Match(MatchData matchData, MatchResult matchResult, boolean isRealMatch) {
        this.matchData = matchData;
        this.matchResult = matchResult;
        this.isRealMatch = isRealMatch;
    }

    public final MatchData getMatchData() {
        return matchData;
    }

    public final Date getDate() {
        return this.matchData.getDate();
    }

    public final int getHome() {
        return this.matchData.getHome();
    }

    public final int getAway() {
        return this.matchData.getAway();
    }

    public final int getRound() {
        return this.matchData.getRound();
    }

    public final int getMatchID() {
        return this.matchData.getMatchID();
    }

    public final int getHomeMatches() {
        if (isPlayed()) {
            return this.matchResult.getHomeGames();
        }
        return -1;
    }

    public final int getHomeSets() {
        if (isPlayed()) {
            return this.matchResult.getHomeSets();
        }
        return -1;
    }

    public final int getHomePoints() {
        if (isPlayed()) {
            return this.matchResult.getHomePoints();
        }
        return -1;
    }

    public final int getAwayPoints() {
        if (isPlayed()) {
            return this.matchResult.getAwayPoints();
        }
        return -1;
    }

    public final int getAwayMatches() {
        if (isPlayed()) {
            return this.matchResult.getAwayGames();
        }
        return -1;
    }

    public final int getAwaySets() {
        if (isPlayed()) {
            return this.matchResult.getAwaySets();
        }
        return -1;
    }

    public final boolean isPlayed() {
        return matchResult.isPlayed();
    }

    public final boolean isRealMatch() {
        return isRealMatch;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Match other = (Match) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.matchData, other.matchData)
                .append(this.matchResult, other.matchResult);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.matchData)
                .append(this.matchResult);
        return builder.build();
    }
}
