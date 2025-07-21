package com.sportsradar;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatchIdentifierTest {
    @Test
    void normalizeTrimsAndCollapsesWhitespaceAndLowercases() {
        MatchIdentifier id = new MatchIdentifier("  Real   Madrid  ", " FC  Barcelona");
        assertEquals("real madrid", id.homeTeam());
        assertEquals("fc barcelona", id.awayTeam());
    }

    @Test
    void constructorThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> new MatchIdentifier(null, "Away"));
        assertThrows(NullPointerException.class, () -> new MatchIdentifier("Home", null));
    }

    @Test
    void constructorThrowsOnEmptyOrWhitespaceOnly() {
        assertThrows(IllegalArgumentException.class, () -> new MatchIdentifier("", "Away"));
        assertThrows(IllegalArgumentException.class, () -> new MatchIdentifier("   ", "Away"));
        assertThrows(IllegalArgumentException.class, () -> new MatchIdentifier("Home", ""));
        assertThrows(IllegalArgumentException.class, () -> new MatchIdentifier("Home", "   "));
    }

    @Test
    void constructorThrowsWhenTeamsAreEqualAfterNormalize() {
        assertThrows(IllegalArgumentException.class, () -> new MatchIdentifier("Team", "team"));
    }

    @Test
    void equalsAndHashCodeEnsureUniqueness() {
        MatchIdentifier id1 = new MatchIdentifier("A", "B");
        MatchIdentifier id2 = new MatchIdentifier(" a ", "  b  ");
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }
}