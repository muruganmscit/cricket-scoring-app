import React from "react";
import style from "./bottomtabnew.module.css";

const Batsman = ({ batsmanScorecards }) => {
  const { batsman, batting, runs, balls } = batsmanScorecards;
  return (
    <div className={`${style.bt} ${style.batsman}`}>
      {batsman.nickName}
      {batting === "1" ? "*" : ""} {runs}{" "}
      <span className={style.batballs}>({balls})</span>
    </div>
  );
};

export default Batsman;
