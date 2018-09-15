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
package senvb.lib.dsabLoader.webLoader;

import senvb.lib.dsabLoader.TeamResult;

class TeamRankingEntry {

    private final String captain;
    private final TeamResult result;
    private final String teamName;
    private final boolean offersFood;

    TeamRankingEntry(String team, String captain, TeamResult result, boolean offersFood) {
        this.result = result;
        this.teamName = team;
        this.captain = captain;
        this.offersFood = offersFood;
    }

    final String getTeamName() {
        return this.teamName;
    }

    final String getCaptain() {
        return this.captain;
    }

    final TeamResult getResults() {
        return this.result;
    }

    final boolean offersFood() {
        return offersFood;
    }

}
