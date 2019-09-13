import React from "react";
import Button from "./Button";
import style from "../module/scoring.module.css";

import gql from "graphql-tag";
import { useApolloClient } from "@apollo/react-hooks";

const SET_RUN_BALL = gql`
  mutation addBall(
    $innings: Int!
    $matchId: Int!
    $teamId: Int!
    $batsman: Int!
    $nonStriker: Int!
    $bowler: Int!
    $overs: Int!
    $ball: Int!
    $runsTotal: Int!
  ) {
    AddBall(
      ballInput: {
        innings: $innings
        matchId: $matchId
        teamId: $teamId
        batsman: $batsman
        nonStriker: $nonStriker
        bowler: $bowler
        overs: $overs
        ball: $ball
        runsTotal: $runsTotal
      }
    ) {
      id
    }
  }
`;

const RunSection = ({
  innings,
  matchId,
  teamId,
  battingScorecard,
  bowler,
  overs,
  ball
}) => {
  const runs = [0, 1, 2, 3, 4, 5, 6, 7];

  const client = useApolloClient();
  // since we have two batsman
  const batsman =
    battingScorecard[0].batting === 1
      ? battingScorecard[0].batsman.id
      : battingScorecard[1].batsman.id;
  const nonStriker =
    battingScorecard[1].batting === 1
      ? battingScorecard[1].batsman.id
      : battingScorecard[0].batsman.id;

  /**
   * function to process run clicks
   * @param {*} run
   */
  const handleRuns = run => {
    client
      .mutate({
        mutation: SET_RUN_BALL,
        variables: {
          innings: innings,
          matchId: matchId,
          teamId: teamId,
          batsman: batsman,
          nonStriker: nonStriker,
          bowler: bowler,
          overs: overs,
          ball: ball,
          runsTotal: run
        }
      })
      .then(console.log);
  };

  return (
    <div className={style.center}>
      <h3>Runs</h3>
      {runs.map(run => (
        <Button
          className={style.run_btn}
          onClick={() => handleRuns(run)}
          key={run}
        >
          {run}
        </Button>
      ))}
    </div>
  );
};

export default RunSection;
