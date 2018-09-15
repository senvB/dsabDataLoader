package senvb.lib.dsabLoader.webLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import senvb.lib.dsabLoader.MatchData;
import senvb.lib.dsabLoader.webLoader.DataLoaderException.LoadingStage;

public class MatchResolver {

    private static final Logger LOG = LoggerFactory.getLogger(MatchResolver.class);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm dd.MM.yy", Locale.getDefault());
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d\\d:\\d\\d\\s\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d");

    public static MatchData processMatchData(String line, int currentRound, Map<String, Integer> mappingTeamID) throws DataLoaderException {
        String[] teamsOrdered = resolveTeamOrder(line, mappingTeamID.keySet());
        int[] numbers = resolveRoundAndGameNumber(line, currentRound, teamsOrdered[0]);
        return new MatchData(numbers[0], numbers[1], mappingTeamID.get(teamsOrdered[0]), mappingTeamID.get(teamsOrdered[1]), resolveMatchDate(line));
    }

    private static String[] resolveTeamOrder(String line, Set<String> teamNames) throws DataLoaderException {
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

    public static boolean isComplete(String match) {
        Matcher matcher = DATE_PATTERN.matcher(match);
        return matcher.find();
    }
}
