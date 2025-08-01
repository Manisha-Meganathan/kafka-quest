import React from "react";
import { Outlet } from "react-router-dom";
import { Provider } from "react-redux";
import { gameStore } from "./stateManagement/store";

function App() {

  return (
    <React.Fragment>
      <Provider store={gameStore}>
        <Outlet />
      </Provider>
    </React.Fragment>
  )

}

export default App;
