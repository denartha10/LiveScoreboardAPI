package com.sportsradar;

/**
 * Represents a football match between two distinct teams.
 */
class Match {
    /**
     * The name of the home team, normalized to lowercase and trimmed of whitespace.
     * This field is immutable after construction.
     */
    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;
    private final long startTime;


    /**
     * Constructs a Match with specified home and away teams.
     *
     * @param homeTeam the name of the home team
     * @param awayTeam the name of the away team
     * @throws IllegalArgumentException if either team name is null, empty, or if both teams are the same
     */
    Match(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null) { throw new IllegalArgumentException("Teams cannot be null or empty"); }
        if (homeTeam.equals(awayTeam)) { throw new IllegalArgumentException("Teams must be distinct"); }

        homeTeam = homeTeam.trim().replaceAll("\\s+", "");
        awayTeam = awayTeam.trim().replaceAll("\\s+", "");

        if (homeTeam.isEmpty() || awayTeam.isEmpty()) { throw new IllegalArgumentException("Teams cannot be empty after trimming"); }
        if (homeTeam.equals(awayTeam)) { throw new IllegalArgumentException("Teams must be distinct"); }

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;
        this.startTime = System.currentTimeMillis(); // Capture the start time of the match
    }

    String getHomeTeam() { return homeTeam; }
    String getAwayTeam() { return awayTeam; }
    int getHomeScore() { return homeScore; }
    int getAwayScore() { return awayScore; }
    int getTotalScore() { return homeScore + awayScore; }
    long getStartTime() { return startTime; }

    MatchSummary toMatchSummary() {
        return new MatchSummary(homeTeam, awayTeam, homeScore, awayScore, startTime);
    }

    /**
     * Updates the scores for both teams.
     *
     * @param homeScore the new score for the home team
     * @param awayScore the new score for the away team
     */
    void updateScore(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative");
        }
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }
}

