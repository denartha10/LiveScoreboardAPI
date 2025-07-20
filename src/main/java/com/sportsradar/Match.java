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
        if (homeTeam == null || awayTeam == null) {
            throw new IllegalArgumentException("Teams cannot be null or empty");
        }

        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Teams must be distinct");
        }

        homeTeam = homeTeam.trim().replaceAll("\\s+", "");
        awayTeam = awayTeam.trim().replaceAll("\\s+", "");

        if (homeTeam.isEmpty() || awayTeam.isEmpty()) {
            throw new IllegalArgumentException("Teams cannot be empty after trimming");
        }

        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Teams must be distinct");
        }

        this.homeTeam = homeTeam.toLowerCase();
        this.awayTeam = awayTeam.toLowerCase();
        this.homeScore = 0;
        this.awayScore = 0;
        this.startTime = System.currentTimeMillis(); // Capture the start time of the match
    }

    String getHomeTeam() {
        return homeTeam;
    }

    String getAwayTeam() {
        return awayTeam;
    }

    int getHomeScore() {
        return homeScore;
    }

    int getAwayScore() {
        return awayScore;
    }

    int getTotalScore() {
        return homeScore + awayScore;
    }

    // method which builds immutable MatchSummary record
    MatchSummary toMatchSummary() {
        return new MatchSummary(homeTeam, awayTeam, homeScore, awayScore, startTime);
    }

    /**
     * Returns the start time of the match.
     *
     * @return the start time in milliseconds since epoch
     */
    long getStartTime() {
        return startTime;
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

    /**
     * Compares this match with another based on team names.
     *
     * @return a negative integer, zero, or a positive integer as this match is less than,
     *         equal to, or greater than the specified match
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Match other)) return false;
        return homeTeam.equals(other.homeTeam) && awayTeam.equals(other.awayTeam);
    }

    /**
     * Returns a hash code value for this match.
     *
     * @return a hash code value based on the home and away team names
     */
    @Override
    public int hashCode() {
        return 31 * homeTeam.hashCode() + awayTeam.hashCode();
    }
}

