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
        assertEquals("Spain", summary.homeTeam());
        assertEquals("Brazil", summary.awayTeam());
        assertEquals(2, summary.homeScore());
        assertEquals(1, summary.awayScore());
        assertEquals(matchStartTime, summary.startedAt());
    }

    @Test
    void testDisplayStringIsFormattedCorrectly() {
        String expected = "Spain 2 - 1 Brazil";
        assertEquals(expected, summary.displayString());
    }

    @Test
    void testTotalScoreComputation() {
        int totalScore = summary.homeScore() + summary.awayScore();
        assertEquals(3, totalScore);
    }

    @Test
    void testMatchSummariesWithSameDataAreEqual() {
        MatchSummary other = new MatchSummary("Spain", "Brazil", 2, 1, matchStartTime);
        assertEquals(summary, other);
        assertEquals(summary.hashCode(), other.hashCode());
    }

    @Test
    void testMatchSummariesWithDifferentDataAreNotEqual() {
        MatchSummary different = new MatchSummary("Spain", "Brazil", 3, 1, matchStartTime);
        assertNotEquals(summary, different);
    }
}

