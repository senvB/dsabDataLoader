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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Collection of players in a league
 */
public class Players implements Serializable {

    private static final long serialVersionUID = 1;

    private static final Comparator<Player> PLAYER_RANKING_COMPARATOR = new PlayerRankingSorter();

    private final List<Player> players = new ArrayList<>();

    public Players(List<Player> players) {
        this.players.addAll(players);
        this.players.sort(PLAYER_RANKING_COMPARATOR);
    }

    public List<Player> getPlayerRanking() {
        return Collections.unmodifiableList(this.players);
    }

//    public List<Player> getPlayerFromTeam(int teamID) {
//        List<Player> teamPlayer = new ArrayList<>();
//        for (Player p : this.players) {
//            if (p.getTeamID() == teamID) {
//                teamPlayer.add(p);
//            }
//        }
//        return Collections.unmodifiableList(teamPlayer);
//    }

//    public int getNumberOfPlayers() {
//        return this.players.size();
//    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Players other = (Players) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.players, other.players);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.players);
        return builder.build();
    }

    private static class PlayerRankingSorter implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            if (p1.getRank() < p2.getRank()) {
                return -1;
            }
            return 1;
        }
    }
}
