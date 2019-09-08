import React, { useContext } from "react";
import style from "./bottomtab.module.css";
import { ScoreContext } from "./../context/ScoreProvider";

const BottomTab = () => {
  const [scorecard] = useContext(ScoreContext);
  if (!isEmpty(scorecard)) {
    return (
      <div className={style.bottom}>
        <span className={`${style.btn} ${style.battingteam}`}>IMP</span>
        <span className={`${style.btn} ${style.total}`}>
          {scorecard.totalScorecards[0].totalRuns}/
          {scorecard.totalScorecards[0].wickets}
        </span>
        <span className={`${style.btn} ${style.totalovers}`}>
          {scorecard.totalScorecards[0].overs}.
          {scorecard.totalScorecards[0].balls}/20
        </span>
        <span className={`${style.btn} ${style.batsman}`}>
          <span className={style.space}></span>
          {scorecard.batsmanScorecards[0].batsman.nickName}*{" "}
          {scorecard.batsmanScorecards[0].runs}{" "}
          <b className={style.smallfont}>
            ({scorecard.batsmanScorecards[0].balls})
          </b>
          <span className={style.space5}></span>
          {scorecard.batsmanScorecards[1].batsman.nickName}{" "}
          {scorecard.batsmanScorecards[1].runs}{" "}
          <b className={style.smallfont}>
            ({scorecard.batsmanScorecards[1].balls})
          </b>
          <span className={style.space}></span>
        </span>
        <span className={`${style.btn} ${style.bowlingteam}`}>INC</span>
        <span className={`${style.btn} ${style.bolwer}`}>
          <span className={style.space}></span>
          {scorecard.bowlerScorecard.bowler.nickName}
          <span className={style.space}></span>
          <b className={style.bolwerscoresmallfont}>
            {scorecard.bowlerScorecard.wickets}-{scorecard.bowlerScorecard.runs}
          </b>
          <b className={style.smallfont}>
            ({scorecard.bowlerScorecard.overs}.{scorecard.bowlerScorecard.balls}
            )
          </b>
        </span>
      </div>
    );
  } else {
    return <div className={style.bottom}>Loading....</div>;
  }
};

function isEmpty(obj) {
  for (var key in obj) {
    if (obj.hasOwnProperty(key)) return false;
  }
  return true;
}

export default BottomTab;
