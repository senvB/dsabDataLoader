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
 * Collects meta data for a league like season information, league name and internal ID.
 */
public class LeagueMetaData implements Serializable {

    /**
     * the season in which thia league runs
     */
    private final Season season;
    /**
     * the internal league ID
     */
    private final int leagueID;
    /**
     * the name of the league
     */
    private final String leagueName;

    public LeagueMetaData(Season season, int leagueID, String leagueName) {
        this.season = season;
        this.leagueID = leagueID;
        this.leagueName = leagueName;
    }

    public final String getName() {
        return leagueName;
    }

    public final int getSeasonID() {
        return season.getSeasonID();
    }

    public final int getLeagueID() {
        return leagueID;
    }

    public final int getRegionID() {
        return season.getRegionID();
    }

    public final String getSeasonName() {
        return season.getName();
    }

    public final String getRegionName() {
        return season.getRegionName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LeagueMetaData other = (LeagueMetaData) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.season, other.season)
                .append(this.leagueID, other.leagueID)
                .append(this.leagueName, other.leagueName);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.season)
                .append(this.leagueID)
                .append(this.leagueName);
        return builder.build();
    }

    public Date getEndOfSeason() {
        return season.getEndDate();
    }

    public Date getStartOfSeason() {
        return season.getStartDate();
    }
}
