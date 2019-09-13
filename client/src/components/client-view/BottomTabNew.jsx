import React, { useContext } from "react";
import { isEmpty } from "../../common/CommonFunctions";
import style from "./bottomtabnew.module.css";
import Batsman from "./Batsman";
import MatchTitleCard from "./MatchTitleCard";
import { ScoreContext } from "./../context/ScoreProvider";

const BottomTabNew = () => {
  const { score, match_details } = useContext(ScoreContext);
  const [scorecard] = score;
  const [match] = match_details;
  const { totalScorecards, batsmanScorecards, bowlerScorecard } = scorecard;

  const innings = !isEmpty(match) ? match.currentInnings : 1;
  const bowlingTeam = innings === 2 ? 0 : 1;

  if (!isEmpty(scorecard)) {
    if (!isEmpty(totalScorecards) || !isEmpty(batsmanScorecards)) {
      return (
        <div className={style.mainbar}>
          <div className={`${style.bt} ${style.area}`}>
            <div className={`${style.bt} ${style.batteam}`}>
              {totalScorecards[innings - 1].team.team3LetterName}
            </div>
            <div className={`${style.bt} ${style.batscore}`}>
              {totalScorecards[innings - 1].totalRuns}/
              {totalScorecards[innings - 1].wickets}
            </div>
            <div className={`${style.bt} ${style.overs}`}>
              {totalScorecards[innings - 1].overs}.
              {totalScorecards[innings - 1].balls}/{match.overs}
            </div>
          </div>
          {!isEmpty(batsmanScorecards) ? (
            <div className={`${style.bt} ${style.area}`}>
              {batsmanScorecards.map(batsman => (
                <Batsman batsmanScorecards={batsman} key={batsman.batsman.id} />
              ))}
            </div>
          ) : (
            <div className={`${style.bt} ${style.area}`}>
              <div className={`${style.bt} ${style.batsman}`}>
                Yet to arrive
              </div>
              <div className={`${style.bt} ${style.batsman}`}>
                Yet to arrive
              </div>
            </div>
          )}
          <div className={`${style.bt} ${style.area}`}>
            <div className={`${style.bt} ${style.bowlteam}`}>
              {totalScorecards[bowlingTeam].team.team3LetterName}
            </div>
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
              <div className={`${style.bt} ${style.bowler}`}>Yet to arrive</div>
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

export default BottomTabNew;
