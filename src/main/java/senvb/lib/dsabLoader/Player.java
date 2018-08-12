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
 * A darts player and the results within the current league.
 */
public class Player implements Serializable {

    /** static information about the player */
    private final PlayerData playerData;
    /** dynamic information about the player */
    private final PlayerResult playerResult;

    public Player(PlayerData data, PlayerResult result) {
        this.playerData = data;
        this.playerResult = result;

    }

    public final int getRank() {
        return this.playerResult.getRank();
    }

    public final int getGamesPos() {
        return this.playerResult.getGamesPos();
    }

    public final int getGamesNeg() {
        return this.playerResult.getGamesNeg();
    }

    public final int getSetsPos() {
        return this.playerResult.getSetsPos();
    }

    public final int getSetsNeg() {
        return this.playerResult.getSetsNeg();
    }

    public final String getName() {
        return this.playerData.getName();
    }

    public final int getTeamID() {
        return this.playerData.getTeamID();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player other = (Player) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.playerData, other.playerData)
                .append(this.playerResult, other.playerResult);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.playerData)
                .append(this.playerResult);
        return builder.build();
    }
}
