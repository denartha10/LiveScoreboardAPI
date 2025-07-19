package com.sportsradar;

import java.util.Comparator;

@SuppressWarnings("ReassignedVariable")
class DefaultMatchRanking implements Comparator<Match> {
    @Override
    public int compare(Match o1, Match o2) {
        // compare by total score first
        int scoreComparison = Integer.compare(o2.getHomeScore() + o2.getAwayScore(), o1.getHomeScore() + o1.getAwayScore());

        // if scores are equal, compare by start time
        if (scoreComparison == 0) scoreComparison = Long.compare(o1.getStartTime(), o2.getStartTime());

        // if still equal, compare by team names
        if (scoreComparison == 0) scoreComparison =
                (o1.getHomeTeam() + o1.getAwayTeam())
                .compareTo(o2.getHomeTeam() + o2.getAwayTeam());

        return scoreComparison;
    }
}
