# Scoreboard Project

## Class Design and Architecture

### Match Class Design

The `Match` class is a simple data holder that encapsulates all relevant match details including:

* home and away teams,
* their current scores,
* the match start time,
* and a convenience method for creating a summary snapshot.

#### New Design Decision

To support a clear separation between data representation and presentation logic, `Match` now provides:

```java
public MatchSummary toSummary() {
  return new MatchSummary(homeTeam, awayTeam, homeScore, awayScore, startedAt);
}
```

This allows the `Match` model to remain focused on its own state, while consumers use `MatchSummary` for reporting.

### DefaultMatchRanking (Comparator) Class Design:

* Keeps the `Match` class focused purely on representing match data.
* Decouples sorting behavior from the data model, making it reusable and testable.
* Allows for flexible sorting strategies without modifying the `Match` class or consumer classes.

The custom `DefaultMatchRanking` is designed to order matches by:

1. Total goals scored (descending),
2. Match recency (more recent first),
3. Team names to break ties.

This ensures consistent, unique sorting within collections while preserving clean separation of concerns.

### MatchSummary Class Design

The `MatchSummary` class is an immutable Data Transfer Object (DTO) that captures a snapshot of a `Match` at a point in time. It contains:

* `homeTeam` (String)
* `awayTeam` (String)
* `homeScore` (int)
* `awayScore` (int)
* `startedAt` (Instant)

It provides read-only getters and a helper for formatting

* **Immutability** is guaranteed by `final` fields and no setters.
* **Extensibility**: Future fields (e.g. elapsed time) can be added without breaking existing clients.

### Scoreboard Class Design

The `ScoreBoard` class is responsible for managing a collection of `Match` objects. It provides methods to:

* **Start new matches** (initial scores zero).

  > **Assumption:** All teams are unique within a single competition, so no team can play more than one match at the same time. Attempting to start a match with an active team will throw an error.

* **Update scores**, enforcing non-negative values and reflecting the live state via `Match.updateScore()`.

* **Finish matches**, removing them so their teams become available again. Overloads accept either a `Match` object or home/away team names for flexibility.

* **Retrieve raw matches** via `getMatches()`, returning a `List<Match>` sorted by the injected ranking strategy.

* **Retrieve a structured summary** via `getSummary()`, returning a `List<MatchSummary>` sorted by total goals (desc) then most recent start (desc).

  ```java
  List<MatchSummary> getSummary();
  ```

  Consumers can then call `displayString()` or their own formatting logic.

Internally, `ScoreBoard` uses a `TreeSet<Match>` with a `DefaultMatchRanking` comparator for O(log n) inserts, updates (remove+reinsert), and deletes, ensuring efficient live scoring.

## Testing Strategy

The testing strategy for this project includes:

* **Unit Tests**: Each class has dedicated unit tests to verify its functionality in isolation. This includes testing the `Match` class for correct data representation and the `ScoreBoard` class for managing matches and score updates. This includes edge cases such as starting a match with no teams, updating scores to negative values, and ensuring that matches can be added and retrieved correctly.

* **Integration Tests**: Tests that cover the interaction between `Match`, `MatchSummary`, and `ScoreBoard`, ensuring that matches can be added, scores updated, and summaries retrieved correctly.

* **Simulation Tests**: Simulating a live sports scenario where multiple matches are started, scores updated, and summaries retrieved to ensure the system behaves as expected under realistic conditions.

## Future Features Not Part of Brief

In the future I would like to add thread safety to the `Scoreboard` class to allow for concurrent updates and retrievals. This would involve using thread-safe collections or synchronization mechanisms to ensure that match data remains consistent when accessed by multiple threads. Especially importannt since this will likely be the case in a live sports application where multiple users may be updating scores simultaneously.

I would also like to extend the `Match` class to include more detailed match events, such as player statistics, fouls, and other relevant data. This would allow for a more comprehensive representation of the match and enable richer features in the future, like the types seen in Google live sports updates.

## Some Interesting Edge Cases

* **Tie-breaking**: If two matches have the same total goals, the more recent match should come first. If they are still tied, the team names will be used to break the tie.
* **Empty Matches**: The system should handle cases where matches have not yet started or have no goals scored, ensuring they are still represented in the scoreboard.
* **Scores Update**: Scores should not be able to go negative, and updates should be validated to ensure they reflect the current state of the match. While a score can be reduced, the current implementation does not allow for this, as it we have not added an attribute to track match events or history like referee decisions or penalties that might affect the score after it has been set.
* **Teams Playing At the Same Time**: This is based on the assumption that this is one competition since it is a "Live Football World Cup Score Board" and in such a competition there is only one of each team and thus no two matches can have the same team playing at the same time. If this were not the case, we would need to add a unique identifier for each match to ensure that matches can be distinguished even if they involve the same teams.