import React, { createContext, useState, useEffect } from "react";
import gql from "graphql-tag";
import { useQuery } from "@apollo/react-hooks";
var nnjson = require("nnjson");

export const ScoreContext = createContext();

const GET_SCORE_QUERY = gql`
  query completeDetails($matchId: Long!) {
    ScoreCard: GetMatchScorecardByMatch(matchId: $matchId) {
      match {
        venue
        overs
        city
        currentInnings
        homeTeam {
          id
          teamName
          team3LetterName
          players {
            firstName
            lastName
            nickName
            id
          }
        }
        awayTeam {
          id
          teamName
          team3LetterName
          players {
            firstName
            lastName
            nickName
            id
          }
        }
        winningTeam {
          id
          teamName
          team3LetterName
        }
      }
      totalScorecards {
        team {
          id
          teamName
          team3LetterName
        }
        innings
        overs
        balls
        totalRuns
        wickets
      }
      bowlerScorecard {
        bowler {
          id
          nickName
        }
        overs
        balls
        runs
        wickets
      }
      batsmanScorecards {
        batsman {
          id
          nickName
        }
        runs
        balls
        batting
      }
      playing11Innings1 {
        batsman {
          id
          nickName
          firstName
          lastName
        }
      }
      playing11Innings2 {
        batsman {
          id
          nickName
          firstName
          lastName
        }
      }
      balls {
        ball
        extraType
        runsTotal
      }
    }
  }
`;

const SUBS_SCORE_QUERY = gql`
  subscription BallAddedSub($matchID: Int!) {
    BallAdded(matchId: $matchID) {
      match {
        venue
        overs
        city
        currentInnings
        homeTeam {
          id
          teamName
          team3LetterName
          players {
            firstName
            lastName
            nickName
            id
          }
        }
        awayTeam {
          id
          teamName
          team3LetterName
          players {
            firstName
            lastName
            nickName
            id
          }
        }
        winningTeam {
          id
          teamName
          team3LetterName
        }
      }
      totalScorecards {
        team {
          id
          teamName
          team3LetterName
        }
        innings
        overs
        balls
        totalRuns
        wickets
      }
      bowlerScorecard {
        bowler {
          id
          nickName
        }
        overs
        balls
        runs
        wickets
      }
      batsmanScorecards {
        batsman {
          id
          nickName
        }
        runs
        balls
        batting
      }
      playing11Innings1 {
        batsman {
          id
          nickName
          firstName
          lastName
        }
      }
      playing11Innings2 {
        batsman {
          id
          nickName
          firstName
          lastName
        }
      }
      balls {
        ball
        extraType
        runsTotal
      }
    }
  }
`;

export const ScoreProvider = props => {
  const { subscribeToMore, loading, data, error } = useQuery(GET_SCORE_QUERY, {
    variables: { matchId: 4 }
  });
  const [scorecard, setScorecard] = useState({});
  //const [match, setMatch] = useState({});
  const _subscribeToTotal = subscribeToMore => {
    subscribeToMore({
      document: SUBS_SCORE_QUERY,
      variables: { matchID: 4 },
      updateQuery: (prev, { subscriptionData }) => {
        if (!subscriptionData.data) return prev;
        const newScore = subscriptionData.data.BallAdded;
        setScorecard(Object.assign({}, newScore));
      }
    });
  };

  useEffect(() => {
    _subscribeToTotal(subscribeToMore);
    if (!error && !loading) {
      const newScore = nnjson.removeNull(data.ScoreCard);
      setScorecard(newScore);
      //const matchDetails = nnjson.removeNull(data.MatchDeatils);
      //setMatch(matchDetails);
    }
  }, [loading, data, error, subscribeToMore]);

  return (
    <ScoreContext.Provider
      value={{
        score: [scorecard, setScorecard]
      }}
    >
      {props.children}
    </ScoreContext.Provider>
  );
};
