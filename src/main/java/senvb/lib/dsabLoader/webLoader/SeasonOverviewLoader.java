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

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import senvb.lib.dsabLoader.Season;
import senvb.lib.dsabLoader.webLoader.DataLoaderException.ExceptionType;

/**
 * Loads an overview of all seasons played in a region.
 */
public final class SeasonOverviewLoader {

    private static final Logger LOG = LoggerFactory.getLogger(SeasonOverviewLoader.class);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    private SeasonOverviewLoader() {
        throw new UnsupportedOperationException();
    }

    public static List<Season> loadSeasons(int regionID, String regionName) throws DataLoaderException {
        List<Season> seasons = new ArrayList<>();
        String url  ="";
        try {
            url = resolveSeasonOverviewUrl(regionID).toString();
            Elements seasonsTable = LoaderUtil.loadHtmlSource(url, "table tbody tr");
            //header row
            Element seasonRow = seasonsTable.first();
            if (seasonRow != null) {
                //iterate over the data rows
                while ((seasonRow = seasonRow.nextElementSibling()) != null) {
                    seasons.add(resolveSeason(seasonRow, regionID, regionName));
                }
            }
            return seasons;
        } catch (IOException e) {
            throw new DataLoaderException(ExceptionType.SEASON_OVERVIEW, e, url);
        }
    }

    private static Season resolveSeason(Element seasonRow, int regionID, String regionName) {
        String name = seasonRow.child(0).text();
        int id = resolveID(seasonRow.child(0).child(0));
        String start = seasonRow.child(1).text();
        String end = seasonRow.child(2).text();
        Date startDate = resolveDate(start);
        Date endDate = resolveDate(end);
        return new Season(name, startDate, endDate, id, regionID, regionName);
    }

    private static URL resolveSeasonOverviewUrl(int regionID) throws MalformedURLException {
        StringBuilder url = new StringBuilder();
        url.append(Constants.BASE_URL).append(Constants.SEASON_OVERVIEW_URL).append(regionID);
        return new URL(url.toString());
    }

    private static int resolveID(Element idElement) {
        Element elementWithRef = idElement;
        if (idElement.children().size() == 1) {
            //first element in row is marked in bold (= additional child)
            elementWithRef = idElement.child(0);
        }
        return resolveSeasonIDFromHref(elementWithRef.attributes());
    }

    private static int resolveSeasonIDFromHref(Attributes attributes) {
        try {
            return Integer.parseInt(attributes.get("href").replace(Constants.SEASON_URL, ""));
        } catch (NumberFormatException e) {
            LOG.warn("Cannot rtesolve seson ID from " + attributes);
        }
        return -1;
    }

    private static Date resolveDate(String dateString) {
        try {
            return DATE_FORMAT.parse(dateString.trim());
        } catch (ParseException e) {
            LOG.warn("Cannot parse date from " + dateString);
        }
        return new Date();
    }
}
