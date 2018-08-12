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
 * Defines a season in a region organized by a darts league administration.
 * A season usually has a name, internal IDs and a start and end date.
 */
public class Season implements Serializable {

    /** the ID of the region */
    private final int regionID;
    /** the name of the region */
    private final String regionName;
    /** the end date for this seadson*/
    private final Date seasonEndDate;
    /* the internal season ID */
    private final int seasonID;
    /* the season name */
    private final String seasonName;
    /** the start date of this season */
    private final Date seasonStartDate;

    public Season(String seasonName, Date startDate, Date endDate, int seasonID, int regionID, String regionName) {
        this.seasonName = seasonName;
        this.seasonStartDate = startDate;
        this.seasonEndDate = endDate;
        this.seasonID = seasonID;
        this.regionID = regionID;
        this.regionName = regionName;
    }

    public final String getName() {
        return this.seasonName;
    }

    public final Date getStartDate() {
        return this.seasonStartDate;
    }

    public final Date getEndDate() {
        return this.seasonEndDate;
    }

    public final int getSeasonID() {
        return this.seasonID;
    }

    public final int getRegionID() {
        return this.regionID;
    }

    public final String getRegionName() {
        return this.regionName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Season other = (Season) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.regionID, other.regionID)
                .append(this.seasonID, other.seasonID)
                .append(this.seasonStartDate, other.seasonStartDate)
                .append(this.seasonEndDate, other.seasonEndDate)
                .append(this.regionName, other.regionName)
                .append(this.seasonName, seasonName);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(seasonName).append(seasonStartDate).append(seasonEndDate).append(seasonID).append(regionID).append(regionName);
        return builder.build();
    }
}
