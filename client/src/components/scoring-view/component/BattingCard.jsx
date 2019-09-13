import React from "react";

const BattingCard = ({ battingScorecard }) => {
  return (
    <div>
      {battingScorecard.map(batsman => (
        <div key={batsman.batsman.id}>
          {batsman.batting === 1 ? (
            <span>Striker: </span>
          ) : (
            <span>Non Striker: </span>
          )}
          <span>
            {batsman.batsman.nickName} {batsman.runs} ({batsman.balls})
          </span>
        </div>
      ))}
    </div>
  );
};

export default BattingCard;
