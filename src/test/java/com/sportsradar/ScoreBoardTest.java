package com.sportsradar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ScoreBoardTest {

    private ScoreBoard scoreBoard;

    @BeforeEach
    public void setUp() {
        DefaultMatchRanking ranking = new DefaultMatchRanking();
        scoreBoard = new ScoreBoard(ranking);
    }

    @Test
    void startMatch_NormalCase() {
        // Test case to start a match with valid team names
        String homeTeam = "germany";
        String awayTeam = "france";
        scoreBoard.startMatch(homeTeam, awayTeam);

        // Verify that the match was added correctly
        List<Match> matches = scoreBoard.getMatches();

        assertNotNull(matches);
        assertFalse(matches.isEmpty());

        Match match = matches.getFirst();
        assertEquals("Germany", match.getHomeTeam());
        assertEquals("France", match.getAwayTeam());

        // Check initial scores
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @Test
    void startMatch_TeamNamesNull() {
        // Test case where team names are null
        String homeTeam = null;
        String awayTeam = null;

        try {
            scoreBoard.startMatch(homeTeam, awayTeam);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Team name cannot be null"));
        }
    }

    @Test
    void startMatch_TeamNamesEmpty() {
        // Test case where team names are empty
        String homeTeam = "";
        String awayTeam = "";
        try {
            scoreBoard.startMatch(homeTeam, awayTeam);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Team name cannot be null or empty"));
        }
    }

    @Test
    void startMatch_TeamNamesSame() {
        // Test case where both team names are the same
        String homeTeam = "germany";
        String awayTeam = "germany";
        try {
            scoreBoard.startMatch(homeTeam, awayTeam);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Teams must be distinct"));
        }
    }

    @Test
    void startMatch_TeamAlreadyPlaying_throws() {
        // Test case where team A already exists in the scoreboard
        String homeTeam = "germany";
        String awayTeam = "france";
        scoreBoard.startMatch(homeTeam, awayTeam);
        try {
            scoreBoard.startMatch(homeTeam, "italy");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("One of the teams is already playing: " + homeTeam));
        }
    }

    @Test
    void startMatch_FixtureAlreadyExists_throws() {
        // Test case where a fixture with the same teams already exists
        String homeTeam = "germany";
        String awayTeam = "france";
        scoreBoard.startMatch(homeTeam, awayTeam);
        try {
            scoreBoard.startMatch(homeTeam, awayTeam);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Match already in progress"));
        }
    }

    @Test
    void updateScore() {
        // Test case to update scores and check ordering
        String homeTeam = "germany";
        String awayTeam = "france";
        scoreBoard.startMatch(homeTeam, awayTeam);
        scoreBoard.updateScore(homeTeam, awayTeam, 2, 1);
        List<Match> matches = scoreBoard.getMatches();
        assertNotNull(matches);
        assertFalse(matches.isEmpty());
        Match match = matches.getFirst();
        assertEquals(2, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
        assertEquals(3, match.getTotalScore());
    }

    @Test
    void updateScore_MultiTeamOrdering() {
        // Test case to check if matches are ordered correctly by total score
        String homeTeam1 = "germany";
        String awayTeam1 = "france";
        String homeTeam2 = "italy";
        String awayTeam2 = "spain";

        // Matches with same score earlier match should come first
        scoreBoard.startMatch(homeTeam1, awayTeam1);
        scoreBoard.startMatch(homeTeam2, awayTeam2);

        List<Match> matches = scoreBoard.getMatches();
        assertNotNull(matches);
        assertEquals(2, matches.size());

        // Check if matches are ordered by earliest start time
        assertEquals(matches.get(0).getTotalScore(),  matches.get(1).getTotalScore());
        assertEquals(homeTeam1, matches.get(0).getHomeTeam());
        assertEquals(awayTeam1, matches.get(0).getAwayTeam());

        // Update scores for both matches
        scoreBoard.updateScore(homeTeam1, awayTeam1, 3, 2);
        scoreBoard.updateScore(homeTeam2, awayTeam2, 4, 1);

        matches = scoreBoard.getMatches();
        assertNotNull(matches);
        assertEquals(2, matches.size());

        // Check if matches are ordered by total score
        assertTrue(matches.get(0).getTotalScore() > matches.get(1).getTotalScore());
        assertEquals(homeTeam2, matches.get(0).getHomeTeam());
        assertEquals(awayTeam2, matches.get(0).getAwayTeam());
    }

    @Test
    void updateScore_NonExistentMatch_throws() {
        // Test case to update score for a match that does not exist
        String homeTeam = "germany";
        String awayTeam = "france";
        try {
            scoreBoard.updateScore(homeTeam, awayTeam, 2, 1);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("No such match"));
        }
    }

    @Test
    void finishMatch_NormalCase() {
        // Test case to finish a match normally
        String homeTeam = "germany";
        String awayTeam = "france";
        scoreBoard.startMatch(homeTeam, awayTeam);
        scoreBoard.updateScore(homeTeam, awayTeam, 2, 1);
        scoreBoard.finishMatch(homeTeam, awayTeam);
        List<Match> matches = scoreBoard.getMatches();
        assertNotNull(matches);
        assertTrue(matches.isEmpty(), "Matches should be empty after finishing the match");
        assertFalse(scoreBoard.getActiveTeams().contains(homeTeam), "Home team should not be active after finishing the match");
        assertFalse(scoreBoard.getActiveTeams().contains(awayTeam), "Away team should not be active after finishing the match");
        assertFalse(scoreBoard.getLookup().containsKey(scoreBoard.key(homeTeam, awayTeam)), "Match should not exist in lookup after finishing");
    }

    @Test
    void finishMatch_nonexistent_throws() {
        // Test case to finish a match that does not exist
        String homeTeam = "germany";
        String awayTeam = "france";
        try {
            scoreBoard.finishMatch(homeTeam, awayTeam);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("No such match"));
        }
    }

    @Test
    void getSummary() {
        // Test case to retrieve summary strings for matches
        String homeTeam1 = "germany";
        String awayTeam1 = "france";
        String homeTeam2 = "italy";
        String awayTeam2 = "spain";

        scoreBoard.startMatch(homeTeam1, awayTeam1);
        scoreBoard.startMatch(homeTeam2, awayTeam2);

        scoreBoard.updateScore(homeTeam1, awayTeam1, 3, 2);
        scoreBoard.updateScore(homeTeam2, awayTeam2, 4, 1);

        String summary = scoreBoard.getSummary();
        assertNotNull(summary);
        assertTrue(summary.contains("1. Germany 3 - France 2"));
        assertTrue(summary.contains("2. Italy 4 - Spain 1"));

        assertEquals("1. Germany 3 - France 2\n2. Italy 4 - Spain 1\n", summary);

        // update Italy vs Spain match
        scoreBoard.updateScore(homeTeam2, awayTeam2, 5, 1);
        summary = scoreBoard.getSummary();
        assertNotNull(summary);
        assertTrue(summary.contains("1. Italy 5 - Spain 1"));
        assertTrue(summary.contains("2. Germany 3 - France 2"));
        assertEquals("1. Italy 5 - Spain 1\n2. Germany 3 - France 2\n", summary);
    }

    @Test
    void getSummary_Empty() {
        // Test case to retrieve summary strings when no matches exist
        String summary = scoreBoard.getSummary();
        assertNotNull(summary);
        assertEquals("No matches in progress", summary, "Summary should indicate no matches are in progress");
    }
}
