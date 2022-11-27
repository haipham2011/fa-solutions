import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './routes/App'
import {
    createBrowserRouter,
    RouterProvider,
    Navigate
} from "react-router-dom";
import ErrorPage from "./routes/ErrorPage";
import OverviewPage from "./routes/OverviewPage";

const router = createBrowserRouter([
    {
        path: "/",
        element: <App />,
        errorElement: <ErrorPage />,
        children: [

            // {
            //     path: "statistics",
            //     element: <DiagnosticsPage />,
            // },
            // {
            //     path: "",
            //     element: <Navigate to="/overview" replace />
            // }
        ],
    },
    {
        path: "stock/:company",
        element: <OverviewPage />,
        errorElement: <ErrorPage />,
    },
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
      <RouterProvider router={router} />
  </React.StrictMode>
)
