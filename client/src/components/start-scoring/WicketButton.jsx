import React from "react";
import style from "../client-view/bottomtab.module.css";

const WicketButton = ({ value }) => {
  return <button className={style.wicket_btn}>{value}</button>;
};

export default WicketButton;
