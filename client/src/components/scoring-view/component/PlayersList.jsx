import React from "react";

import style from "../module/scoring.module.css";

const PlayersList = ({ inning1, inning2, team, idDisplay }) => {
  return (
    <div>
      <div className={style.playerlist}>
        <span>{team[0].team.teamName}</span>
        <ul>
          {inning1.map(player => (
            <li key={player.batsman.id}>
              <span>
                {player.batsman.firstName} {player.batsman.lastName}
              </span>{" "}
              {idDisplay && (
                <span>
                  [ <b>{player.batsman.id}</b> ]
                </span>
              )}
            </li>
          ))}
        </ul>
      </div>
      <div className={style.playerlist}>
        <span>{team[1].team.teamName}</span>
        <ul>
          {inning2.map(player => (
            <li key={player.batsman.id}>
              <span>
                {player.batsman.firstName} {player.batsman.lastName}
              </span>{" "}
              {idDisplay && (
                <span>
                  [ <b>{player.batsman.id}</b> ]
                </span>
              )}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default PlayersList;
