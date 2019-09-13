import React, { useState } from "react";
import Button from "./Button";
import style from "../module/scoring.module.css";

import gql from "graphql-tag";
import { useApolloClient } from "@apollo/react-hooks";

const SET_BATSMAN = gql`
  mutation setNewBatsman(
    $batsmanId: Long!
    $matchId: Long!
    $isOnStrike: Boolean!
  ) {
    SetNewBatsman(
      batsmanId: $batsmanId
      matchId: $matchId
      isOnStrike: $isOnStrike
    )
  }
`;

const BatsmanSetSection = ({ matchId }) => {
  const [striker, setStriker] = useState("");
  const [nonstriker, setNonstriker] = useState("");
  const client = useApolloClient();

  const handleClick = value => {
    if (value !== "") {
      const batsmanId = value === 0 ? striker : nonstriker;
      const isfacing = value === 0 ? true : false;
      client
        .mutate({
          mutation: SET_BATSMAN,
          variables: {
            batsmanId: batsmanId,
            matchId: matchId,
            isOnStrike: isfacing
          }
        })
        .then(console.log);
      setStriker("");
      setNonstriker("");
    } else {
      alert("enter the batsman id");
    }
  };

  return (
    <div className={style.center}>
      <h3>Setting Batsman</h3>
      Striker:
      <input
        type="text"
        className={style.text}
        value={striker}
        onChange={e => setStriker(e.target.value)}
      />
      <Button className={style.run_btn} key="0" onClick={() => handleClick(0)}>
        SET
      </Button>{" "}
      Non Striker:
      <input
        type="text"
        className={style.text}
        value={nonstriker}
        onChange={e => setNonstriker(e.target.value)}
      />
      <Button className={style.run_btn} key="1" onClick={() => handleClick(1)}>
        SET
      </Button>
    </div>
  );
};

export default BatsmanSetSection;
