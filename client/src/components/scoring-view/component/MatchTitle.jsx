import React from "react";
import style from "../module/scoring.module.css";

const MatchTitle = ({ match }) => {
  return (
    <div className={style.container}>
      <div>
        <span className={style.heading}>{match.homeTeam.teamName}</span>{" "}
        <b>VS</b>{" "}
        <span className={style.heading}>{match.awayTeam.teamName}</span>
      </div>
      {/*}
      <div>
        <b>{match.homeTeam.teamName}:</b>{" "}
        {match.homeTeam.players.map(player => (
          <span key={player.id}>
            {player.nickName} [{player.id}],{" "}
          </span>
        ))}
      </div>
      <div>
        <b>{match.awayTeam.teamName}:</b>{" "}
        {match.awayTeam.players.map(player => (
          <span key={player.id}>
            {player.nickName} [{player.id}],{" "}
          </span>
        ))}
        </div>*/}
    </div>
  );
};

export default MatchTitle;
