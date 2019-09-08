import React from "react";
import Logo from "./Logo";
import BottomTab from "./BottomTab";
import { ScoreProvider } from "../context/ScoreProvider";
import style from "./bottomtab.module.css";

const ScoreCard = () => {
  return (
    <ScoreProvider>
      <div className={style.container}>
        <Logo />
        <BottomTab />
      </div>
    </ScoreProvider>
  );
};

export default ScoreCard;
