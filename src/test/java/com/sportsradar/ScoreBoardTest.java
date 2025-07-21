package com.sportsradar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreBoardTest {

    private ScoreBoard scoreBoard;

    @BeforeEach
    public void setUp() {
        scoreBoard = new ScoreBoard(); // Assume this is your implementation
    }

    @Test
    void startMatch_NormalCase() {
        assertDoesNotThrow(() -> scoreBoard.startMatch("Mexico", "Canada"));
        List<MatchSummary> summary = scoreBoard.getSummary();
        assertEquals(1, summary.size());
        assertEquals("mexico", summary.getFirst().homeTeam(), "Home team should be 'Mexico");
        assertEquals("canada", summary.getFirst().awayTeam(), "Away team should be 'Canada'");
    }

    @Test
    void startMatch_TeamNamesNull() {
        assertThrows(NullPointerException.class, () -> scoreBoard.startMatch(null, "Canada"), "Home team name cannot be null");
        assertThrows(NullPointerException.class, () -> scoreBoard.startMatch("Mexico", null), "Away team name cannot be null");
        assertThrows(NullPointerException.class, () -> scoreBoard.startMatch(null, null), "Both team names cannot be null");
    }

    @Test
    void startMatch_TeamNamesEmpty() {
        assertThrows(IllegalArgumentException.class, () -> scoreBoard.startMatch("  ", "Canada"), "Home team name cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> scoreBoard.startMatch("Mexico", "   "), "Away team name cannot be empty");
    }

    @Test
    void startMatch_TeamNamesSame() {
        assertThrows(IllegalArgumentException.class, () -> scoreBoard.startMatch("Mexico", "Mexico"), "Home and away teams cannot be the same");
    }

    @Test
    void startMatch_TeamAlreadyPlaying_throws() {
        scoreBoard.startMatch("Mexico", "Canada");
        assertThrows(IllegalStateException.class, () -> scoreBoard.startMatch("Mexico", "Brazil"), "Home team is already playing");
        assertThrows(IllegalStateException.class, () -> scoreBoard.startMatch("Spain", "Canada"), "Away team is already playing");
    }

    @Test
    void startMatch_FixtureAlreadyExists_throws() {
        scoreBoard.startMatch("Mexico", "Canada");
        assertThrows(IllegalStateException.class, () -> scoreBoard.startMatch("Mexico", "Canada"), "Match already exists between Mexico and Canada");
    }

    @Test
    void updateScore() {
        MatchIdentifier id = scoreBoard.startMatch("Mexico", "Canada");
        scoreBoard.updateScore(id, 1, 2);

        MatchSummary match = scoreBoard.getSummary().getFirst();
        assertEquals(1, match.homeScore(), "Home score should be 1");
        assertEquals(2, match.awayScore(), "Away score should be 2");
    }

    @Test
    void updateScore_MultiTeamOrdering() {
        MatchIdentifier id1 = scoreBoard.startMatch("Mexico", "Canada");
        MatchIdentifier id2 = scoreBoard.startMatch("Spain", "Brazil");
        MatchIdentifier id3 = scoreBoard.startMatch("Germany", "France");

        scoreBoard.updateScore(id1, 0, 5);      // total 5
        scoreBoard.updateScore(id2,10, 2);      // total 12
        scoreBoard.updateScore(id3, 2, 2);     // total 4

        List<MatchSummary> summary = scoreBoard.getSummary();

        assertEquals("spain", summary.get(0).homeTeam(), "First match should be Spain vs Brazil");
        assertEquals("brazil", summary.get(0).awayTeam(), "First match should be Spain vs Brazil");

        assertEquals("mexico", summary.get(1).homeTeam(), "Second match should be Mexico vs Canada");
        assertEquals("canada", summary.get(1).awayTeam(), "Second match should be Mexico vs Canada");

        assertEquals("germany", summary.get(2).homeTeam(), "Third match should be Germany vs France");
        assertEquals("france", summary.get(2).awayTeam(), "Third match should be Germany vs France");
    }

    @Test
    void updateScore_NonExistentMatch_throws() {
        MatchIdentifier id = scoreBoard.startMatch("Mexico", "Canada");
        scoreBoard.finishMatch(id);
        assertThrows(NoSuchElementException.class, () -> scoreBoard.updateScore(id, 1, 1), "Match does not exist between Mexico and Canada");
    }

    @Test
    void finishMatch_NormalCase() {
        MatchIdentifier id = scoreBoard.startMatch("Mexico", "Canada");
        scoreBoard.finishMatch(id);

        List<MatchSummary> summary = scoreBoard.getSummary();
        assertTrue(summary.isEmpty(), "Summary should be empty after finishing the match");
    }

    @Test
    void finishMatch_nonexistent_throws() {
        MatchIdentifier id = scoreBoard.startMatch("Mexico", "Canada");
        scoreBoard.finishMatch(id);
        assertThrows(NoSuchElementException.class, () -> scoreBoard.finishMatch(id), "Match does not exist between Mexico and Canada");
    }

    @Test
    void getSummary() {
        MatchIdentifier id = scoreBoard.startMatch("Mexico", "Canada");
        scoreBoard.updateScore(id, 2, 3);

        List<MatchSummary> summary = scoreBoard.getSummary();

        assertEquals(1, summary.size(), "Summary should contain one match");
        assertEquals("mexico", summary.getFirst().homeTeam(), "Home team should be 'Mexico'");
        assertEquals("canada", summary.getFirst().awayTeam(), "Away team should be 'Canada'");
        assertEquals(2, summary.getFirst().homeScore(), "Home score should be 2");
        assertEquals(3, summary.getFirst().awayScore(), "Away score should be 3");
    }

    @Test
    void getSummary_Empty() {
        List<MatchSummary> summary = scoreBoard.getSummary();
        assertTrue(summary.isEmpty(), "Summary should be empty when no matches have been played");
    }
}