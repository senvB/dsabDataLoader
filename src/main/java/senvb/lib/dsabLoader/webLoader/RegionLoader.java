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

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import senvb.lib.dsabLoader.Region;
import senvb.lib.dsabLoader.webLoader.DataLoaderException.LoadingStage;

/**
 * Loader for the regions within a state.
 * Every region contains information about it name, the manager, the email and homepage and an internal ID.
 */
public final class RegionLoader {

    private static final Logger LOG = LoggerFactory.getLogger(RegionLoader.class);

    private RegionLoader() {
        throw new UnsupportedOperationException();
    }

    public static List<Region> loadRegionsForState(String stateName) throws DataLoaderException {
        List<Region> regions = new ArrayList<>();
        String url = "";
        try {
            url = resolveRegionForStateUrl(stateName).toString();
            Elements regionTable = LoaderUtil.loadHtmlSource(url, "table.list tbody tr");
            //header row
            Element regionRow = regionTable.first();
            if (regionRow != null) {
                //iterate over the data rows
                while ((regionRow = regionRow.nextElementSibling()) != null) {
                    regions.add(resolveRegion(regionRow));
                }
            }
            return regions;
        } catch (IOException e) {
            throw new DataLoaderException(LoadingStage.REGION, DataLoaderException.ExceptionType.IO, e, url);
        }
    }

    private static Region resolveRegion(Element regionRow) {
        String regionName = regionRow.child(0).text();
        int regionID = extractRegionNumber(regionRow.child(0));
        String regionManager = regionRow.child(3).text();
        String homepage = regionRow.child(4).text();
        String email = resolveEmail(regionRow.child(3));
        return new Region(regionName, regionID, regionManager, email, homepage);
    }

    private static String resolveEmail(Element e) {
        if (e.children().size() > 0) {
            return e.child(0).attr("href").replace("mailto:", "");
        }
        return "";
    }

    private static URL resolveRegionForStateUrl(String stateName) throws MalformedURLException, DataLoaderException {
        String url = Constants.BASE_URL + Constants.REGION_URL_PRE + resolveStateUrlName
                (stateName) + Constants.REGION_URL_POST;
        return new URL(url);
    }

    private static String resolveStateUrlName(String stateName) throws DataLoaderException {
        String name = Constants.URL_STATE_NAMES_MAPPING.get(stateName);
        if (name != null) {
            return name;
        }
        throw new DataLoaderException(LoadingStage.REGION, DataLoaderException.ExceptionType.PARSING);
    }

    private static int extractRegionNumber(Element e) {
        if (e.children().size() > 0) {
            String regionNumberInUrl = e.child(0).attr("href");
            try {
                String regionNumberString = regionNumberInUrl.replace(Constants.REGION_NR_EXTRACT_PRE_URL, "").replace(Constants.REGION_NR_EXTRACT_POST_URL, "");
                return Integer.parseInt(regionNumberString);
            } catch (NumberFormatException ex) {
                LOG.warn("Cannot extract region number from string: " + regionNumberInUrl);
            }
        }
        return -1;
    }
}
