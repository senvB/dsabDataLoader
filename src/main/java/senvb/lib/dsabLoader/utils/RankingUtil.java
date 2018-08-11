package senvb.lib.dsabLoader.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import senvb.lib.dsabLoader.Match;
import senvb.lib.dsabLoader.Matches;
import senvb.lib.dsabLoader.Team;
import senvb.lib.dsabLoader.TeamResult;
import senvb.lib.dsabLoader.Teams;

public class RankingUtil {

    private static final IntermediateTeamResultComparator INTERMEDIATES_COMPARATOR = new IntermediateTeamResultComparator();

    public static Teams calculateHomeRanking(Teams originalTeams, Matches matches) {
        Map<Integer, IntermediateTeamResult> intermediates = prepareIntermediateResults(originalTeams);
        for (Match m : matches.getMatches()) {
            if (m.isPlayed() && m.isRealMatch()) {
                IntermediateTeamResult result = intermediates.get(m.getHome());
                updateForHome(m, result);
            }
        }
        List<Team> teams = resolveTeams(originalTeams, intermediates);
        return new Teams(teams);
    }

    public static Teams calculateAwayRanking(Teams originalTeams, Matches matches) {
        Map<Integer, IntermediateTeamResult> intermediates = prepareIntermediateResults(originalTeams);
        for (Match m : matches.getMatches()) {
            if (m.isPlayed() && m.isRealMatch()) {
                IntermediateTeamResult result = intermediates.get(m.getAway());
                updateForAway(m, result);
            }
        }
        List<Team> teams = resolveTeams(originalTeams, intermediates);
        return new Teams(teams);
    }

    public static Teams calculateFirstHalfRanking(Teams originalTeams, Matches matches) {
        Map<Integer, IntermediateTeamResult> intermediates = prepareIntermediateResults(originalTeams);
        int numberMatchesPerHalf = matches.getMatches().size() / 2;
        for (Match m : matches.getMatches()) {
            if (m.isPlayed() && m.isRealMatch() && m.getMatchID() <= numberMatchesPerHalf) {
                IntermediateTeamResult resultHome = intermediates.get(m.getHome());
                updateForHome(m, resultHome);
                IntermediateTeamResult resultAway = intermediates.get(m.getAway());
                updateForAway(m, resultAway);
            }
        }
        List<Team> teams = resolveTeams(originalTeams, intermediates);
        return new Teams(teams);
    }

    public static Teams calculateSecondHalfRanking(Teams originalTeams, Matches matches) {
        Map<Integer, IntermediateTeamResult> intermediates = prepareIntermediateResults(originalTeams);
        int numberMatchesPerHalf = matches.getMatches().size() / 2;
        for (Match m : matches.getMatches()) {
            if (m.isPlayed() && m.isRealMatch() && m.getMatchID() > numberMatchesPerHalf) {
                IntermediateTeamResult resultHome = intermediates.get(m.getHome());
                updateForHome(m, resultHome);
                IntermediateTeamResult resultAway = intermediates.get(m.getAway());
                updateForAway(m, resultAway);
            }
        }
        List<Team> teams = resolveTeams(originalTeams, intermediates);
        return new Teams(teams);
    }

    private static void updateForHome(Match m, IntermediateTeamResult result) {
        result.points += m.getHomePoints();
        result.matchesPlayed++;
        result.gamesPos += m.getHomeMatches();
        result.gamesNeg += m.getAwayMatches();
        result.setsPos += m.getHomeSets();
        result.setsNeg = m.getAwaySets();
    }

    private static void updateForAway(Match m, IntermediateTeamResult result) {
        result.points += m.getAwayPoints();
        result.matchesPlayed++;
        result.gamesPos += m.getAwayMatches();
        result.gamesNeg += m.getHomeMatches();
        result.setsPos += m.getAwaySets();
        result.setsNeg = m.getHomeSets();
    }

    private static List<Team> resolveTeams(Teams originalTeams, Map<Integer, IntermediateTeamResult> intermediates) {
        List<IntermediateTeamResult> sortedResults = new ArrayList<>(intermediates.values());
        sortedResults.sort(INTERMEDIATES_COMPARATOR);
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < sortedResults.size(); i++) {
            IntermediateTeamResult itr = sortedResults.get(i);
            itr.rank = i+1;
            teams.add(new Team(originalTeams.getTeamByNumber(itr.teamID).get().getTeamData(), createTeamResult(itr)));
        }
        return teams;
    }

    private static TeamResult createTeamResult(IntermediateTeamResult itr) {
        return new TeamResult(itr.matchesPlayed, itr.gamesPos, itr.gamesNeg, itr.setsPos, itr.setsNeg, itr.points, itr.rank);
    }

    private static Map<Integer, IntermediateTeamResult> prepareIntermediateResults(Teams originalTeams) {
        Map<Integer, IntermediateTeamResult> intermediates = new HashMap<>();
        originalTeams.getTeams().forEach(t -> intermediates.put(t.getTeamID(), new IntermediateTeamResult(t.getTeamID())));
        return intermediates;
    }

    private static class IntermediateTeamResultComparator implements Comparator<IntermediateTeamResult> {

        @Override
        public int compare(IntermediateTeamResult o1, IntermediateTeamResult o2) {
            if (o1.points > o2.points) {
                return -1;
            } else if (o1.points == o2.points) {
                if (o1.gamesPos > o2.gamesPos) {
                    return -1;
                } else if (o1.gamesPos == o2.gamesPos) {
                    if (o1.gamesNeg < o2.gamesNeg) {
                        return -1;
                    } else if (o1.gamesNeg == o2.gamesNeg) {
                        if (o1.setsPos > o2.setsPos) {
                            return -1;
                        } else if (o1.setsPos == o2.setsPos) {
                            if (o1.setsNeg < o2.setsNeg) {
                                return -1;
                            } else if (o1.setsNeg == o2.setsNeg) {
                                return 0;
                            }
                        }
                    }
                }
            }
            return 1;
        }
    }

    private static class IntermediateTeamResult {
        final int teamID;
        /**
         * games lost
         */
        int gamesNeg;
        /**
         * games won
         */
        int gamesPos;
        /**
         * matches play in total
         */
        int matchesPlayed;
        /**
         * points won during the season
         */
        int points;
        /**
         * current rank in the league
         */
        int rank;
        /**
         * sets lost
         */
        int setsNeg;
        /**
         * sets won
         */
        int setsPos;

        IntermediateTeamResult(int teamID) {
            this.teamID = teamID;
        }
    }
}
