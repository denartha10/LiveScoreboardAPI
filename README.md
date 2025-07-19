# Scoreboard Project

## Match Class Design

This initial design for the `Match` class encapsulates all relevant match details including 
- home and away teams, 
- their current scores, 
- and the match start time. 

I am not sure if the comparable logic should be in the `Match` class, I am going to compare matches in a collection (red black tree) but should it be up to the consumer of the class to implement the comparison logic?

The `Match` class is designed to encapsulate the details of a football match, including the teams involved, their scores, and the time the match started. It implements the `Comparable` interface to allow for natural ordering based on specific criteria, which is useful when storing matches in a collection like a red-black tree.

It implements `Comparable` to support 
- ordering by total goals scored (descending), 
- then by recency (more recent matches first), 
- and finally by team names to break ties, 

ensuring consistent and unique sorting in our collection. Score updates are handled through a dedicated method, keeping the class focused and cohesive for use within a larger scoreboard system.

> I have written some initial tests to validate the functionality of the `Match` class.