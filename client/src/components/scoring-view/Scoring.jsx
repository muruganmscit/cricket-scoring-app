import React, { useContext } from "react";

import { isEmpty } from "../../common/CommonFunctions";

import style from "./module/scoring.module.css";
import RunSection from "./component/RunSection";
import ExtraSection from "./component/ExtraSection";
import WicketSection from "./component/WicketSection";
import BatsmanSetSection from "./component/BatsmanSetSection";
import BowlerSetSection from "./component/BowlerSetSection";
import MatchTitle from "./component/MatchTitle";
import BattingCard from "./component/BattingCard";

import { ScoreContext } from "../context/ScoreProvider";

// TODO: Adding a button to start and end innings [in this we will set all the batting not out batting flag to 4]

const Scoring = ({ client, ...props }) => {
  const { score, match_details } = useContext(ScoreContext);
  const [match] = match_details;
  const [scorecard] = score;
  const { totalScorecards, batsmanScorecards, bowlerScorecard } = scorecard;

  const runningInnings = !isEmpty(match) ? match.currentInnings : 1;
  const bowlingTeam = runningInnings === 2 ? 0 : 1;

  if (!isEmpty(match)) {
    return (
      <div className={style.container}>
        <div>
          <MatchTitle match={match} />
        </div>
        <br />
        {!isEmpty(totalScorecards) ? (
          <div>
            <div className={style.heading}>
              Innings: {runningInnings} <br />
              Overs: {totalScorecards[runningInnings - 1].overs}.
              {totalScorecards[runningInnings - 1].balls}
              <br />
              Total: {totalScorecards[runningInnings - 1].totalRuns}/
              {totalScorecards[runningInnings - 1].wickets}
            </div>
            <br />
            <div className={style.innercontainer}>
              <div className={`${style.innercontainer} ${style.batting}`}>
                <b className={style.heading}>
                  {totalScorecards[runningInnings - 1].team.teamName}
                </b>
                {!isEmpty(batsmanScorecards) ? (
                  <BattingCard battingScorecard={batsmanScorecards} />
                ) : (
                  <div>Batsman Card - Not Set Yet</div>
                )}
              </div>
              <div className={`${style.innercontainer} ${style.batting}`}>
                <b className={style.heading}>
                  {totalScorecards[bowlingTeam].team.teamName}
                </b>
                {!isEmpty(bowlerScorecard) ? (
                  <div>
                    Bowler: {bowlerScorecard.bowler.nickName}{" "}
                    {bowlerScorecard.wickets}-{bowlerScorecard.runs} (
                    {bowlerScorecard.overs}.{bowlerScorecard.balls}){" "}
                  </div>
                ) : (
                  <div>Bowling Card - Not Set Yet</div>
                )}
              </div>
            </div>
            <br />
            <div>
              {!isEmpty(bowlerScorecard) && batsmanScorecards.length === 2 ? (
                <RunSection
                  innings={runningInnings}
                  matchId={props.match.params.id}
                  teamId={totalScorecards[runningInnings - 1].team.id}
                  battingScorecard={batsmanScorecards}
                  bowler={bowlerScorecard.bowler.id}
                  overs={totalScorecards[runningInnings - 1].overs}
                  ball={totalScorecards[runningInnings - 1].balls + 1}
                />
              ) : (
                <div></div>
              )}
            </div>
            <br />
            <div>
              {!isEmpty(bowlerScorecard) && batsmanScorecards.length === 2 ? (
                <ExtraSection
                  innings={runningInnings}
                  matchId={props.match.params.id}
                  teamId={totalScorecards[runningInnings - 1].team.id}
                  battingScorecard={batsmanScorecards}
                  bowler={bowlerScorecard.bowler.id}
                  overs={totalScorecards[runningInnings - 1].overs}
                  ball={totalScorecards[runningInnings - 1].balls + 1}
                />
              ) : (
                <div></div>
              )}
            </div>
            <br />
            <div>
              {!isEmpty(bowlerScorecard) && batsmanScorecards.length === 2 ? (
                <WicketSection
                  innings={runningInnings}
                  matchId={props.match.params.id}
                  teamId={totalScorecards[runningInnings - 1].team.id}
                  battingScorecard={batsmanScorecards}
                  bowler={bowlerScorecard.bowler.id}
                  overs={totalScorecards[runningInnings - 1].overs}
                  ball={totalScorecards[runningInnings - 1].balls + 1}
                />
              ) : (
                <div></div>
              )}
            </div>
            <br />
            <div>
              {isEmpty(batsmanScorecards) || batsmanScorecards.length !== 2 ? (
                <BatsmanSetSection matchId={props.match.params.id} />
              ) : (
                <div></div>
              )}
            </div>
            <br />
            <div>
              {isEmpty(bowlerScorecard) ? (
                <BowlerSetSection matchId={props.match.params.id} />
              ) : (
                <div></div>
              )}
            </div>{" "}
          </div>
        ) : (
          <div className={style.heading}>Match Yet To Start</div>
        )}
      </div>
    );
  } else {
    return <div>Loading....</div>;
  }
};

export default Scoring;
