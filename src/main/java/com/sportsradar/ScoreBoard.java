package com.sportsradar;

import java.util.*;

/**
 * Live Football World Cup Scoreboard that tracks ongoing matches and their scores.
 * Matches are ordered by total goals (descending) and most recently started first on tie.
 * Teams cannot play more than one match simultaneously.
 */
public class ScoreBoard {
    private final NavigableSet<Match> matches;
    private final Map<MatchIdentifier, Match> lookup = new HashMap<>();
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
     * Starts a new match with initial score 0-0.
     *
     * @param home  Home team name
     * @param away  Away team name
     * @throws IllegalArgumentException if either team name is null, empty, or if both teams are the same
     * @throws IllegalStateException if either team is already playing in another match
     *
     * @return MatchIdentifier for the started match
     */
    public MatchIdentifier startMatch(String home, String away) {
        MatchIdentifier id = new MatchIdentifier(home, away);

        if (activeTeams.contains(id.homeTeam()) || activeTeams.contains(id.awayTeam())) {
            throw new IllegalStateException("One of the teams is already playing: " + (activeTeams.contains(id.homeTeam()) ? id.homeTeam() : id.awayTeam()));
        }

        Match match = new Match(id.homeTeam(), id.awayTeam());
        lookup.put(id, match);
        matches.add(match);
        activeTeams.add(id.homeTeam());
        activeTeams.add(id.awayTeam());
        return id;
    }

    /**
     * helper to ensure match is found
     *
     * @param id MatchIdentifier of the match to look up
     * @return Match object if found
     * @throws NoSuchElementException if no match exists for the given identifier
     */
    private Match requireLiveMatch(MatchIdentifier id) {
        Match match = lookup.get(id);
        if (match == null) {
            throw new NoSuchElementException("No match found for id: " + id);
        }
        return match;
    }

    /**
     * Updates the score of an ongoing match.
     *
     * @param id         MatchIdentifier of the match to update
     * @param homeScore  New home team score
     * @param awayScore  New away team score
     * @throws IllegalStateException if no such match exists
     */
    public void updateScore(MatchIdentifier id, int homeScore, int awayScore) {
        Match match = requireLiveMatch(id);
        matches.remove(match);
        match.updateScore(homeScore, awayScore);
        matches.add(match);
    }

    /**
     * Finishes a match and removes it from the scoreboard.
     *
     * @param id    MatchIdentifier of the match to finish
     * @throws IllegalStateException if no such match exists
     */
    public MatchSummary finishMatch(MatchIdentifier id) {
        Match match = requireLiveMatch(id);
        matches.remove(match);
        lookup.remove(id);
        activeTeams.remove(id.homeTeam());
        activeTeams.remove(id.awayTeam());
        return match.toMatchSummary();
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
