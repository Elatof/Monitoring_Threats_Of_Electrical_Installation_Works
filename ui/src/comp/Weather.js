import { useState } from "react";
import { useGeolocated } from "react-geolocated";
import { NotificationManager } from 'react-notifications';
import "./Weather.css"
import Cookies from 'universal-cookie';

function Weather() {
    const { coords, isGeolocationAvailable, isGeolocationEnabled } =
        useGeolocated({
            positionOptions: {
                enableHighAccuracy: false,
            },
            userDecisionTimeout: 5000,
        }) ;

    const [response, setResponse] = useState();
    if (coords && !response) {
        let cookie = new Cookies();
        fetch(`http://localhost:8080/service-api/weather/current?lat=${coords.latitude}&lon=${coords.longitude}`,
            {
                headers: {
                    "Authorization": "Bearer " + cookie.get('token')
                }
            })
            .then(response => { return response.json(); })
            .then(data => { 
                setResponse(data); 
                let level = data.treatmentLevel;
                if(level > 4 && level < 7) {
                    NotificationManager.warning('Ви знаходитесь в регіоні з рівнем загрози: ' + level + ', будьте обережні');
                }
                if(level > 6) {
                    NotificationManager.warning('Ви знаходитесь в регіоні з рівнем загрози: ' + level + ', будьте обережні');
                }
            })
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
                        <td><b>Коородинати: </b></td>
                        <td>{response.coord.lon} {response.coord.lat}</td>
                    </tr>
                    <tr>
                        <td><b>Локація: </b></td>
                        <td>{response.name}</td>
                    </tr>
                    <tr>
                        <td><b>Температура: </b></td>
                        <td>{response.main.temp} °C</td>
                    </tr>
                    <tr>
                        <td><b>Тиск: </b></td>
                        <td>{response.main.pressure} п.</td>
                    </tr>
                    <tr>
                        <td><b>Швидкість вітру: </b></td>
                        <td>{response.wind.speed} м.с.</td>
                    </tr>
                </tbody>
            </table>
            <table className="treatment">
                <tbody>
                    <tr>
                        <td><b>Рівень загрози: </b></td>
                        <td style={{ background: response.treatmentColor }}>{response.treatmentLevel}</td>
                    </tr>
                    <tr>
                        <td><b>Стан: </b></td>
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