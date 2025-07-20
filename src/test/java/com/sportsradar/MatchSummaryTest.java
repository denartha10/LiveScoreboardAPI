package com.sportsradar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class MatchSummaryTest {

    private MatchSummary summary;
    private long matchStartTime;

    @BeforeEach
    void setUp() {
        matchStartTime = Instant.now().toEpochMilli();
        summary = new MatchSummary("Spain", "Brazil", 2, 1, matchStartTime);
    }

    @Test
    void testFieldsAreSetCorrectly() {
        assertEquals("Spain", summary.homeTeam(), "Home team should be Spain");
        assertEquals("Brazil", summary.awayTeam(), "Away team should be Brazil");
        assertEquals(2, summary.homeScore(), "Home score should be 2");
        assertEquals(1, summary.awayScore(), "Home score should be 2 and away score should be 1");
        assertEquals(matchStartTime, summary.startedAt(), "Match start time should match the provided value");
    }

    @Test
    void testDisplayStringIsFormattedCorrectly() {
        String expected = "Spain 2 - Brazil 1";
        assertEquals(expected, summary.toString(), "Display string should be formatted as 'HomeTeam HomeScore - AwayScore AwayTeam'");
    }

    @Test
    void testTotalScoreComputation() {
        int totalScore = summary.homeScore() + summary.awayScore();
        assertEquals(3, totalScore, "Total score should be the sum of home and away scores");
    }

    @Test
    void testMatchSummariesWithSameDataAreEqual() {
        MatchSummary other = new MatchSummary("Spain", "Brazil", 2, 1, matchStartTime);
        assertEquals(summary, other, "Match summaries with same data should be equal");
        assertEquals(summary.hashCode(), other.hashCode() , "Match summaries with same data should be equal");
    }

    @Test
    void testMatchSummariesWithDifferentDataAreNotEqual() {
        MatchSummary different = new MatchSummary("Spain", "Brazil", 3, 1, matchStartTime);
        assertNotEquals(summary, different, "Match summaries with different scores should not be equal");
    }
}

