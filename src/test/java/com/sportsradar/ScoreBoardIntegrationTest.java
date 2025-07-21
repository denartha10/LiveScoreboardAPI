package com.sportsradar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ScoreBoardIntegrationTest {
    private ScoreBoard scoreBoard;

    @BeforeEach
    void setup() {
        scoreBoard = new ScoreBoard();
    }

    @Test
    void liveScenarioSimulation() {
        // Start matches
        MatchIdentifier m1 = scoreBoard.startMatch("Mexico", "Canada");
        MatchIdentifier m2 = scoreBoard.startMatch("Spain", "Brazil");
        MatchIdentifier m3 = scoreBoard.startMatch("Germany", "France");

        // Update scores in simulated real-time
        scoreBoard.updateScore(m1, 0, 5);
        scoreBoard.updateScore(m2, 10, 2);
        scoreBoard.updateScore(m3, 2, 2);

        // Retrieve and assert summary ordering
        List<MatchSummary> summary = scoreBoard.getSummary();
        assertEquals(3, summary.size());

        // Spain vs Brazil: total 12 goals -> first
        MatchSummary top = summary.getFirst();
        assertEquals("spain", top.homeTeam());
        assertEquals("brazil", top.awayTeam());
        assertEquals(10, top.homeScore());
        assertEquals(2, top.awayScore());

        // Mexico vs Canada: total 5 goals -> second
        MatchSummary second = summary.get(1);
        assertEquals("mexico", second.homeTeam());
        assertEquals("canada", second.awayTeam());
        assertEquals(0, second.homeScore());
        assertEquals(5, second.awayScore());

        // Germany vs France: total 4 goals -> third
        MatchSummary third = summary.get(2);
        assertEquals("germany", third.homeTeam());
        assertEquals("france", third.awayTeam());
        assertEquals(2, third.homeScore());
        assertEquals(2, third.awayScore());

        // Finish a match and verify it's removed
        MatchSummary finished = scoreBoard.finishMatch(m2);
        assertEquals("spain", finished.homeTeam());
        assertEquals("brazil", finished.awayTeam());

        List<MatchSummary> afterFinish = scoreBoard.getSummary();
        assertEquals(2, afterFinish.size());
        assertFalse(afterFinish.stream()
                .anyMatch(ms -> ms.homeTeam().equals("spain") && ms.awayTeam().equals("brazil")));

        // Attempt to start a match with a team that is still active
        assertThrows(IllegalStateException.class, () -> scoreBoard.startMatch("Mexico", "Italy"));
    }
}