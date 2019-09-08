import React, { createContext, useState, useEffect } from "react";
import gql from "graphql-tag";
import { useQuery } from "@apollo/react-hooks";
var nnjson = require("nnjson");

export const ScoreContext = createContext();

const GET_SCORE_QUERY = gql`
  {
    GetMatchScorecard(teamId: 1, matchId: 1) {
      totalScorecards {
        team {
          teamName
        }
        innings
        overs
        balls
        totalRuns
        wickets
      }
      bowlerScorecard {
        bowler {
          nickName
        }
        overs
        balls
        runs
        wickets
      }
      batsmanScorecards {
        batsman {
          nickName
        }
        runs
        balls
        batting
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
      totalScorecards {
        team {
          teamName
        }
        innings
        overs
        balls
        totalRuns
        wickets
      }
      bowlerScorecard {
        bowler {
          nickName
        }
        overs
        balls
        runs
        wickets
      }
      batsmanScorecards {
        batsman {
          nickName
        }
        runs
        balls
        batting
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
  const { subscribeToMore, loading, data, error } = useQuery(GET_SCORE_QUERY);
  const [scorecard, setScorecard] = useState({});
  const _subscribeToTotal = subscribeToMore => {
    subscribeToMore({
      document: SUBS_SCORE_QUERY,
      variables: { matchID: 1 },
      updateQuery: (prev, { subscriptionData }) => {
        if (!subscriptionData.data) return prev;
        const newScore = subscriptionData.data.BallAdded;
        //console.log(newScore);
        setScorecard(Object.assign({}, newScore));
      }
    });
  };

  useEffect(() => {
    _subscribeToTotal(subscribeToMore);
    if (!error && !loading) {
      const newScore = nnjson.removeNull(data.GetMatchScorecard);
      setScorecard(newScore);
    }
  }, [loading, data, error, subscribeToMore]);

  return (
    <ScoreContext.Provider value={[scorecard, setScorecard]}>
      {props.children}
    </ScoreContext.Provider>
  );
};
