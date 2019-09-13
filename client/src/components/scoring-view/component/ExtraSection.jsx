import React, { useState } from "react";
import Button from "./Button";
import style from "../module/scoring.module.css";

import gql from "graphql-tag";
import { useApolloClient } from "@apollo/react-hooks";

const SET_EXTRA_BALL = gql`
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
    $extraType: Extras
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
        extraType: $extraType
      }
    ) {
      id
    }
  }
`;

const ExtraSection = ({
  innings,
  matchId,
  teamId,
  battingScorecard,
  bowler,
  overs,
  ball
}) => {
  const client = useApolloClient();

  const extras = [
    {
      extraType: "BYES",
      value: 0
    },
    {
      extraType: "LEG_BYES",
      value: 1
    },
    {
      extraType: "NO_BALL",
      value: 2
    },
    {
      extraType: "PENALTY",
      value: 3
    },
    {
      extraType: "WIDE",
      value: 4
    },
    {
      extraType: "WIDE_BYES",
      value: 5
    },
    {
      extraType: "NO_BALL_BYES",
      value: 6
    },
    {
      extraType: "NO_BALL_LEG_BYES",
      value: 7
    }
  ];

  // setting the variable to hold the runs for extra
  const [runs, setRuns] = useState(1);

  // since we have two batsman
  const batsman =
    battingScorecard[0].batting === 1
      ? battingScorecard[0].batsman.id
      : battingScorecard[1].batsman.id;
  const nonStriker =
    battingScorecard[1].batting === 1
      ? battingScorecard[1].batsman.id
      : battingScorecard[0].batsman.id;

  const handleExtras = type => {
    client
      .mutate({
        mutation: SET_EXTRA_BALL,
        variables: {
          innings: innings,
          matchId: matchId,
          teamId: teamId,
          batsman: batsman,
          nonStriker: nonStriker,
          bowler: bowler,
          overs: overs,
          ball: ball,
          runsTotal: runs,
          extraType: type
        }
      })
      .then(console.log);
    setRuns(1);
  };

  return (
    <div className={style.center}>
      <h3>Extras</h3>
      Runs:{" "}
      <input
        type="text"
        className={style.text}
        value={runs}
        onChange={e => setRuns(e.target.value)}
      />
      <br />
      {extras.map(extra => (
        <Button
          className={style.extra_btn}
          onClick={() => handleExtras(extra.extraType)}
          key={extra.value}
        >
          {extra.extraType}
        </Button>
      ))}
    </div>
  );
};

export default ExtraSection;
