import React from "react";
import Logo from "./Logo";
import style from "./bottomtab.module.css";
import BottomTabNew from "./BottomTabNew";

const ScoreCard = () => {
  return (
    <div className={style.container}>
      <Logo />
      <BottomTabNew />
    </div>
  );
};

export default ScoreCard;
