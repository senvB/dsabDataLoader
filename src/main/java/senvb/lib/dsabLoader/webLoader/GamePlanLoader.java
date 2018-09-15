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

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import senvb.lib.dsabLoader.LeagueMetaData;
import senvb.lib.dsabLoader.MatchData;
import senvb.lib.dsabLoader.webLoader.DataLoaderException.LoadingStage;

class GamePlanLoader {

    private static final Logger LOG = LoggerFactory.getLogger(GamePlanLoader.class);

    private GamePlanLoader() {
        throw new UnsupportedOperationException();
    }

    static GameAndTeamData loadGamePlan(LeagueMetaData lmd, Map<String, Integer> mappingTeamID) throws DataLoaderException {
        String urlString = "";
        try {
            URL url = resolveGamePlanUrl(lmd);
            urlString = url.toString();
            String pdfData = LoaderUtil.loadPdfFromSource(url);
            String[] prefixes = findPrefixOfStreet(url);
            return resolveGamePlanData(pdfData, mappingTeamID, prefixes);
        } catch (IOException e) {
            throw new DataLoaderException(LoadingStage.GAME_PLAN, DataLoaderException.ExceptionType.IO, e, urlString);
        }
    }

    private static URL resolveGamePlanUrl(LeagueMetaData lmd) throws MalformedURLException {
        String url = Constants.BASE_URL + Constants.LEAGUE_DATA_URL_PRE + lmd
                .getLeagueID() + Constants.GAME_PLAN_URL_POST + lmd.getSeasonID();
        return new URL(url);
    }

    private static String[] findPrefixOfStreet(URL url) throws IOException {
        Rectangle rect = new Rectangle(280, 500, 350, 700);
        RenderFilter[] filter = {new RegionTextRenderFilter(rect)};
        PdfReader reader = new PdfReader(url);
        FilteredTextRenderListener strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter);
        String extract = PdfTextExtractor.getTextFromPage(reader, 1, strategy);
        return extract.split("\n");
    }

    private static GameAndTeamData resolveGamePlanData(String pdfText, Map<String, Integer> mappingTeamID, String[] prefixes) throws DataLoaderException {
        String[] lines = pdfText.split("\n");
        ParseMode mode = ParseMode.ADDRESS;

        List<String> addressLines = new ArrayList<>();
        List<String> matchLines = new ArrayList<>();
        String matchLine = "";
        for (String currentLine : lines) {
            if (currentLine.startsWith("Vorrunde")) {
                mode = ParseMode.MATCHES;
                continue;
            }
            if (mode == ParseMode.ADDRESS) {
                if (LoaderUtil.lineStartsWithNumberValue(currentLine, addressLines.size() + 1)) {
                    addressLines.add(currentLine);
                } else if (!addressLines.isEmpty()) {
                    String extendedLine = addressLines.remove(addressLines.size() - 1) + " " + currentLine;
                    addressLines.add(extendedLine);
                }
            } else {
                if (!currentLine.startsWith("Runde") && !currentLine.startsWith("Seite") && !currentLine.startsWith("R\u00FCckrunde")) {
                    matchLine = matchLine + currentLine;
                    if (LoaderUtil.lineStartsWithANumber(matchLine) && MatchResolver.isComplete(matchLine)) { //ParseMode.MATCHES
                        matchLines.add(matchLine);
                        matchLine = "";
                    }
                }
            }
        }
        //parse team data
        Map<String, TeamAddressData> teamData = new HashMap<>();
        for (String team : addressLines) {
            Map<String, TeamAddressData> nextTeamData = AddressResolver.analyzeAddressLine(team, mappingTeamID, prefixes);
            teamData.putAll(nextTeamData);
        }

        //parse matches
        int currentRound = 1;
        Map<Integer, MatchData> matches = new HashMap<>();

        for (String match : matchLines) {
            MatchData m = MatchResolver.processMatchData(match, currentRound, mappingTeamID);
            matches.put(m.getMatchID(), m);
            currentRound = m.getRound();
        }
        return new GameAndTeamData(teamData, matches);
    }

    private enum ParseMode {
        ADDRESS,
        MATCHES
    }
}
