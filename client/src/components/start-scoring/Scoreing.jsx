import React, { useContext, useState } from "react";
import style from "./scoring.module.css";
import RunButton from "./RunButton";
import ExtrasButton from "./ExtrasButton";
import WicketButton from "./WicketButton";
import { ScoreContext } from "./../context/ScoreProvider";

const Scoreing = () => {
  const [match] = useContext(ScoreContext);
  const [scorecard] = useContext(ScoreContext);
  const { totalScorecards, batsmanScorecards, bowlerScorecard } = scorecard;
  const [extraruns, setExtraruns] = useState(1);

  // run scoring function
  const handleRun = run => {
    console.log(run);
  };

  const handleExtras = extra => {
    console.log(extra);
    console.log(extraruns);

    setExtraruns(1);
  };

  const handleChange = e => {
    setExtraruns(e.target.value);
  };

  if (!isEmpty(match)) {
    return (
      <div className={style.container}>
        <div>
          <span>
            Home Team: {match.homeTeam.teamName} <b>VS</b>{" "}
            {match.awayTeam.teamName} :Away Team
          </span>
        </div>
        {!isEmpty(totalScorecards) ? (
          <div>Innings: 1 --> Overs: 0.0</div>
        ) : (
          <div className={style.heading}>Match Yet To Start</div>
        )}
        {!isEmpty(batsmanScorecards) ? (
          <div>
            <div>
              <span className={style.title}>Striker: </span>
              <span>Balamurugan 22 (30)</span>
            </div>
            <div>
              <span className={style.title}>Non Striker:</span>
              <span>Kingshuk 22 (30)</span>{" "}
            </div>{" "}
          </div>
        ) : (
          <div>{""}</div>
        )}
        {!isEmpty(bowlerScorecard) ? (
          <div>Bowler: Pavi 1-22 (3.0) </div>
        ) : (
          <div>{""}</div>
        )}
        Runs:
        <div>
          <RunButton value="0" onClick={() => handleRun(0)} />
          <RunButton value="1" onClick={() => handleRun(1)} />
          <RunButton value="2" onClick={() => handleRun(2)} />
          <RunButton value="3" onClick={() => handleRun(3)} />
          <RunButton value="4" onClick={() => handleRun(4)} />
          <RunButton value="5" onClick={() => handleRun(5)} />
          <RunButton value="6" onClick={() => handleRun(6)} />
          <RunButton value="7" onClick={() => handleRun(7)} />
        </div>
        <br />
        Extras:{" "}
        <input
          className={style.text}
          type="text"
          value={extraruns}
          onChange={handleChange}
        />
        <div>
          <ExtrasButton value="BYES" onClick={() => handleExtras("BYES")} />
          <ExtrasButton
            value="LEG_BYES"
            onClick={() => handleExtras("LEG_BYES")}
          />
          <ExtrasButton
            value="NO_BALL"
            onClick={() => handleExtras("NO_BALL")}
          />
          <ExtrasButton
            value="PENALTY"
            onClick={() => handleExtras("PENALTY")}
          />
          <ExtrasButton value="WIDE" onClick={() => handleExtras("WIDE")} />
          <ExtrasButton
            value="WIDE_BYES"
            onClick={() => handleExtras("WIDE_BYES")}
          />
          <ExtrasButton
            value="NO_BALL_BYES"
            onClick={() => handleExtras("NO_BALL_BYES")}
          />
          <ExtrasButton
            value="NO_BALL_LEG_BYES"
            onClick={() => handleExtras("NO_BALL_LEG_BYES")}
          />
        </div>
        Wickets:
        <div>
          <WicketButton value="BOWLED" />
          <WicketButton value="CAUGHT" />
          <WicketButton value="CAUGHT_AND_BOWLED" />
          <WicketButton value="LBW" />
          <WicketButton value="STUMPED" />
          <WicketButton value="RUN_OUT" />
          <WicketButton value="RETIRED_HURT" />
          <WicketButton value="HIT_WICKET" />
          <WicketButton value="OBSTRUCTING_THE_FIELD" />
          <WicketButton value="HIT_THE_BALL_TWICE" />
          <WicketButton value="HANDLED_THE_BALL" />
          <WicketButton value="TIMED_OUT" />
        </div>
      </div>
    );
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

export default Scoreing;
