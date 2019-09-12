import React, { useContext } from "react";
import style from "./bottomtabnew.module.css";
import { ScoreContext } from "./../context/ScoreProvider";

const MatchTitleCard = () => {
  const [match] = useContext(ScoreContext);
  return (
    <div className={style.mainbar}>
      <div>
        <div className={`${style.team} ${style.home}`}>
          {match.homeTeam.teamName}
        </div>
        <div className={style.vs}>vs</div>
        <div className={`${style.team} ${style.away}`}>
          {match.awayTeam.teamName}
        </div>
      </div>
      <div className={style.address}>
        Live from {match.venue}, {match.city}
      </div>
    </div>
  );
};

export default MatchTitleCard;
