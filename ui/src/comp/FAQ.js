import React, { Component } from 'react';
import { NotificationManager } from 'react-notifications';
import Cookies from 'universal-cookie';
import './FAQ.css'

class FAQ extends Component {

    constructor() {
        super();
        this.state = {
            info: null,
            response: false,
            infoValue: [
                ") - Неможливий рівень",
                ") - Мінімальний рівень загрози",
                ") - Мінімальний рівень загрози",
                ") - Мінімальний рівень загрози",
                ") - Середній рівень загрози, приділяти увагу",
                ") - Середній рівень загрози, приділяти увагу",
                ") - Середній рівень загрози, приділяти увагу",
                ") - Високий рівень загрози, заборона робіт",
                ") - Високий рівень загрозби, заборона робіт",
                ") - Високий рівень загрози, заборона робіт",
            ]
        };

        let cookie = new Cookies();
        fetch('http://localhost:8080/service-api/meta/treatment-config', {
            headers: {
                "Authorization": "Bearer " + cookie.get('token')
            }
        }).then(response => { return response.json(); })
            .then(data => {
                this.setState({ info: data, response: true });
            });


        this.getHistData = this.getHistData.bind(this);
    }


    getHistData(e) {
        NotificationManager.success('Початок вичитики даних з сервера');
        fetch(`http://localhost:8080/service-api/weather/all`, {
            headers: {
                "Authorization": "Bearer " + new Cookies().get('token')
            }
        }).then(function (response) {
            if (response.status === 500) {
                NotificationManager.error('Помилка сервера');
            }
            if (response.status === 200) {
                return response.json();
            }
        }).then(data => {
            NotificationManager.success('Успішне отримання даних з серверу, початок генерації файлу спеціально для вас');
            const fileName = "statistic";
            const json = JSON.stringify(data);
            const blob = new Blob([json], { type: 'application/json' });
            const href = URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = href;
            link.download = fileName + ".json";
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        });


    }



    render() {
        let inc = -1;
        console.log(this.state);
        return this.state.response ? (
            <div className="mainDivDetailsItem">
                <div className="heade">FAQ</div>
                <div className="header2">Таблиця загроз</div>
                <tbody className="levels">
                    {
                        this.state.info.map(item => {
                            inc = inc + 1;
                            return <tr className=''>
                                <td style={{ background: item, fontSize: 22 }}>{inc} </td>
                                <td style={{ background: item, fontSize: 22 }}>{this.state.infoValue[inc]}</td>
                            </tr>
                        })
                    }
                </tbody>
                <button className="logout" onClick={() => this.getHistData()}>Згенерувати історичні дані</button>
            </div>
        ) : (<div></div>);
    }
} export default FAQ; 