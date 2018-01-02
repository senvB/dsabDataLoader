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
 * Information about a region in a state. Each region has a manager and an internal ID.
 */
public class Region implements Serializable {

    private static final long serialVersionUID = 1;

    /**
     * the homepage for this region
     */
    private final String homepage;
    /**
     * the region manager
     */
    private final String regionManager;
    /**
     * email address of the region manager
     */
    private final String regionManagerEmail;
    /**
     * internal region ID
     */
    private final int regionID;
    /**
     * name of the region
     */
    private final String regionName;


    public Region(String regionName, int regionID, String regionManager, String regionManagerEmail, String homepage) {
        this.homepage = homepage;
        this.regionManager = regionManager;
        this.regionManagerEmail = regionManagerEmail;
        this.regionID = regionID;
        this.regionName = regionName;
    }

    public int getRegionID() {
        return this.regionID;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public String getRegionManager() {
        return this.regionManager;
    }

    public String getRegionManagerEmail() {
        return this.regionManagerEmail;
    }

    public String getHomepage() {
        return this.homepage;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Region other = (Region) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.regionID, other.regionID)
                .append(this.regionManager, other.regionManager)
                .append(this.regionManagerEmail, other.regionManagerEmail)
                .append(this.homepage, other.homepage)
                .append(this.regionName, other.regionName);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(regionManager).append(regionManagerEmail).append(homepage).append(regionID).append(regionName);
        return builder.build();
    }

}
