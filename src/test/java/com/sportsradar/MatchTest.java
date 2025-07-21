package com.sportsradar;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Match} class.
 * These tests validate core behaviors of a football match, such as:
 * - Initial score setup
 * - Valid team names
 * - Uniqueness of participating teams
 * - Score tracking and total score calculation
 */
public class MatchTest {

    /**
     * Verifies that a new match starts with both teams having a score of zero.
     */
    @Test
    public void testMatchStartsAtZeroZero() {
        Match match = new Match("Germany", "France");
        assertEquals(0, match.getHomeScore(), "Home team score should start at 0");
        assertEquals(0, match.getAwayScore(), "Away team score should start at 0");
    }

    /**
     * Ensures that match creation fails if either team name is null or empty.
     * Expects an {@link IllegalArgumentException}.
     */
    @Test
    public void testMatchTeamsNonNullAndNonEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Match(null, "France"), "Home team name cannot be null");
        assertThrows(IllegalArgumentException.class, () -> new Match("Germany", null), "Away team name cannot be null");
        assertThrows(IllegalArgumentException.class, () -> new Match("", "France"), "Home team name cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> new Match("Germany", ""), "Away team name cannot be empty");
    }

    /**
     * Ensures that a match cannot be created between two identical teams.
     * Expects an {@link IllegalArgumentException}.
     */
    @Test
    public void testTeamsAreDistinct() {
        assertThrows(IllegalArgumentException.class, () -> new Match("Germany", "Germany"), "Home and away teams cannot be the same");
    }

    /**
     * Verifies that the total score is correctly calculated
     * as the sum of the home and away scores.
     */
    @Test
    public void testTotalScoreIsCorrect(){
        Match match = new Match("Germany", "France");
        match.updateScore(3, 2);
        assertEquals(5, match.getTotalScore(), "Total score should be the sum of home and away scores");
    }

    /**
     * Test data of Match and record MatchSummary are equal when calling toMatchSummary().
     */
    @Test
    public void testMatchToMatchSummary() {
        Match match = new Match("germany", "france");
        match.updateScore(3, 2);
        MatchSummary summary = match.toMatchSummary();

        assertEquals("germany", summary.homeTeam(), "Home team should be 'Germany'");
        assertEquals("france", summary.awayTeam(), "Away team should be 'France'");
        assertEquals(3, summary.homeScore(), "Home score should be 3");
        assertEquals(2, summary.awayScore(), "Away score should be 2");
        assertTrue(summary.startedAt() > 0, "Expected match start time to be greater than zero");
    }
}