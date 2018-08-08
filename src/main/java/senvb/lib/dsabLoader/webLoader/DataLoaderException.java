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

/**
 * Exception in case that loading the data from the web fails.
 */
public class DataLoaderException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Exception type defines at which stage the loading of information failed.
     */
    public enum ExceptionType {
        CURRENT_PLAYER_RANKING, SEASON_OVERVIEW, LEAGUE_META_DATA, TEAM_RANKING, GAME_PLAN, CURRENT_RESULTS, REGION
    }

    private final ExceptionType type;



    private final String url;

    public DataLoaderException(ExceptionType type, Throwable t, String url) {
        super(t);
        this.type = type;
        this.url = url;
    }

    public DataLoaderException(ExceptionType type) {
        super();
        this.type = type;
        this.url = "";
    }

    public final ExceptionType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

}
