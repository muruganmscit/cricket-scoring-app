import React, { useContext } from "react";

import { isEmpty } from "../../common/CommonFunctions";

import style from "./module/scoring.module.css";
import RunSection from "./component/RunSection";
import ExtraSection from "./component/ExtraSection";
import WicketSection from "./component/WicketSection";
import BatsmanSetSection from "./component/BatsmanSetSection";
import BowlerSetSection from "./component/BowlerSetSection";
import MatchTitle from "./component/MatchTitle";
import PlayersList from "./component/PlayersList";
import InningsStatusChange from "./component/InningsStatusChange";
import BattingCard from "./component/BattingCard";

import { ScoreContext } from "../context/ScoreProvider";

// TODO: Adding a button to start and end innings [in this we will set all the batting not out batting flag to 4]

const Scoring = ({ client, ...props }) => {
  const { score, match_id } = useContext(ScoreContext);

  // setting the match id to re-render the data
  const [setMatchId] = match_id;
  setMatchId(props.match.params.id);

  const [scorecard] = score;
  const {
    match,
    totalScorecards,
    batsmanScorecards,
    bowlerScorecard,
    playing11Innings1,
    playing11Innings2
  } = scorecard;

  if (!isEmpty(match)) {
    const innings = match.currentInnings;
    switch (innings) {
      case 0:
        return (
          <div className={style.container}>
            <MatchTitle match={match} />
            <br />
            <PlayersList
              inning1={playing11Innings1}
              inning2={playing11Innings2}
              team={totalScorecards}
              idDisplay={true}
            />
            <InningsStatusChange
              value="FIRST_INNINGS"
              matchId={props.match.params.id}
              title="Start 1st Innings"
            />
          </div>
        );
      case 1:
      case 2:
        const runningInnings = innings;
        const bowlingTeam = runningInnings === 2 ? 0 : 1;
        const inning1Total = totalScorecards[0].totalRuns;
        const innings_flag =
          runningInnings === 2 ? "MATCH_END" : "INNINGS_BREAK";
        const innings_button =
          runningInnings === 2 ? "End Match" : "End 1st Innings";

        const endInnings =
          totalScorecards[runningInnings - 1].overs === match.overs ||
          totalScorecards[runningInnings - 1].wickets === 10 ||
          totalScorecards[1].totalRuns > inning1Total
            ? true
            : false;

        let teamId = 0;
        if (endInnings) {
          teamId =
            totalScorecards[1].totalRuns > inning1Total
              ? totalScorecards[1].team.id
              : totalScorecards[1].totalRuns < inning1Total
              ? totalScorecards[0].team.id
              : -1;
        }

        return (
          <div className={style.container}>
            <MatchTitle match={match} />
            <br />
            <PlayersList
              inning1={playing11Innings1}
              inning2={playing11Innings2}
              team={totalScorecards}
              idDisplay={true}
            />
            <div className={style.heading}>
              Innings: {runningInnings} <br />
              Overs: {totalScorecards[runningInnings - 1].overs}.
              {totalScorecards[runningInnings - 1].balls}
              <br />
              Total: {totalScorecards[runningInnings - 1].totalRuns}/
              {totalScorecards[runningInnings - 1].wickets}
            </div>
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
            <div>
              {!isEmpty(bowlerScorecard) &&
              batsmanScorecards.length === 2 &&
              !endInnings ? (
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
            <div>
              {!isEmpty(bowlerScorecard) &&
              batsmanScorecards.length === 2 &&
              !endInnings ? (
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
            <div>
              {!isEmpty(bowlerScorecard) &&
              batsmanScorecards.length === 2 &&
              !endInnings ? (
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
            <div>
              {(isEmpty(batsmanScorecards) || batsmanScorecards.length !== 2) &&
              !endInnings ? (
                <BatsmanSetSection matchId={props.match.params.id} />
              ) : (
                <div></div>
              )}
            </div>
            <div>
              {isEmpty(bowlerScorecard) && !endInnings ? (
                <BowlerSetSection matchId={props.match.params.id} />
              ) : (
                <div></div>
              )}
            </div>
            {endInnings && (
              <div>
                <InningsStatusChange
                  value={innings_flag}
                  matchId={props.match.params.id}
                  title={innings_button}
                  teamId={teamId}
                />
              </div>
            )}
          </div>
        );
      case 3:
        return (
          <div className={style.container}>
            <MatchTitle match={match} />
            <br />
            <PlayersList
              inning1={playing11Innings1}
              inning2={playing11Innings2}
              team={totalScorecards}
              idDisplay={true}
            />
            <InningsStatusChange
              value="SECOND_INNINGS"
              matchId={props.match.params.id}
              title="Start 2st Innings"
            />
          </div>
        );
      case 5:
        return (
          <div className={style.container}>
            <div className={`${style.team} ${style.home}`}>
              {match.winningTeam.teamName} has WON!!!!
            </div>
          </div>
        );
      default:
        return <MatchTitle match={match} />;
    }
  } else {
    return <div>Loading....</div>;
  }
};

export default Scoring;
