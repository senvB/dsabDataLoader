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
 * General match data.
 */
public class MatchData implements Serializable {

    /** internal ID of the away team */
    private final int away;
    /** dat when the match is played */
    private final Date date;
    /** internal ID oif the home team */
    private final int home;
    /** match ID */
    private final int matchID;
    /** number of the round the match is played */
    private final int round;

    public MatchData(int round, int gameNumber, int home, int away, Date date) {
        this.round = round;
        this.matchID = gameNumber;
        this.home = home;
        this.away = away;
        this.date = date;
    }

    final Date getDate() {
        return this.date;
    }

    public final int getRound() {
        return this.round;
    }

    public int getMatchID() {
        return this.matchID;
    }

    public int getHome() {
        return this.home;
    }

    public int getAway() {
        return this.away;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MatchData other = (MatchData) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.date, other.date)
                .append(this.round, other.round)
                .append(this.matchID, other.matchID)
                .append(this.home, other.home)
                .append(this.away, other.away);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.date)
                .append(this.round)
                .append(this.matchID)
                .append(this.home)
                .append(this.away);
        return builder.build();
    }

}
