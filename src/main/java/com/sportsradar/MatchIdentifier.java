package com.sportsradar;

import java.util.*;

/**
 * A simple, immutable key for a live match.
 * Normalizes team names by trimming, collapsing whitespace, and lowercasing.
 */
record MatchIdentifier(String homeTeam, String awayTeam) {

    /**
     * Constructs a MatchIdentifier with normalized team names.
     *
     * @param homeTeam Name of the home team
     * @param awayTeam Name of the away team
     * @throws NullPointerException if either team name is null
     * @throws IllegalArgumentException if empty, or if both teams are the same after normalization
     */
    MatchIdentifier {
        // Normalize: trim ends, collapse internal whitespace to single spaces, lowercase
        homeTeam = normalize(homeTeam);
        awayTeam = normalize(awayTeam);
        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Home and away teams must be different");
        }
    }

    /**
     * Returns a string representation of the match identifier.
     * Format: "homeTeam vs awayTeam"
     *
     * @return String representation of the match identifier
     *
     */
    private static String normalize(String name) {
        Objects.requireNonNull(name, "Team name cannot be null");
        // Trim leading/trailing, replace consecutive whitespace with single space
        String cleaned = name.trim().replaceAll("\\s+", " ");
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be empty or whitespace");
        }
        return cleaned.toLowerCase(Locale.ROOT);
    }
}
