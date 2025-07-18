# Scoreboard Project

## Match Class Design

This initial design for the `Match` class encapsulates all relevant match details including 
- home and away teams, 
- their current scores, 
- and the match start time. 

It implements `Comparable` to support 
- ordering by total goals scored (descending), 
- then by recency (more recent matches first), 
- and finally by team names to break ties, 

ensuring consistent and unique sorting in our collection. Score updates are handled through a dedicated method, keeping the class focused and cohesive for use within a larger scoreboard system.

> I have written some initial tests to validate the functionality of the `Match` class.