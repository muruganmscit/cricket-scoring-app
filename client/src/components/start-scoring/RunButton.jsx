import React from "react";
import style from "../client-view/bottomtab.module.css";

const RunButton = ({ value, onClick, state }) => {
  return (
    <button value={value} className={style.run_btn} onClick={onClick}>
      {value}
    </button>
  );
};

export default RunButton;
