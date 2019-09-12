import React, { useContext } from "react";
import style from "./bottomtabnew.module.css";
import Batsman from "./Batsman";
import MatchTitleCard from "./MatchTitleCard";
import { ScoreContext } from "./../context/ScoreProvider";

const BottomTabNew = () => {
  const [scorecard] = useContext(ScoreContext);
  const { totalScorecards, batsmanScorecards, bowlerScorecard } = scorecard;
  if (!isEmpty(scorecard)) {
    if (!isEmpty(totalScorecards) || !isEmpty(batsmanScorecards)) {
      return (
        <div className={style.mainbar}>
          <div className={`${style.bt} ${style.area}`}>
            <div className={`${style.bt} ${style.batteam}`}>IMP</div>
            <div className={`${style.bt} ${style.batscore}`}>
              {totalScorecards[0].totalRuns}/{totalScorecards[0].wickets}
            </div>
            <div className={`${style.bt} ${style.overs}`}>
              {totalScorecards[0].overs}.{totalScorecards[0].balls}/20
            </div>
          </div>
          <div className={`${style.bt} ${style.area}`}>
            <Batsman batsmanScorecards={batsmanScorecards[0]} />
            <Batsman batsmanScorecards={batsmanScorecards[1]} />
          </div>
          <div className={`${style.bt} ${style.area}`}>
            <div className={`${style.bt} ${style.bowlteam}`}>AMX</div>
            {!isEmpty(bowlerScorecard) ? (
              <div className={`${style.bt} ${style.bowler}`}>
                {bowlerScorecard.bowler.nickName}{" "}
                {scorecard.bowlerScorecard.wickets}-
                {scorecard.bowlerScorecard.runs}{" "}
                <span className={style.bowlballs}>
                  ({scorecard.bowlerScorecard.overs}.
                  {scorecard.bowlerScorecard.balls})
                </span>
              </div>
            ) : (
              <div className={`${style.bt} ${style.bowler}`}>Last Over</div>
            )}
          </div>
        </div>
      );
    } else {
      return <MatchTitleCard />;
    }
  } else {
    return <div className={style.mainbar}>Loading</div>;
  }
};

function isEmpty(obj) {
  for (var key in obj) {
    if (obj.hasOwnProperty(key)) return false;
  }
  return true;
}

export default BottomTabNew;
