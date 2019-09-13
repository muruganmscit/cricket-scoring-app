import React, { useState } from "react";
import Button from "./Button";
import style from "../module/scoring.module.css";

import gql from "graphql-tag";
import { useApolloClient } from "@apollo/react-hooks";

const SET_BOWLER = gql`
  mutation setBowler($bowlerId: Long!, $matchId: Long!) {
    SetNewBowler(bowlerId: $bowlerId, matchId: $matchId)
  }
`;

const BowlerSetSection = ({ matchId }) => {
  const [bowler, setBowler] = useState("");
  const client = useApolloClient();

  const handleClick = () => {
    if (bowler !== "") {
      client
        .mutate({
          mutation: SET_BOWLER,
          variables: {
            bowlerId: bowler,
            matchId: matchId
          }
        })
        .then(console.log);
      setBowler("");
    } else {
      alert("enter the bowler id");
    }
  };

  return (
    <div className={style.center}>
      <h3>Setting Bowler</h3>
      Bowler:
      <input
        type="text"
        className={style.text}
        value={bowler}
        onChange={e => setBowler(e.target.value)}
      />
      <Button className={style.run_btn} onClick={() => handleClick()}>
        SET
      </Button>
    </div>
  );
};

export default BowlerSetSection;
