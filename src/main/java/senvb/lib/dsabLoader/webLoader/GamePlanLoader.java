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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import senvb.lib.dsabLoader.LeagueMetaData;
import senvb.lib.dsabLoader.MatchData;
import senvb.lib.dsabLoader.webLoader.DataLoaderException.LoadingStage;

class GamePlanLoader {

    private static final Logger LOG = LoggerFactory.getLogger(GamePlanLoader.class);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm dd.MM.yy", Locale.getDefault());
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d\\d:\\d\\d\\s\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d");


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
        StringBuilder url = new StringBuilder();
        url.append(Constants.BASE_URL).append(Constants.LEAGUE_DATA_URL_PRE).append(lmd.getLeagueID())
                .append(Constants.GAME_PLAN_URL_POST).append(lmd.getSeasonID());
        return new URL(url.toString());
    }

    private static String[] findPrefixOfStreet(URL url) throws IOException {
        Rectangle rect = new Rectangle(280, 560, 350, 700);
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
                if (LoaderUtil.lineStartsWithANumber(currentLine)) { //ParseMode.MATCHES
                    matchLines.add(currentLine);
                }
            }
        }
        //parse team data
        Map<String, TeamAddressData> teamData = new HashMap<>();
        for (String team : addressLines) {
            Map<String, TeamAddressData> nextTeamData = analyzeAddressLine(team, mappingTeamID, prefixes);
            teamData.putAll(nextTeamData);
        }

        //parse matches
        int currentRound = 1;
        Map<Integer, MatchData> matches = new HashMap<>();
        for (String match : matchLines) {
            MatchData m = processMatchData(match, currentRound, mappingTeamID);
            matches.put(m.getMatchID(), m);
            currentRound = m.getRound();
        }
        return new GameAndTeamData(teamData, matches);
    }

    private static Map<String, TeamAddressData> analyzeAddressLine(String currentLine, Map<String, Integer> mappingTeamID, String[] prefixes) {
        String currentTeam = null;
        Map<String, TeamAddressData> teamData = new HashMap<>();
        for (String name : mappingTeamID.keySet()) {
            if (removeNumber(currentLine).trim().startsWith(name.substring(0, Math.min(16, name.length())))) {
                currentTeam = name;
                break;
            }
        }
        if (currentTeam != null) {
            TeamAddressData tad = new TeamAddressData();
            tad.setName(currentTeam);
            Matcher matcherPlz = Pattern.compile("[0-9]{5}").matcher(currentLine);
            if (matcherPlz.find()) {
                tad.setPlz(matcherPlz.group(0));
                String[] splittedPLZ = currentLine.split(tad.getPlz());
                String[] splittedDistrictPhone = splitDistrictAndPhone(splittedPLZ[1].trim());
                tad.setDistrict(splittedDistrictPhone[0].trim());
                tad.setPhone(splittedDistrictPhone[1].trim());
                String[] splitted = splitVenueStreet(removeTeam(removeNumber(splittedPLZ[0]), currentTeam), prefixes);
                tad.setVenue(splitted[0].trim());
                tad.setStreet(splitted[1].trim());
            }
            teamData.put(currentTeam, tad);
        } else {
            LOG.warn("Cannot resolve team data from " + currentLine);
        }
        return teamData;
    }

    private static String[] splitVenueStreet(String venueStreet, String[] prefixes) {
        String venue = "";
        String street = "";
        for (String s : prefixes) {
            if (venueStreet.contains(s)) {
                int pos = venueStreet.lastIndexOf(s);
                venue = venueStreet.substring(0, pos);
                street = venueStreet.substring(pos);
                break;
            }
        }
        return new String[]{venue, street};
    }

    private static String removeTeam(String teamVenueStreet, String currentTeam) {
        if (teamVenueStreet.startsWith(currentTeam)) {
            return teamVenueStreet.substring(currentTeam.length()).trim();
        }
        LOG.warn("team and venue string does not start with the team: " +
                teamVenueStreet + "/" + currentTeam);
        return teamVenueStreet;
    }

    private static String removeNumber(String data) {
        String trimmed = data.trim();
        if (Character.isDigit(trimmed.charAt(0))) {
            trimmed = trimmed.substring(1);
        }
        if (Character.isDigit(trimmed.charAt(0))) {
            trimmed = trimmed.substring(1);
        }
        return trimmed.trim();
    }

    private static String[] splitDistrictAndPhone(String districtAndPhone) {
        StringBuilder district = null;
        StringBuilder phone = null;
        for (String s : districtAndPhone.split(" ")) {
            if (Character.isDigit(s.charAt(0))) {
                if (district == null) {
                    district = new StringBuilder();
                }
                if (phone == null) {
                    phone = new StringBuilder(s);
                } else {
                    phone.append(" ").append(s);
                }
            } else if (district == null) {
                district = new StringBuilder(s);
            } else {
                district.append(" ").append(s);
            }
        }
        if (phone == null) {
            phone = new StringBuilder();
        }
        if (district == null) {
            district = new StringBuilder();
        }
        return new String[]{district.toString(), phone.toString()};
    }

    private static MatchData processMatchData(String line, int currentRound, Map<String, Integer> mappingTeamID) throws DataLoaderException {
        String[] teamsOrdered = resolveTeamOder(line, mappingTeamID.keySet());
        int[] numbers = resolveRoundAndGameNumber(line, currentRound, teamsOrdered[0]);
        return new MatchData(numbers[0], numbers[1], mappingTeamID.get(teamsOrdered[0]), mappingTeamID.get(teamsOrdered[1]), resolveMatchDate(line));
    }

    private static String[] resolveTeamOder(String line, Set<String> teamNames) throws DataLoaderException {
        String teamAName = null;
        String teamBName = null;
        int indexA = -1;
        int indexB = -1;
        for (String name : teamNames) {
            String nameTrimmed = name.replace(" ", "");
            String lineTrimmed = line.replace(" ", "");
            if (line.contains(name) || lineTrimmed.contains(nameTrimmed)) {
                if (teamAName == null) {
                    teamAName = name;
                    indexA = line.indexOf(name);
                    if (indexA == -1) {
                        indexA = lineTrimmed.indexOf(nameTrimmed);
                    }
                } else {
                    teamBName = name;
                    indexB = line.indexOf(name);
                    if (indexB == -1) {
                        indexB = lineTrimmed.indexOf(nameTrimmed);
                    }
                }
            }
        }
        if (indexA == -1 || indexB == -1) {
            throw new DataLoaderException(LoadingStage.GAME_PLAN, DataLoaderException.ExceptionType.PARSING);
        } else if (indexA < indexB) {
            return new String[]{teamAName, teamBName};
        } else {
            return new String[]{teamBName, teamAName};
        }
    }

    private static Date resolveMatchDate(String line) throws DataLoaderException {
        Matcher matcher = DATE_PATTERN.matcher(line);
        if (matcher.find()) {
            try {
                return DATE_FORMAT.parse(matcher.group(0));
            } catch (ParseException e) {
                throw new DataLoaderException(LoadingStage.GAME_PLAN, DataLoaderException.ExceptionType.PARSING);
            }
        }
        throw new DataLoaderException(LoadingStage.GAME_PLAN, DataLoaderException.ExceptionType.PARSING);
    }

    private static int[] resolveRoundAndGameNumber(String line, int currentRound, String teamHome) {
        int[] numbers = new int[]{currentRound, 0};
        int startOfTeams = line.indexOf(teamHome);
        if (startOfTeams == -1) {
            startOfTeams = line.indexOf(teamHome.substring(0, 3).replace(" ", ""));
        }
        String[] roundGameSplitted = line.substring(0, startOfTeams).split(" ");
        if (roundGameSplitted.length == 1) {
            numbers[1] = Integer.parseInt(roundGameSplitted[0].trim());
        } else if (roundGameSplitted.length == 2) {
            int round = Integer.parseInt(roundGameSplitted[0].trim());
            int gameNumber = Integer.parseInt(roundGameSplitted[1].trim());
            numbers[0] = round;
            numbers[1] = gameNumber;
        }
        return numbers;
    }

    private enum ParseMode {
        ADDRESS,
        MATCHES
    }
}
