import React, { useContext } from "react";
import { isEmpty } from "../../common/CommonFunctions";
import style from "./bottomtabnew.module.css";
import style1 from "../scoring-view/module/scoring.module.css";
import Batsman from "./Batsman";
import MatchTitleCard from "./MatchTitleCard";
//import PlayersList from "../scoring-view/component/PlayersList";
import { ScoreContext } from "./../context/ScoreProvider";

const BottomTabNew = ({ matchID }) => {
  const { score, match_id } = useContext(ScoreContext);

  // setting the match id to re-render the data
  const [setMatchId] = match_id;
  setMatchId(matchID);

  const [scorecard] = score;
  const {
    match,
    totalScorecards,
    batsmanScorecards,
    bowlerScorecard
    //playing11Innings1,
    //playing11Innings2
  } = scorecard;

  // checking for the match details
  if (!isEmpty(match)) {
    const innings = match.currentInnings;
    const bowlingTeam = innings === 2 ? 0 : 1;

    switch (innings) {
      case 0:
        return <MatchTitleCard match={match} />;
      case 1:
      case 2:
        return (
          <div className={style.mainbar}>
            <div className={`${style.bt} ${style.area}`}>
              <div className={`${style.bt} ${style.batteam}`}>
                {totalScorecards[innings - 1].team.team3LetterName}
              </div>
              <div className={`${style.bt} ${style.batscore}`}>
                {totalScorecards[innings - 1].totalRuns} /{" "}
                {totalScorecards[innings - 1].wickets}
              </div>
              <div className={`${style.bt} ${style.overs}`}>
                {totalScorecards[innings - 1].overs}.
                {totalScorecards[innings - 1].balls} / {match.overs}
              </div>
            </div>
            {!isEmpty(batsmanScorecards) ? (
              <div className={`${style.bt} ${style.area}`}>
                {batsmanScorecards.map(batsman => (
                  <Batsman
                    batsmanScorecards={batsman}
                    key={batsman.batsman.id}
                  />
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
                <div className={`${style.bt} ${style.bowler}`}>
                  Yet to arrive
                </div>
              )}
            </div>
          </div>
        );
      case 3:
        return (
          <div className={style.mainbar}>
            <div className={`${style1.team} ${style1.home}`}>
              Innings Break [Target: {totalScorecards[0].totalRuns}]
            </div>
          </div>
        );
      case 5:
        return (
          <div className={style.mainbar}>
            <div className={`${style1.team} ${style1.home}`}>
              {match.winningTeam.teamName} has WON!!!!
            </div>
          </div>
        );
      default:
        return <MatchTitleCard match={match} />;
    }
  } else {
    return <div className={style.mainbar}>Loading</div>;
  }
};

export default BottomTabNew;
