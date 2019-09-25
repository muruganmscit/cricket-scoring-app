import React from "react";
import { Switch, Route } from "react-router-dom";
import Scoring from "./components/scoring-view/Scoring";
import StartMatch from "./components/match/start/StartMatch";
import ListMatches from "./components/match/list/ListMatches";
import ShowDetails from "./components/ShowDetails";
import { ScoreProvider } from "./components/context/ScoreProvider";

const App = () => {
  return (
    <ScoreProvider>
      <div className="App">
        <Route exact path="/" component={Scoring} />
        <Switch>
          <Route exact path="/matches" component={ListMatches} />
          <Route exact path="/start_match/:id" component={StartMatch} />
          <Route exact path="/scoring/:id" component={Scoring} />
          <Route exact path="/show/:id" component={ShowDetails} />
        </Switch>
      </div>
    </ScoreProvider>
  );
};

export default App;
