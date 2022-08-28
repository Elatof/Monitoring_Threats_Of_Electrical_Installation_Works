import { useState } from "react";
import { useGeolocated } from "react-geolocated";
import { NotificationManager } from 'react-notifications';
import "./Weather.css"

function Weather() {
    const { coords, isGeolocationAvailable, isGeolocationEnabled } =
        useGeolocated({
            positionOptions: {
                enableHighAccuracy: false,
            },
            userDecisionTimeout: 5000,
        });

    const [response, setResponse] = useState();
    if (coords && !response) {
        console.log(response);
        fetch(`http://localhost:8080/service-api/weather/current?lat=${coords.latitude}&lon=${coords.longitude}`)
            .then(response => { return response.json(); })
            .then(data => { setResponse(data); })

    }

    return !isGeolocationAvailable ? (
        NotificationManager.warning('Браузер не підтримує геолокацію')
    ) : !isGeolocationEnabled ? (
        NotificationManager.warning('Відмова доступу до геолокації')
    ) : response ? (
        <div className="location">
            <div className="heade">Інформація про теперішню геолокацію</div>
            <table >
                <tbody>
                    <tr>
                        <td>Коородинати: </td>
                        <td>{response.coord.lon} {response.coord.lat}</td>
                    </tr>
                    <tr>
                        <td>Локація:</td>
                        <td>{response.name}</td>
                    </tr>
                    <tr>
                        <td>Температура:</td>
                        <td>{response.main.temp} °C</td>
                    </tr>
                    <tr>
                        <td>Тиск:</td>
                        <td>{response.main.pressure} п.</td>
                    </tr>
                    <tr>
                        <td>Швидкість вітру: </td>
                        <td>{response.wind.speed} м.с.</td>
                    </tr>
                </tbody>
            </table>
            <table className="treatment">
                <tbody>
                    <tr>
                        <td>Рівень загрози: </td>
                        <td style={{ background: response.treatmentColor }}>{response.treatmentLevel}</td>
                    </tr>
                    <tr>
                        <td>Стан: </td>
                        {response.weather.map(element => <td>{element.description}</td>)}
                    </tr>
                </tbody>
            </table>
        </div>
    ) : (
        <div>Getting the location data&hellip; </div>
    );
}

export default Weather;