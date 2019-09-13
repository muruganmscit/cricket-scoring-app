import React from "react";

const Button = ({ className, onClick, children }) => {
  return (
    <button type="button" onClick={onClick} className={className}>
      {children}
    </button>
  );
};

export default Button;
