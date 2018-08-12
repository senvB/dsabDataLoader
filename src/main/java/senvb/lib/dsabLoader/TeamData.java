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
 * Static information about a team. The data will not change during the season.
 */
public class TeamData implements Serializable {

    /**
     * the address of the venue
     */
    private final String address;
    /**
     * the name of the team captain
     */
    private final String captain;
    /**
     * the name of the team
     */
    private final String name;
    /**
     * phone number to contact the team
     */
    private final String phoneNumber;
    /**
     * internal team ID
     */
    private final int teamID;
    /**
     * the location where the team plays its matches
     */
    private final String venue;

    public TeamData(int teamID, String name, String captain, String address, String phoneNumber, String venue) {
        this.teamID = teamID;
        this.name = name;
        this.captain = captain;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.venue = venue;
    }

    String getName() {
        return this.name;
    }

    String getCaptain() {
        return this.captain;
    }

    String getAddress() {
        return this.address;
    }

    String getPhoneNumber() {
        return this.phoneNumber;
    }

    final int getTeamID() {
        return this.teamID;
    }

    String getVenue() {
        return this.venue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TeamData other = (TeamData) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.teamID, other.teamID)
                .append(this.name, other.name)
                .append(this.captain, other.captain)
                .append(this.address, other.address)
                .append(this.phoneNumber, other.phoneNumber)
                .append(this.venue, other.venue);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.teamID)
                .append(this.name)
                .append(this.captain)
                .append(this.address)
                .append(this.phoneNumber)
                .append(this.venue);
        return builder.build();
    }


}
