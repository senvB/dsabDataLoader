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

public class PlayerData implements Serializable {

    private static final long serialVersionUID = 1;

    /**
     * name of the player
     */
    private final String name;
    /**
     * the team of the player
     */
    private final int teamID;

    public PlayerData(String name, int team) {
        this.name = name;
        this.teamID = team;
    }

    final String getName() {
        return this.name;
    }

    final int getTeamID() {
        return this.teamID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerData other = (PlayerData) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.teamID, other.teamID)
                .append(this.name, other.name);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.teamID)
                .append(this.name);
        return builder.build();
    }
}
