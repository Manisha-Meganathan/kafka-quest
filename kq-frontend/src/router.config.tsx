
import { createBrowserRouter, useRouteError } from "react-router-dom";
import App from "./App";
import Dashboard from "./pages/Dashboard";
import WelcomePage from "./pages/WelcomePage";

function ErrorPage() {
  const error = useRouteError() as { statusText?: string; message?: string };

  return (
    <div className="h-screen flex flex-col justify-center items-center gap-2">
      <h1 className="text-3xl font-semibold">Oops!</h1>
      <p>Sorry, an unexpected error has occurred.</p>
      <p className="text-gray-700">
        <i>{error.statusText || error.message}</i>
      </p>
    </div>
  );
}

function UnderConstruction() {
  return (
    <div className="h-full flex flex-col justify-center items-center gap-2">
      <p className="text-xl text-gray-700">This page is under construction</p>
      <p className="text-xs text-gray-500">We are working on it</p>
    </div>
  );
}

function NotFoundPage() {
  return (
    <div className="h-full flex flex-col justify-center items-center gap-2">
      <p className="text-xl text-gray-700">404 NOT FOUND</p>
      <p className="text-xs text-gray-500">Page is not available</p>
    </div>
  );
}

export default createBrowserRouter([
  {
    path: "/",
    element: <App />,
    errorElement: <ErrorPage />,
    children: [
      {
        index: true,
        element: <WelcomePage />,
      },
      {
        path: "puzzle",
        element: <Dashboard />,
      },
    ],
  },
  {
    path: "*",
    element: <NotFoundPage />
  }
]);
