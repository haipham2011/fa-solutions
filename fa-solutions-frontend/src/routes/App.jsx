import {useEffect, useState} from 'react'
import { Outlet, Link } from "react-router-dom";
import {LineChart ,XAxis, YAxis, CartesianGrid, Line, Tooltip, Legend } from "recharts";
import {API_URL} from "../utils";

function App() {
  const [companies, setCompanies] = useState([])
  useEffect(() => {
      fetch(`${API_URL}/stock`)
          .then((response) => response.json())
          .then((data) => setCompanies(data));
  }, [])

  return (
      <>
          <div className="App">
              <h1>Fa Solutions</h1>
              {companies.map(company => <>
                  <h2>
                      <Link to={`/stock/${company.name}`}><span>{company.name}</span></Link>
                      </h2>
                  <div>
                      <LineChart width={500} height={300} data={company.stockList.map(stock => {
                          return {
                              name: stock.date,
                              price: stock.price
                          }
                      })}>
                          <CartesianGrid stroke="#eee" strokeDasharray="5 5"/>
                          <XAxis dataKey="name"/>
                          <YAxis/>
                          <Tooltip />
                          <Legend />
                          <Line type="monotone" dataKey="price" stroke="#8884d8" />
                      </LineChart>
                  </div>
              </>)}
          </div>
          <Outlet />
      </>

  )
}

export default App
