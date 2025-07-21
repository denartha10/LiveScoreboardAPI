# Scoreboard Project

## Contents

1. [Guidelines, Key Requirements & Assumptions](#guidelines-key-requirements--assumptions)
2. [Testing Strategy](#testing-strategy)
3. [Some Interesting Edge Cases](#some-interesting-edge-cases)
4. [Class Reference](#class-reference)
5. [Design Notes](#design-notes)

---

## Guidelines, Key Requirements & Assumptions

### Guidelines

* Solution must be a simple library exposing a public API for integration into other applications.
* The solution must not use external storage or databases; instead, it should use in-memory data structures (e.g., `NavigableSet` / `TreeSet`) to store match information.
* Focus on Object-Oriented Programming (OOP) and SOLID principles for clean code practices. Test driven development (TDD) is encouraged.

---

### Key Requirements

* Create a live football World Cup scoreboard that can track matches, teams, and scores.
* The scoreboard must allow for:

  * Adding new matches with team names.
  * Updating scores for existing matches.
  * Finding matches currently being played.
  * Retrieving a summary of all matches, sorted by total score and start time.

---

### Assumptions

* Focus on a single competition (e.g., the World Cup) where each team can appear in only one match at a time.
* Team names are unique and non-empty.
* Scores are non-negative integers.
* Complex match events (e.g., fouls, detailed event history) are out of scope; extension would require updating the `Match` class to track events and enforce score validity.
* Designed for live updates; `TreeSet` ensures fast insertion and retrieval based on defined ranking logic.
* Sorting logic is encapsulated in `DefaultMatchRanking` to allow priority changes without modifying core classes.
* The library is designed to be used in single-threaded environments, assuming no concurrent modifications to the scoreboard. If concurrency is required, additional synchronization mechanisms would be needed. Thread‑safety: In a real‑world live application, we’d need to guard our in‑memory maps and sorted sets against concurrent access. A simple approach would be to make all public methods synchronized, or to wrap our collections in Collections.synchronizedMap and synchronizedSortedSet.

## Testing Strategy

* **Unit Tests**: Validate each class in isolation (e.g., `Match` validation and `ScoreBoard` operations). Cover edge cases like invalid team names and disallowed scores.
* **Integration Tests**: Verify interactions between `Match`, `MatchSummary`, and `ScoreBoard`, ensuring end-to-end workflow correctness.

## Some Interesting Edge Cases

* **Tie-breaking**: If total goals are equal, the most recently started match appears first; if still tied, alphabetical order of team names applies.
* **Score Validation**: Scores cannot be negative; updates are only valid if they reflect non-negative integers.
* **Concurrent Matches**: Under single-competition assumption, no two matches share a team. Otherwise `MatchIdentifier` would require more complex unique identification than just team names.

## Class Reference

### `src/main/java/com/sportsradar/ScoreBoard.java`

**Overview:**
Handles live tracking of football World Cup matches. Manages active games, updates scores in real-time, and enforces constraints like preventing duplicate team participation.

**Highlights:**

* Stores matches in a `NavigableSet`, sorted by total goals, start time, and team names.
* Ensures no duplicate matches and that teams aren't in more than one game simultaneously.
* Core methods for starting, updating, and finishing matches.
* Generates match summaries in defined sort order.

**Key Methods:**

* `startMatch(String home, String away)`
* `updateScore(MatchIdentifier id, int homeScore, int awayScore)`
* `finishMatch(MatchIdentifier id)`
* `getSummary()`

---

### `src/main/java/com/sportsradar/Match.java`

**Overview:**
Represents a football match between two teams, tracking names, scores, and start time.

**Highlights:**

* Assumes inputs are validated and normalized by `MatchIdentifier`.
* Tracks mutable scores and immutable start time.
* Exposes immutable summaries via [`MatchSummary`](#srcmainjavacomsportsradarmatchsummaryjava).
* Overrides `equals()` and `hashCode()` based on team identity.

**Key Methods:**

* `updateScore(int homeScore, int awayScore)`
* `toMatchSummary()`
* `equals(Object obj)`, `hashCode()`

---

### `src/main/java/com/sportsradar/DefaultMatchRanking.java`

**Overview:**
Encapsulates match ordering logic, allowing easy adjustments to ranking criteria.

**Sort Criteria:**

1. Higher total score
2. Earlier start time
3. Alphabetical order of team names

**Key Method:**

* `compare(Match o1, Match o2)`

---

### `src/main/java/com/sportsradar/MatchSummary.java`

**Overview:**
An immutable Java `record` summarizing a football match.

**Fields:**

* `homeTeam`, `awayTeam`
* `homeScore`, `awayScore`
* `startedAt` (timestamp)

**Highlights:**

* Title-case formatting for team names.
* Custom `toString()` for readable output.

## Class Reference

### `src/main/java/com/sportsradar/MatchIdentifier.java`

**Overview:**
Provides an immutable, strongly-typed key for a match between two teams, centralizing all name-validation and normalization logic.

**Highlights:**

* Normalizes team names by trimming leading/trailing whitespace, collapsing internal whitespace to single spaces, and lowercasing.
* Enforces non-null, non-empty, and distinct home/away team names in one place.
* Overrides `equals()` and `hashCode()` to allow use as a key in maps and sets.

**Key Methods:**

* `MatchIdentifier(String homeTeam, String awayTeam)` — constructor that performs normalization and validation.

## Design Notes

* Only `ScoreBoard` and `MatchSummary` are public; other classes are package-private.
* Emphasis on immutability, clean data models, and encapsulation.
* Future ranking changes centralized in `DefaultMatchRanking` for minimal impact on core logic.
* **Strongly‑Typed Identifiers**: Replacing raw home/away string parameters with a `MatchIdentifier` record ensures that all team-name validation, normalization (trim, collapse whitespace, lowercase), and uniqueness checks happen exactly once. This approach makes the API safer, unambiguous, and resilient against typos or inconsistent casing.