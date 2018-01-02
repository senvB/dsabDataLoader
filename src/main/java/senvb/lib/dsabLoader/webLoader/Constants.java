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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Constants {

    static final String BASE_URL = "http://www.dsab-vfs.de";
    static final String GAME_PLAN_URL_POST = "&typ=partienplanPDF&saison=";
    static final String LEAGUE_URL_POST = "&typ=teamrang&saison=";
    static final String PLAYER_URL_POST = "&typ=einzelrang&saison=";
    static final String REGION_NR_EXTRACT_POST_URL = "&typ=saisons";
    static final String REGION_NR_EXTRACT_PRE_URL = "/VFSProject/WebObjects/VFSProject.woa/wa/rangListen?ligagruppe=";
    static final String REGION_URL_POST = "&typ=ligagruppen";
    static final String REGION_URL_PRE = "/VFSProject/WebObjects/VFSProject.woa/wa/rangListen?bundesland=";
    static final String RESULT_LIST_URL_POST = "&typ=ergebnislistePDF&saison=";
    static final String LEAGUE_DATA_URL_PRE = "/VFSProject/WebObjects/VFSProject.woa/wa/rangListen?liga=";
    static final String SEASON_OVERVIEW_URL = "/VFSProject/WebObjects/VFSProject.woa/wa/rangListen?typ=saisons&ligagruppe=";
    static final String SEASON_URL = "/VFSProject/WebObjects/VFSProject.woa/wa/rangListen?typ=saisonLigen&saison=";

    static final Map<String, String> URL_STATE_NAMES_MAPPING;

    static {
        URL_STATE_NAMES_MAPPING = new HashMap<>();
        URL_STATE_NAMES_MAPPING.put("Baden-Württemberg", "baden");
        URL_STATE_NAMES_MAPPING.put("Bayern", "bayern");
        URL_STATE_NAMES_MAPPING.put("Berlin", "berlin");
        URL_STATE_NAMES_MAPPING.put("Brandenburg", "branden");
        URL_STATE_NAMES_MAPPING.put("Bremen", "bremen");
        URL_STATE_NAMES_MAPPING.put("Hamburg", "hamburg");
        URL_STATE_NAMES_MAPPING.put("Hessen", "hessen");
        URL_STATE_NAMES_MAPPING.put("Mecklenburg-Vorpommern", "mecklen");
        URL_STATE_NAMES_MAPPING.put("Niedersachsen", "niedersachsen");
        URL_STATE_NAMES_MAPPING.put("Nordrhein-Westfalen", "nrw");
        URL_STATE_NAMES_MAPPING.put("Rheinland-Pfalz", "pfalz");
        URL_STATE_NAMES_MAPPING.put("Saarland", "saar");
        URL_STATE_NAMES_MAPPING.put("Sachsen", "sachsen");
        URL_STATE_NAMES_MAPPING.put("Sachsen-Anhalt", "anhalt");
        URL_STATE_NAMES_MAPPING.put("Schleswig-Holstein", "schlesswig");
        URL_STATE_NAMES_MAPPING.put("Thüringen", "thuer");
    }

    public static Set<String> getStates() {
        return URL_STATE_NAMES_MAPPING.keySet();
    }


}
