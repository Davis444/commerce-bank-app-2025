import React from "react";
import { Route, Routes } from "react-router-dom";  // Don't need BrowserRouter here anymore
import Login from "./Pages/Login";
import CheckURL from "./Pages/checkURL"; // Adjust according to your folder structure

function App() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/checkURL" element={<CheckURL />} />
    </Routes>
  );
}

export default App;
