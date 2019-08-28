import React from 'react';
import { Switch, Route } from 'react-router-dom'
import CreateDetails from "./components/CreateDetails"
import ShowDetails from "./components/ShowDetails"

function App() {
  return (
    <div className="App">
      <div className="ph3 pv1 background-gray">
        <Route exact path="/" component={CreateDetails} />
        <Switch>
          <Route exact path="/create/:id" component={CreateDetails} />
          <Route exact path="/show/:id" component={ShowDetails} />
        </Switch>
      </div>
    </div>
  );
}

export default App;
