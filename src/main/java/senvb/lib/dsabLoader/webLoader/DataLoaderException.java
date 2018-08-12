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

    /**
     * Loading stage defines at which stage the loading of information failed.
     */
    public enum LoadingStage {
        CURRENT_PLAYER_RANKING, SEASON_OVERVIEW, LEAGUE_META_DATA, TEAM_RANKING, GAME_PLAN, CURRENT_RESULTS, REGION
    }

    /**
     * Exception type defines at which level of a stage the error occured.
     */
    public enum ExceptionType {
        IO, PARSING
    }

    private final LoadingStage loadingStage;

    private final ExceptionType exceptionType;

    private final String url;

    public DataLoaderException(LoadingStage loadingStage, ExceptionType eType, Throwable t, String url) {
        super(t);
        this.loadingStage = loadingStage;
        this.url = url;
        this.exceptionType = eType;
    }

    public DataLoaderException(LoadingStage loadingStage, ExceptionType eType) {
        this(loadingStage, eType, null, "");
    }

    public LoadingStage getLoadingStage() {
        return loadingStage;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public String getUrl() {
        return url;
    }

}
