# Scoreboard Project

## Class Design and Architecture

### Match Class Design

The `Match` class is a simple data holder that encapsulates all relevant match details including:
- home and away teams,
- their current scores,
- and the match start time.

Previously, the class implemented `Comparable` to allow natural ordering for use in collections like red-black trees. However, this approach tightly coupled comparison logic with data representation, which violates the Single Responsibility Principle and reduces maintainability.

#### New Design Decision

To better adhere to **SOLID principles**, **object-oriented best practices**, and **Clean Code**, comparison logic has now been moved into a separate `DefaultMatchRanking` class. 

### DefaultMatchRanking (Comparator) Class Design:

- Keeps the `Match` class focused purely on representing match data.
- Decouples sorting behavior from the data model, making it reusable and testable.
- Allows for flexible sorting strategies without modifying the `Match` class or consumer classes.

The custom `DefaultMatchRanking` is designed to order matches by:
1. Total goals scored (descending),
2. Match recency (more recent first),
3. Team names to break ties.

This ensures consistent, unique sorting within collections while preserving clean separation of concerns. It is a form of premature optimization, as it allows for future extensions without impacting the core `Match` class. However, as a premature optimization, it does not introduce unnecessary overhead or complexity at this stage.

### Scoreboard Class Design

The `Scoreboard` class is responsible for managing a collection of `Match` objects. It provides methods to:

- Start new matches, assuming initial scores are zero, and the `Match` objects contain home and away team names, an important aspect as mentioned in edge cases is that all teams are unique in the context of a single competition (like the World Cup) and so no two matches can have the same team playing at the same time.
- Update scores, ensuring that scores cannot go negative and that updates reflect the current state of the match. This is enforced by the `updateScore` method in the `Match` class.
- Finish matches, which removes them from the scoreboard. I decided to allow matches to be removed from the scoreboard once they are finished, by a method that takes the `Match` object as a parameter. But also a function of the same name that takes the home and away team names as parameters to allow for easier removal of matches without needing to keep track of the `Match` object itself. This function overloading allows for flexibility in how matches are managed.
- Retrieve a summary of all matches, sorted by a ranking strategy. This will return a list of `Match` objects sorted according to the `DefaultMatchRanking` comparator, ensuring that the output is consistent with the defined ranking strategy.

This class uses the `DefaultMatchRanking` comparator to sort matches when retrieving summaries, ensuring that the output is always consistent with the defined ranking strategy. Since the scoreboard is a going to be used for live updates the internal data structure is a `TreeSet` which allows for efficient retrieval and sorting of matches. `TreeSet` internally uses a red-black tree, which provides O(log n) time complexity for insertions and deletions, making it suitable for a live sports scoreboard where matches can be frequently added or removed.

## Testing Strategy

The testing strategy for this project includes:

- **Unit Tests**: Each class has dedicated unit tests to verify its functionality in isolation. This includes testing the `Match` class for correct data representation and the `Scoreboard` class for managing matches and score updates. This includes edge cases such as starting a match with no teams, updating scores to negative values, and ensuring that matches can be added and retrieved correctly.
- **Integration Tests**: Tests that cover the interaction between `Match` and `Scoreboard`, ensuring that matches can be added, scores updated, and summaries retrieved correctly.

- **Simulation Tests**: Simulating a live sports scenario where multiple matches are started, scores updated, and summaries retrieved to ensure the system behaves as expected under realistic conditions.

## Future Features Not Part of Brief

In the future I would like to add thread safety to the `Scoreboard` class to allow for concurrent updates and retrievals. This would involve using thread-safe collections or synchronization mechanisms to ensure that match data remains consistent when accessed by multiple threads. Especially importannt since this will likely be the case in a live sports application where multiple users may be updating scores simultaneously.

I would also like to extend the `Match` class to include more detailed match events, such as player statistics, fouls, and other relevant data. This would allow for a more comprehensive representation of the match and enable richer features in the future. like the types seen in Google live sports updates.

## Some Interesting Edge Cases

- **Tie-breaking**: If two matches have the same total goals, the more recent match should come first. If they are still tied, the team names will be used to break the tie.
- **Empty Matches**: The system should handle cases where matches have not yet started or have no goals scored, ensuring they are still represented in the scoreboard.
- **Scores Update**: Scores should not be able to go negative, and updates should be validated to ensure they reflect the current state of the match. While a score can be reduced, the current implementation does not allow for this, as it we have not added an attribute to track match events or history like referee decisions or penalties that might affect the score after it has been set.
- **Teams Playing At the Same Time** This is based on the assumption that this is one competition since it is a "Live Football World Cup Score Board" and in such a competition there is only one of each team and thus no two matches can have the same team playing at the same time. If this were not the case, we would need to add a unique identifier for each match to ensure that matches can be distinguished even if they involve the same teams. 
