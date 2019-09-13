import React, { useState } from "react";
import Button from "./Button";
import style from "../module/scoring.module.css";

import gql from "graphql-tag";
import { useApolloClient } from "@apollo/react-hooks";

const SET_WICKET_BALL = gql`
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
    $wicketType: Wickets
    $wicketPlayer: Int!
    $fielder1: Int
    $fielder2: Int
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
        wicketType: $wicketType
        wicketPlayer: $wicketPlayer
        fielder1: $fielder1
        fielder2: $fielder2
      }
    ) {
      id
    }
  }
`;

const WicketSection = ({
  innings,
  matchId,
  teamId,
  battingScorecard,
  bowler,
  overs,
  ball
}) => {
  const client = useApolloClient();
  const wickets = [
    {
      type: "BOWLED",
      value: 0
    },
    {
      type: "CAUGHT",
      value: 1
    },
    {
      type: "CAUGHT_AND_BOWLED",
      value: 2
    },
    {
      type: "LBW",
      value: 3
    },
    {
      type: "STUMPED",
      value: 4
    },
    {
      type: "RUN_OUT",
      value: 5
    },
    {
      type: "RETIRED_HURT",
      value: 6
    },
    {
      type: "HIT_WICKET",
      value: 7
    },
    {
      type: "OBSTRUCTING_THE_FIELD",
      value: 8
    },
    {
      type: "HIT_THE_BALL_TWICE",
      value: 9
    },
    {
      type: "HANDLED_THE_BALL",
      value: 10
    },
    {
      type: "TIMED_OUT",
      value: 11
    }
  ];

  // since we have two batsman
  const batsman =
    battingScorecard[0].batting === 1
      ? battingScorecard[0].batsman.id
      : battingScorecard[1].batsman.id;
  const nonStriker =
    battingScorecard[1].batting === 1
      ? battingScorecard[1].batsman.id
      : battingScorecard[0].batsman.id;

  // TODO: If its a STUMPED. Check for the wide status
  const handleWicket = value => {
    // if wicket type caught, runout
    console.log(value);
    client
      .mutate({
        mutation: SET_WICKET_BALL,
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
          wicketType: value,
          wicketPlayer: out,
          fielder1: fielder1 === "" ? null : fielder1,
          fielder2: fielder2 === "" ? null : fielder2
        }
      })
      .then(console.log);
  };

  // players responsible for wicket happening
  const [fielder1, setFielder1] = useState("");
  const [fielder2, setFielder2] = useState("");
  const [runs, setRuns] = useState(0);
  const [out, setOut] = useState(batsman);

  return (
    <div className={style.center}>
      <h3>Wickets</h3>
      Runs:{" "}
      <input
        type="text"
        className={style.text}
        value={runs}
        onChange={e => setRuns(e.target.value)}
      />{" "}
      Player Out:{" "}
      <input
        type="text"
        className={style.text}
        value={out}
        onChange={e => setOut(e.target.value)}
      />
      <br />
      Fielder1:{" "}
      <input
        type="text"
        className={style.text}
        value={fielder1}
        onChange={e => setFielder1(e.target.value)}
      />{" "}
      Fielder2:{" "}
      <input
        type="text"
        className={style.text}
        value={fielder2}
        onChange={e => setFielder2(e.target.value)}
      />
      <br />
      {wickets.map(wicket => (
        <Button
          className={style.wicket_btn}
          onClick={() => handleWicket(wicket.type)}
          key={wicket.value}
        >
          {wicket.type}
        </Button>
      ))}
    </div>
  );
};

export default WicketSection;
