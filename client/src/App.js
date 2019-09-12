import React from "react";
import { Switch, Route } from "react-router-dom";
import Scoreing from "./components/start-scoring/Scoreing";
import ShowDetails from "./components/ShowDetails";
import { ScoreProvider } from "./components/context/ScoreProvider";

function App() {
  return (
    <ScoreProvider>
      <div className="App">
        <div className="ph3 pv1 background-gray">
          <Route exact path="/" component={Scoreing} />
          <Switch>
            <Route exact path="/create/:id" component={Scoreing} />
            <Route exact path="/show/:id" component={ShowDetails} />
          </Switch>
        </div>
      </div>
    </ScoreProvider>
  );
}

export default App;
