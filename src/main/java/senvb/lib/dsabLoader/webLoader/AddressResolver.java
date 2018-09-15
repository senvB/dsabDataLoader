package senvb.lib.dsabLoader.webLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressResolver {

    private static final Logger LOG = LoggerFactory.getLogger(AddressResolver.class);

    public static Map<String, TeamAddressData> analyzeAddressLine(String currentLineIn, Map<String, Integer> mappingTeamID, String[] prefixes) {
        String currentLine = currentLineIn.replace("(kein Essen)", "").replace("(Kein Essen)", "");
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
}
