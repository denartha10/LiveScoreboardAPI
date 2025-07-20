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
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    /**
     * Ensures that match creation fails if either team name is null or empty.
     * Expects an {@link IllegalArgumentException} with a clear message.
     */
    @Test
    public void testMatchTeamsNonNullAndNonEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Match(null, "France"));
        assertEquals("Teams cannot be null or empty", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new Match("Germany", null));
        assertEquals("Teams cannot be null or empty", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new Match("", "France"));
        assertEquals("Teams cannot be null or empty", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new Match("Germany", ""));
        assertEquals("Teams cannot be null or empty", exception.getMessage());
    }

    /**
     * Ensures that a match cannot be created between two identical teams.
     * Expects an {@link IllegalArgumentException} stating the teams must be distinct.
     */
    @Test
    public void testTeamsAreDistinct() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Match("Germany", "Germany"));
        assertEquals("Teams must be distinct", exception.getMessage());
    }

    /**
     * Verifies that the total score is correctly calculated
     * as the sum of the home and away scores.
     */
    @Test
    public void testTotalScoreIsCorrect(){
        Match match = new Match("Germany", "France");
        match.updateScore(3, 2);
        assertEquals(5, match.getTotalScore());
    }


    /**
     * Test data of Match and record MatchSummary are equal when calling toMatchSummary().
     */
    @Test
    public void testMatchToMatchSummary() {
        Match match = new Match("Germany", "France");
        match.updateScore(3, 2);
        MatchSummary summary = match.toMatchSummary();

        assertEquals("Germany", summary.homeTeam());
        assertEquals("France", summary.awayTeam());
        assertEquals(3, summary.homeScore());
        assertEquals(2, summary.awayScore());
        assertTrue(summary.startedAt() > 0); // Ensure startedAt is set
    }

}