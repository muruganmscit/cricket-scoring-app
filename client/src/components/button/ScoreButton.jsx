import React from 'react';

const ScoreButton = ({label}) => {
  return (
    <div className="middle">
      <a className="btn" href="#">
        <p>{label}</p>
      </a>
    </div>
  );
}

export default ScoreButton;