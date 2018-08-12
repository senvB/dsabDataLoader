/**
 * The DSAB data loader library allows to parse information for DSAB dart leagues.
 * Copyright (C) 2017-2018  Sven Baselau
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package senvb.lib.dsabLoader.webLoader;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import senvb.lib.dsabLoader.LeagueMetaData;
import senvb.lib.dsabLoader.MatchResult;
import senvb.lib.dsabLoader.webLoader.DataLoaderException.LoadingStage;

class CurrentResultListLoader {
    private CurrentResultListLoader() {
        throw new UnsupportedOperationException();
    }

    static Map<Integer, MatchResult> loadCurrentResultList(LeagueMetaData lmd, Map<String, Integer> mappingTeamID) throws DataLoaderException {
        Map<Integer, MatchResult> results = new HashMap<>();
        String urlString = "";
        try {
            URL url = resolveCurrentResultListUrl(lmd);
            urlString = url.toString();
            String pdfData = LoaderUtil.loadPdfFromSource(url);
            resolveResults(pdfData, mappingTeamID, results);
            return results;
        } catch (IOException e) {
            throw new DataLoaderException(LoadingStage.CURRENT_RESULTS, DataLoaderException.ExceptionType.IO, e, urlString);
        }
    }

    private static void resolveResults(String pdfText, Map<String, Integer> mappingTeamID, Map<Integer, MatchResult> results) throws DataLoaderException {
        String[] lines = pdfText.split("\n");
        //counter to keep track of the cexpected match number (also used for sanity checks)
        int matchCounter = 1;
        //the final data line (can be contructed out of multiple lines read from the PDF
        String dataLine = "";
        int numberMatchesPerRound = (int) Math.floor(((double) mappingTeamID.size()) / 2.0d);
        for (String currentLine : lines) {
            if (currentLineIsValid(currentLine)) {
                if (dataLine.isEmpty() && !LoaderUtil.lineStartsWithANumber
                        (currentLine)) {
                    // do not handle if there is no data parsed so far and
                    // the current line does not start with a (match) number
                    continue;
                }
                if (!dataLine.isEmpty()) {
                    //extend dataline as it consists of multiple input lines
                    dataLine = dataLine + " ";
                }
                dataLine = dataLine + currentLine;

                int colonCount = StringUtils.countMatches(dataLine, ":");
                if (colonCount == 3) {
                    //exactly the colons for point, matches and sets
                    int matchNumber = resolve_matchNumber(matchCounter, numberMatchesPerRound, dataLine);
                    resolveResult(results, dataLine, matchNumber);
                    matchCounter++;
                    dataLine = "";
                }
            }
        }
    }

    private static void resolveResult(Map<Integer, MatchResult> results, String dataLine, int matchNumber) {
        String[] resultStringSplit = dataLine.substring(dataLine.indexOf(":") - 2).split(" ");
        results.put(matchNumber, new MatchResult(Integer.parseInt(resultStringSplit[0]), Integer.parseInt(resultStringSplit[2]), Integer.parseInt(resultStringSplit[3]), Integer.parseInt(resultStringSplit[5]), Integer.parseInt(resultStringSplit[6]), Integer.parseInt(resultStringSplit[8])));
    }

    private static int resolve_matchNumber(int matchCounter, int numberMatchesPerRound, String dataLine) throws DataLoaderException {
        int matchNumber;
        try {
            if (matchCounter % numberMatchesPerRound == 1) {
                matchNumber = Integer.parseInt(dataLine.split(" ")[1].trim());
            } else {
                matchNumber = Integer.parseInt(dataLine.split(" ")[0].trim());
            }
            if (matchCounter != matchNumber) {
                throw new DataLoaderException(LoadingStage.CURRENT_RESULTS, DataLoaderException.ExceptionType.PARSING);
            }
        } catch (NumberFormatException e) {
            throw new DataLoaderException(LoadingStage.CURRENT_RESULTS, DataLoaderException.ExceptionType.PARSING);
        }
        return matchNumber;
    }

    private static boolean currentLineIsValid(String currentLine) {
        //individual checks for keywords indicating that the current line does not contain any useful data
        return  !(currentLine.startsWith("Vorrunde") || currentLine.startsWith("R\u00FCckrunde") ||
                currentLine.startsWith("Runde")
                || currentLine.startsWith("Sportart") || currentLine.startsWith("Sekretaer"));
    }

    private static URL resolveCurrentResultListUrl(LeagueMetaData lmd) throws MalformedURLException {
        String url = Constants.BASE_URL + Constants.LEAGUE_DATA_URL_PRE + lmd
                .getLeagueID() + Constants.RESULT_LIST_URL_POST + lmd.getSeasonID();
        return new URL(url);
    }
}
