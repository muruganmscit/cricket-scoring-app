import React from "react";
import style from "../client-view/bottomtab.module.css";

const ExtrasButton = ({ value, onClick }) => {
  return (
    <button className={style.extra_btn} onClick={onClick}>
      {value}
    </button>
  );
};

export default ExtrasButton;
