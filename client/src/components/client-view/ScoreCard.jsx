import React from "react";
import Logo from "./Logo";
import style from "./bottomtab.module.css";
import BottomTabNew from "./BottomTabNew";

const ScoreCard = ({ matchID }) => {
  return (
    <div className={style.container}>
      <Logo />
      <BottomTabNew matchID={matchID} />
    </div>
  );
};

export default ScoreCard;
