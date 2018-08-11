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


public class Matches implements Serializable {

    private static final long serialVersionUID = 1;

    private static final Comparator<Match> MATCH_DATE_SORTER = new MatchIDSorter();

    private final List<Match> matches = new ArrayList<>();

    public Matches(List<Match> matches) {
        this.matches.addAll(matches);
       this.matches.sort(MATCH_DATE_SORTER);
    }

    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
    }

    public Match getMatchesByID(int mID) {
        for (Match m : matches) {
            if (m.getMatchID() == mID) {
                return m;
            }
        }
        return null;
    }

    private static class MatchIDSorter implements Comparator<Match> {
        public int compare(Match o1, Match o2) {
            return o1.getMatchID() - o2.getMatchID();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Matches other = (Matches) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.matches, other.matches);
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.matches);
        return builder.build();
    }
}
