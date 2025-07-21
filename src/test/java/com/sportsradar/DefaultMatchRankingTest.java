package com.sportsradar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultMatchRankingTest {
    private Comparator<Match> ranking;

    @BeforeEach
    public void setup() {
        ranking = new DefaultMatchRanking();
    }

    /**
     * utility to create a match with a fixed start time
     * This is useful for testing purposes to ensure
     */
    private Match createMatch(String homeTeam, String awayTeam, int homeScore, int awayScore, long startTime) {
        Match match = new Match(homeTeam, awayTeam);
        match.updateScore(homeScore, awayScore);

        // Set private field startTime to a fixed value for testing
        try {
            java.lang.reflect.Field startTimeField = Match.class.getDeclaredField("startTime");
            startTimeField.setAccessible(true);
            startTimeField.setLong(match, startTime); // Fixed start time for consistency
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set start time for match", e);
        }
        return match;
    }

    @Test
    void whenDifferentScores_thenHigherScoreWins() {
        Match match1 = createMatch("Team A", "Team B", 2, 1, Instant.now().toEpochMilli());
        Match match2 = createMatch("Team C", "Team D", 3, 0, Instant.now().toEpochMilli());

        assertTrue(ranking.compare(match1, match2) < 0, "Match with higher score should come first");
    }

    @Test
    void whenSameScores_thenEarlierStartTimeWins() {
        long fixedStartTime = Instant.now().toEpochMilli();
        Match match1 = createMatch("Team A", "Team B", 1, 1, fixedStartTime);
        Match match2 = createMatch("Team C", "Team D", 1, 1, fixedStartTime - 1000); // Earlier start time

        assertTrue(ranking.compare(match1, match2) > 0, "Match with earlier start time should come first" );
        assertTrue(ranking.compare(match2, match1) < 0, "Match with later start time should come after" );
    }

    @Test
    void whenSameTotalScoresAndStartTime_thenAlphabeticalOrder() {
        long fixedStartTime = Instant.now().toEpochMilli();
        Match match1 = createMatch("Alpha", "Beta", 1, 1, fixedStartTime);
        Match match2 = createMatch("Alpha", "Beta", 1, 1, fixedStartTime);
        Match match3 = createMatch("Beta", "Alpha", 1, 1, fixedStartTime);

        // Matches with the same teams and scores should be equal
        assertEquals(0, ranking.compare(match1, match2), "Matches with same teams and scores should be equal");

        // Matches with the same teams but different order should be compared alphabetically
        assertTrue(ranking.compare(match1, match3) < 0, "Match with 'Beta' as home team should come after 'Alpha'");
        assertTrue(ranking.compare(match3, match1) > 0, "Match with 'Alpha' as home team should come before 'Beta'");
    }
}
