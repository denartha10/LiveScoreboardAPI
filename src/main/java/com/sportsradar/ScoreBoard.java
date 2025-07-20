package com.sportsradar;

// feat: implemented final public API for ScoreBoard and MatchSummary, still need to create some simulation tests for the ScoreBoard class

import java.util.*;

/**
 * Live Football World Cup Scoreboard that tracks ongoing matches and their scores.
 * Matches are ordered by total goals (descending) and most recently started first on tie.
 * Teams cannot play more than one match simultaneously.
 */
public class ScoreBoard {
    private final NavigableSet<Match> matches;
    private final Map<String, Match> lookup = new HashMap<>();
    private final Set<String> activeTeams = new HashSet<>();


    /**
     * Constructs a ScoreBoard with the default match-ranking comparator.
     * The default ranking is by total goals (descending), then by start time (ascending),
     * and finally by team names in lexicographical order.
     */
    public ScoreBoard() {
        Comparator<Match> defaultRanking = new DefaultMatchRanking();
        this.matches = new TreeSet<>(defaultRanking);
    }

    /**
     * Generates a unique key for a fixture based on home and away team names.
     *
     * @param home  Home team name
     * @param away  Away team name
     * @return      Unique string key
     */
    String key(String home, String away) {
        return home + "#" + away;
    }

    /**
     * Starts a new match with initial score 0-0.
     *
     * @param home  Home team name
     * @param away  Away team name
     * @throws IllegalArgumentException if either team is already playing or the match exists
     */
    public void startMatch(String home, String away) {
        if (home == null || away == null) {
            throw new IllegalArgumentException("Team names cannot be null");
        }

        // Normalise team names for comparison
        home = home.trim().replaceAll("\\s+", " ");
        away = away.trim().replaceAll("\\s+", " ");
        if (home.isEmpty() || away.isEmpty()) {
            throw new IllegalArgumentException("Team names cannot be empty");
        }

        String k = key(home, away);

        if (lookup.containsKey(k)) { throw new IllegalStateException("Match already in progress"); }

        if (activeTeams.contains(home) || activeTeams.contains(away)) {
            throw new IllegalStateException( "One of the teams is already playing: " + (activeTeams.contains(home) ? home : away) );
        }

        Match m = new Match(home, away);

        lookup.put(k, m);
        matches.add(m);
        activeTeams.add(home);
        activeTeams.add(away);
    }

    /**
     * Updates the score of an ongoing match.
     *
     * @param home       Home team name
     * @param away       Away team name
     * @param homeScore  New home team score
     * @param awayScore  New away team score
     * @throws IllegalStateException if no such match exists
     */
    public void updateScore(String home, String away, int homeScore, int awayScore) {
        // Normalise team names for comparison
        home = home.trim().replaceAll("\\s+", " ");
        away = away.trim().replaceAll("\\s+", " ");

        Match m = lookup.get(key(home, away));

        if (m == null) {
            throw new IllegalStateException("No such match");
        }
        matches.remove(m);
        m.updateScore(homeScore, awayScore);
        matches.add(m);
    }

    /**
     * Finishes a match and removes it from the scoreboard.
     *
     * @param home  Home team name
     * @param away  Away team name
     * @throws IllegalStateException if no such match exists
     */
    public void finishMatch(String home, String away) {
        // Normalise team names for comparison
        home = home.trim().replaceAll("\\s+", " ");
        away = away.trim().replaceAll("\\s+", " ");

        String k = key(home, away);
        Match m = lookup.remove(k);
        if (m == null) {
            throw new IllegalStateException("No such match");
        }
        matches.remove(m);
        activeTeams.remove(home);
        activeTeams.remove(away);
    }

    /**
     * Returns a summary of all matches in the scoreboard.
     * This is a list of MatchSummary records ordered by Comparator defined in the constructor.
     *
     * @return List of MatchSummary records
     */
    public List<MatchSummary> getSummary() {
        List<MatchSummary> summary = new ArrayList<>();
        for (Match match : matches) {
            summary.add(match.toMatchSummary());
        }
        return summary;
    }
}
