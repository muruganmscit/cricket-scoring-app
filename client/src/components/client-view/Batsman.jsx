import React from "react";
import style from "./bottomtabnew.module.css";

const Batsman = ({ batsmanScorecards }) => {
  const { batsman, batting, runs, balls } = batsmanScorecards;
  const striker = batting === 1 ? "*" : "";
  return (
    <div className={`${style.bt} ${style.batsman}`}>
      {batsman.nickName}
      {striker} {runs} <span className={style.batballs}>({balls})</span>
    </div>
  );
};

export default Batsman;
