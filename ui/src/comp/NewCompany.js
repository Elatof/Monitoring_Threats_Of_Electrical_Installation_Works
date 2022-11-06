import React, { Component } from 'react';
import { NotificationManager } from 'react-notifications';
import Cookies from 'universal-cookie';
import './NewCompany.css'

class NewCompany extends Component {
    constructor() {
        super();
        this.state = {
            name: ''
        }

        this.handleChange = this.handleChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    handleImageChange = (e) => {
        this.setState({
            image: e.target.files[0]
        })
    };

    handleChange(e) {
        let value = e.target.value;
        this.state.name = value;
    }

    onSubmit(e) {
        console.log(this.state)
        e.preventDefault();

        let form_data = new FormData();
        let cookie = new Cookies();
        form_data.append('file', this.state.image, this.state.image.name);

        fetch(`http://localhost:8080/service-api/companies/?name=${this.state.name}`, {
            method: "POST",
            body: form_data,
            headers: {
                "Authorization": "Bearer " + cookie.get('token')
            }
        }).then(function (response) {
            if (response.status === 500) {
                NotificationManager.error('Помилка сервера');
            }
            if (response.status === 400) {
                NotificationManager.warning('Помилка вхідних даних, повторіть спробу.');
            }
            if (response.status === 200) {
                NotificationManager.success('Нове компанія добавленнна');
            }
        }).catch(function () {
            NotificationManager.error('Помилка сервера');
        });
    }

    render() {
        return (
            <div className="newItem">
                <div className="heade">Створення нової компанії</div>
                <form onSubmit={this.onSubmit}>
                    <p />
                    <input className="newItem" type="text" id="name" required={true} placeholder="Введіть назву" name="name" onChange={this.handleChange} />
                    <p />
                    <input className="newItem" type="file" id="image" required={true} accept="image/png, image/jpeg" onChange={this.handleImageChange} />
                    <p />
                    <button className='myButton'>Підтвердити добавлення</button>
                </form>
            </div>
        );
    }
} export default NewCompany;