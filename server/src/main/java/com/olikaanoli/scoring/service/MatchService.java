package com.olikaanoli.scoring.service;

import com.olikaanoli.scoring.config.InningStatus;
import com.olikaanoli.scoring.input.match.CreateMatchInput;
import com.olikaanoli.scoring.input.match.StartMatchInput;
import com.olikaanoli.scoring.model.*;
import com.olikaanoli.scoring.repository.*;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@GraphQLApi
public class MatchService {

    private final MatchRepository matchRepository;
    private final BatsmanScorecardRepository batsmanScorecardRepository;
    private final BowlerScorecardRepository bowlerScorecardRepository;
    private final TotalScorecardRepository totalScorecardRepository;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final MapperFacade mapperFacade;
    private final SubscriptionService subscriptionService;

    public MatchService(
            MatchRepository matchRepository,
            TeamService teamService,
            PlayerService playerService,
            BatsmanScorecardRepository batsmanScorecardRepository,
            BowlerScorecardRepository bowlerScorecardRepository,
            TotalScorecardRepository totalScorecardRepository,
            MapperFacade mapperFacade,
            SubscriptionService subscriptionService
    ) {
        this.matchRepository = matchRepository;
        this.teamService = teamService;
        this.playerService = playerService;
        this.batsmanScorecardRepository = batsmanScorecardRepository;
        this.bowlerScorecardRepository = bowlerScorecardRepository;
        this.totalScorecardRepository = totalScorecardRepository;
        this.mapperFacade = mapperFacade;
        this.subscriptionService = subscriptionService;
    }

    /**
     * Function to get all the matchs
     */
    @GraphQLQuery(name = "GetAllMatches")
    public List<Match> getMatches() {
        return matchRepository.findAll();
    }

    /**
     * Function to get a single match details
     */
    @GraphQLQuery(name = "GetMatchById")
    public Optional<Match> getMatchById(@GraphQLArgument(name = "matchId") Long matchId) {
        return matchRepository.findById(matchId);
    }

    @GraphQLMutation(name = "CreateMatch")
    public Match saveMatch(@GraphQLArgument(name = "createMatchInput") CreateMatchInput match) {

        Match lMatch = mapperFacade.map(match, Match.class);

        // updating the home and away team details
        lMatch.setHomeTeam(teamService.getTeamById(match.getHomeTeamId()).get());
        lMatch.setAwayTeam(teamService.getTeamById(match.getAwayTeamId()).get());

        lMatch.setCurrentInnings(InningStatus.MATCH_INIT);

        return matchRepository.save(lMatch);
    }

    @GraphQLMutation(name = "StartMatch")
    public Match startMatchUpdate(
            @GraphQLArgument(name = "matchId") Long matchId,
            @GraphQLArgument(name = "startMatchInput") StartMatchInput startMatchInput
    ) {
        // getting the match details from server
        Match locMatch = getMatchById(matchId).get();

        // updating the match details with new details
        mapperFacade.map(startMatchInput, locMatch);

        // Setting the Toss winning team
        locMatch.setTossWinner(teamService.getTeamById(startMatchInput.getTossWinnerId()).get());

        // setting the running innings
        locMatch.setCurrentInnings(InningStatus.TEAM_SELECTED);

        // updating the partial data into the table
        matchRepository.save(locMatch);

        // calling the method to update batting & bowling scorecard
        // for both teams
        computePlayerScorecard(
                startMatchInput.getInnings1Team(),
                startMatchInput.getInnings1Playing11(),
                matchId, 1, 2);
        computePlayerScorecard(
                startMatchInput.getInnings2Team(),
                startMatchInput.getInnings2Playing11(),
                matchId, 2, 1);

        subscriptionService.postToSubscribers(locMatch.getId());

        return locMatch;
    }

    /**
     * Updating the match details
     *
     * @param match   Match Object
     * @param matchId Match ID
     * @return match
     */
    @GraphQLMutation(name = "UpdateMatchDetails")
    public Match updateMatch(
            @GraphQLArgument(name = "match") Match match,
            @GraphQLArgument(name = "matchId") Long matchId) {
        Match locMatch = getMatchById(matchId).get();
        mapperFacade.map(match, locMatch);
        return matchRepository.save(locMatch);
    }

    @GraphQLMutation(name = "MatchInningsStatusUpdate")
    public Match matchInningsStatusUpdate(
            @GraphQLArgument(name = "matchId") Long matchId,
            @GraphQLArgument(name = "innings") InningStatus innings) {
        Match locMatch = getMatchById(matchId).get();
        locMatch.setCurrentInnings(innings);
        matchRepository.save(locMatch);

        subscriptionService.postToSubscribers(locMatch.getId());
        return locMatch;
    }

    @GraphQLMutation(name = "EndMatch")
    public Match endMatch(
            @GraphQLArgument(name = "matchId") Long matchId,
            @GraphQLArgument(name = "innings") InningStatus innings,
            @GraphQLArgument(name = "teamId") Long teamId) {
        Match locMatch = getMatchById(matchId).get();

        locMatch.setCurrentInnings(innings);
        // getting team details
        locMatch.setWinningTeam(teamService.getTeamById(teamId).get());
        matchRepository.save(locMatch);

        subscriptionService.postToSubscribers(locMatch.getId());
        return locMatch;
    }

    public InningStatus getInningsForMatch(Long matchId) {
        return matchRepository.findInningsByMatchID(matchId);
    }

    /**
     * Method to update the score card for batting and bowlers
     *
     * @param inningsTeam
     * @param playersList
     * @param matchId
     * @param battingInn
     * @param bowlingInn
     */
    public void computePlayerScorecard(
            Long inningsTeam,
            Set<Long> playersList,
            Long matchId,
            int battingInn,
            int bowlingInn
    ) {

        // 1. Batting & Bowling scorecard with 22 rows for both teams
        Set<BatsmanScorecard> batsmanScorecardSet = new HashSet<>();
        Set<BowlerScorecard> bowlerScorecardSet = new HashSet<>();

        // iterating through 1st innings team team
        Team inn1Team = teamService.getTeamById(inningsTeam).get();
        for (Long player : playersList) {

            Player currentPlayer = playerService.getPlayerById(player).get();

            // Setting the batting scorecard list
            BatsmanScorecard batsmanScorecard = new BatsmanScorecard();
            batsmanScorecard.setMatchId(matchId);
            batsmanScorecard.setInnings(battingInn);
            batsmanScorecard.setTeam(inn1Team);
            batsmanScorecard.setBatsman(currentPlayer);
            batsmanScorecardSet.add(batsmanScorecard);

            // Setting the bowling scorecard list
            BowlerScorecard bowlerScorecard = new BowlerScorecard();
            bowlerScorecard.setMatchId(matchId);
            bowlerScorecard.setInnings(bowlingInn);
            bowlerScorecard.setTeam(inn1Team);
            bowlerScorecard.setBowler(currentPlayer);
            bowlerScorecardSet.add(bowlerScorecard);
        }

        // updating the batting scorecard table
        batsmanScorecardRepository.saveAll(batsmanScorecardSet);

        // updating the bowling scorecard table
        bowlerScorecardRepository.saveAll(bowlerScorecardSet);

        // 3. total_scorecard table with 2 rows one for each team.
        TotalScorecard totalScorecard = new TotalScorecard();
        totalScorecard.setMatchId(matchId);
        totalScorecard.setTeam(inn1Team);
        totalScorecard.setInnings(battingInn);
        totalScorecardRepository.save(totalScorecard);
    }
}
