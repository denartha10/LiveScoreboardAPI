# Scoreboard Project

## Contents

- 1. [Guidelines, Key Requirements & Assumptions](#guidelines-key-requirements--assumptions)
  - 1. [Guidelines](#guidelines)
  - 2. [Key Requirements](#key-requirements)
  - 3. [Assumptions](#assumptions)
- 2. [Testing Strategy](#testing-strategy)
- 3. [Some Interesting Edge Cases](#some-interesting-edge-cases)
- 4. [Future Features Not Part of Brief](#future-features-not-part-of-brief)
- 5. [Class Reference](#class-reference)
  - 1. [`ScoreBoard` Class](#srcmainjavacomsportsradarscoreboardjava)
  - 2. [`Match` Class](#srcmainjavacomsportsradarmatchjava)
  - 3. [`DefaultMatchRanking` Class](#srcmainjavacomsportsradardefaultmatchrankingjava)
  - 4. [`MatchSummary` Record](#srcmainjavacomsportsradarmatchsummaryjava)
- 6. [Design Notes](#design-notes)

---

## Guidelines, Key Requirements & Assumptions

### Guidelines

* Solution must be a simple library exposing a public API for integration into other applications.
* The solution must not use external storage or databases; instead, it should use in-memory data structures (e.g., `NavigableSet` / `TreeSet`) to store match information.
* Focus on Object Oriented Programming (OOP) and SOLID principles for clean code practices. Test driven development (TDD) is encouraged.

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

## Testing Strategy

* **Unit Tests**: Validate each class in isolation (e.g., `Match` validation and `ScoreBoard` operations). Cover edge cases like invalid team names and disallowed scores.
* **Integration Tests**: Verify interactions between `Match`, `MatchSummary`, and `ScoreBoard`, ensuring end-to-end workflow correctness.
* **Simulation Tests**: Emulate live scenarios with multiple concurrent matches to test performance and correct ordering under realistic loads.

## Some Interesting Edge Cases

* **Tie-breaking**: If total goals are equal, the most recently started match appears first; if still tied, alphabetical order of team names applies.
* **Score Validation**: Scores cannot be negative; updates are only valid if they reflect non-negative integers.
* **Concurrent Matches**: Under single-competition assumption, no two matches share a team. Otherwise, unique match identifiers would be required.

## Future Features Not Part of Brief

* Detailed event tracking (goals, cards, substitutions).
* Support for multiple concurrent competitions or friendly matches.
* Persistence layer integration (e.g., SQL or NoSQL database).
* Real-time notifications or websocket support for live front-end updates.

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
* `updateScore(String home, String away, int homeScore, int awayScore)`
* `finishMatch(String home, String away)`
* `getSummary()`

---

### `src/main/java/com/sportsradar/Match.java`

**Overview:**
Represents a football match between two teams, tracking names, scores, and start time.

**Highlights:**

* Validates inputs: non-null, non-empty, and unique team names.
* Normalizes names (trimmed, lowercase).
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

## Design Notes

* Only `ScoreBoard` and `MatchSummary` are public; other classes are package-private.
* Emphasis on immutability, clean data models, and encapsulation.
* Future ranking changes centralized in `DefaultMatchRanking` for minimal impact on core logic.