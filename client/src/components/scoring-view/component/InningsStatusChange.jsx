import React from "react";
import Button from "./Button";

import style from "../module/scoring.module.css";

import gql from "graphql-tag";
import { useApolloClient } from "@apollo/react-hooks";

const SET_INNINGS_STATUS = gql`
  mutation matchInningsStatusUpdate($innings: InningStatus, $matchId: Long!) {
    MatchInningsStatusUpdate(innings: $innings, matchId: $matchId) {
      id
    }
  }
`;

const SET_END_MATCH_STATUS = gql`
  mutation endMatch($matchId: Long!, $innings: InningStatus, $teamId: Long!) {
    EndMatch(matchId: $matchId, innings: $innings, teamId: $teamId) {
      id
    }
  }
`;

const InningsStatusChange = ({ value, matchId, title, teamId }) => {
  const client = useApolloClient();

  const handleInnings = innings => {
    console.log(innings);
    if (innings === "MATCH_END") {
      client
        .mutate({
          mutation: SET_END_MATCH_STATUS,
          variables: {
            matchId: matchId,
            teamId: teamId,
            innings: innings
          }
        })
        .then(console.log);
    } else {
      client
        .mutate({
          mutation: SET_INNINGS_STATUS,
          variables: {
            innings: innings,
            matchId: matchId
          }
        })
        .then(console.log);
    }
  };

  return (
    <div>
      <Button className={style.extra_btn} onClick={() => handleInnings(value)}>
        {title}
      </Button>
    </div>
  );
};

export default InningsStatusChange;
