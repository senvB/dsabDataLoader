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

public class PlayerResult implements Serializable {

    /**
     * games won
     */
    private final int gamesNeg;
    /**
     * games lost
     */
    private final int gamesPos;
    /**
     * current rank in the league
     */
    private final int rank;
    /**
     * sets won
     */
    private final int setsNeg;
    /**
     * sets lost
     */
    private final int setsPos;

    public PlayerResult(int rank, int gamesPos, int gamesNeg, int setsPos, int setsNeg) {
        this.gamesNeg = gamesNeg;
        this.gamesPos = gamesPos;
        this.rank = rank;
        this.setsNeg = setsNeg;
        this.setsPos = setsPos;
    }

    final int getRank() {
        return this.rank;
    }

    final int getGamesPos() {
        return this.gamesPos;
    }

    final int getGamesNeg() {
        return this.gamesNeg;
    }

    final int getSetsPos() {
        return this.setsPos;
    }

    final int getSetsNeg() {
        return this.setsNeg;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerResult other = (PlayerResult) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.gamesNeg, other.gamesNeg)
                .append(this.gamesPos, other.gamesPos)
                .append(this.rank, other.rank)
                .append(this.setsNeg, other.setsNeg)
                .append(this.setsPos, other.setsPos);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.gamesNeg)
                .append(this.gamesPos)
                .append(this.rank)
                .append(this.setsNeg)
                .append(this.setsPos);
        return builder.build();
    }
}
