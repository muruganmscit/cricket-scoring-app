import React from "react";
import ScoreCard from "./client-view/ScoreCard";

const ShowDetails = ({ ...props }) => {
  return (
    <div>
      <ScoreCard matchID={props.match.params.id} />
    </div>
  );
};
export default ShowDetails;
