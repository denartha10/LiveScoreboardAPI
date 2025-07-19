package com.sportsradar;

/**
 * Represents a football match between two distinct teams.
 *
 * <p>This class is responsible for:
 * <ul>
 *   <li>Storing the names of the home and away teams</li>
 *   <li>Tracking each team's score</li>
 *   <li>Ensuring valid team names (non-null, non-empty, and distinct)</li>
 *   <li>Providing the ability to update and retrieve scores</li>
 *   <li>Calculating the total score of the match</li>
 *   <li>Adding a compare method to compare matches based on team names (for now)</li>
 * </ul>
 *
 * <p>All interactions with this class are expected to enforce input validation
 * and maintain match integrity.
 */
class Match {
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

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
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

