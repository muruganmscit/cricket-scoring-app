import React from "react";
import { Switch, Route } from "react-router-dom";
import Scoring from "./components/scoring-view/Scoring";
import ShowDetails from "./components/ShowDetails";
import { ScoreProvider } from "./components/context/ScoreProvider";

const App = () => {
  return (
    <ScoreProvider>
      <div className="App">
        <Route exact path="/" component={Scoring} />
        <Switch>
          <Route exact path="/create/:id" component={Scoring} />
          <Route exact path="/show/:id" component={ShowDetails} />
        </Switch>
      </div>
    </ScoreProvider>
  );
};

export default App;
