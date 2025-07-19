# Scoreboard Project

## Match Class Design

The `Match` class is a simple data holder that encapsulates all relevant match details including:
- home and away teams,
- their current scores,
- and the match start time.

Previously, the class implemented `Comparable` to allow natural ordering for use in collections like red-black trees. However, this approach tightly coupled comparison logic with data representation, which violates the Single Responsibility Principle and reduces maintainability.

### New Design Decision

To better adhere to **SOLID principles**, **object-oriented best practices**, and **Clean Code**, comparison logic has now been moved into a separate `DefaultMatchRanking` class. 

## DefaultMatchRanking (Comparator) Class Design:

- Keeps the `Match` class focused purely on representing match data.
- Decouples sorting behavior from the data model, making it reusable and testable.
- Allows for flexible sorting strategies without modifying the `Match` class or consumer classes.

The custom `DefaultMatchRanking` is designed to order matches by:
1. Total goals scored (descending),
2. Match recency (more recent first),
3. Team names to break ties.

This ensures consistent, unique sorting within collections while preserving clean separation of concerns.