import { useParams } from "react-router-dom";
import {useEffect, useState} from "react";
import {CartesianGrid, Legend, Line, LineChart, Tooltip, XAxis, YAxis} from "recharts";
import {API_URL} from "../utils";

export default function OverviewPage() {
    const { company } = useParams()
    const [stockData, setStockData] = useState({})
    const [stockStats, setStockStats] = useState(null)

    useEffect(() => {
        fetch(`${API_URL}/stock/${company}`)
            .then((response) => response.json())
            .then((data) => setStockData(data));
    }, [company])

    const filterStock = (dateRange) => {
        fetch(`${API_URL}/stock/${company}?dateRange=${dateRange}`)
            .then((response) => response.json())
            .then((data) => setStockData(data));

        fetch(`${API_URL}/stock/statistics/${company}?dateRange=${dateRange}`)
            .then((response) => response.json())
            .then((data) => setStockStats(data));
    }

    useEffect(() => {
        fetch(`${API_URL}/stock/statistics/${company}`)
            .then((response) => response.json())
            .then((data) => setStockStats(data));
    }, [company])

    return (<>
        <div>
            <h1>{stockData.name}</h1>
            <LineChart width={500} height={300} data={stockData.stockList?.map(stock => {
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
            <div>
                <button onClick={() => filterStock("3d")}>3 days</button>
                <button onClick={() => filterStock("w")}>One week</button>
                <button onClick={() => filterStock("m")}>One month</button>
                <button onClick={() => filterStock("6m")}>6 months</button>
                <button onClick={() => filterStock("y")}>One year</button>
            </div>
            {stockStats && <div>
                <h2>Statistics</h2>
                <div>Min: {stockStats.min.price} {stockStats.min.date}</div>
                <div>Max: {stockStats.max.price} {stockStats.max.date}</div>
                <div>Change rate: {stockStats.changeRate}</div>
            </div>}
        </div>
    </>)
}