package com.sportsradar;

public record MatchSummary(String homeTeam, String awayTeam, int homeScore, int awayScore, long startedAt) {
    // function for title case used on home and away team names
    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String[] words = input.split(" ");
        StringBuilder titleCase = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                titleCase.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return titleCase.toString().trim();
    }

    /**
     * Constructs a MatchSummary string representation. using java toString() method
     */
    @Override
    public String toString() {
        try {
            return String.format("%s %d - %d %s", toTitleCase(homeTeam), homeScore, awayScore, toTitleCase(awayTeam));
        } catch (Exception e) {
            return "Error formatting match summary";
        }
    }
}