import React, { Component } from 'react';
import { NotificationManager } from 'react-notifications';
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import Cookies from 'universal-cookie';

class NewUser extends Component {
    constructor() {
        super();
        this.state = {
            user: {},
            companies: [],
            userTypeDesc: "користувача"
        }

        this.onSubmit = this.onSubmit.bind(this);
    }

    componentDidMount() {
        let cookie = new Cookies();
        let initialItems = [];
        fetch('http://localhost:8080/service-api/companies/', {
            headers: {
                "Authorization": "Bearer " + cookie.get('token')
            }
        })
            .then(response => { return response.json(); })
            .then(data => {
                initialItems = data.map((company) => { return company });
                this.setState({ companies: initialItems });
            });
    }

    onSubmit(e) {
        e.preventDefault();
        if (this.state.user.company === null) {
            NotificationManager.warning("Ви обов'язково маєте вибрати компанію");
            return;
        }

        let userType = this.props.userType;
        let url = "http://localhost:8080/service-api/users/";
        this.state.user.isAdmin = userType;
        if (userType === 2) { //Admin
            url = url + `admin`;
            this.setState({ userTypeDesc: "адміністратора" });
        }

        fetch(url, {
            method: "POST",
            body: JSON.stringify(this.state.user),
            headers: {
                "Authorization": "Bearer " + new Cookies().get('token'),
                "content-type": "application/json"
            }
        }).then(function (response) {
            if (response.status === 500) {
                NotificationManager.error('Помилка сервера');
            }
            if (response.status === 400) {
                NotificationManager.warning('Помилка вхідних даних, повторіть спробу.');
            }
            if (response.status === 200) {
                NotificationManager.success('Новий користувач добавленний');
            }
        }).catch(function () {
            NotificationManager.error('Помилка сервера');
        });
    }

    render() {
        console.log(this.state.comapanies)
        let companyNames = this.state.companies.map((company) => { return company.name });
        let handleCompany = (e) => {
            let handleCompany;
            this.state.companies.forEach((company) => { if (e.value === company.name) handleCompany = company; });
            this.state.user.company = handleCompany;
        }

        return (
            <div className="newItem">
                <form onSubmit={this.onSubmit}>
                    <div className="heade">Cтворення нового {this.state.userTypeDesc}</div>
                    <p />
                    <input className="newItem" type="text" id="name" required={true} placeholder="Введіть ім'я" name="name" onChange={(e) => this.state.user.firstName = e.target.value} />
                    <p />
                    <input className="newItem" type="text" id="secondName" required={true} placeholder="Введіть прізвище" name="secondName" onChange={(e) => this.state.user.secondName = e.target.value} />
                    <p />
                    <input className="newItem" type="password" id="password" required={true} placeholder="Введіть пароль" name="password" onChange={(e) => this.state.user.password = e.target.value} />
                    <p />
                    <input className="newCustomer" type="tel" id="phone" required={true} placeholder="Введіть мобільний телефон" pattern="^[+][0-9]{12}$" name="phone" onChange={(e) => this.state.user.phone = e.target.value} />
                    <p></p>
                    Приклад формату: +380982561299
                    <p></p>
                    <input className="newCustomer" type="email" id="email" required={true} placeholder="Введіть почтову скриньку" name="email" onChange={(e) => this.state.user.email = e.target.value} />
                    <p />
                    Дата народження:
                    <input className="newOrder" type="date" id="dayOfBirth" required={true} placeholder="Enter Date" name="dayOfBirth" onChange={(e) => this.state.user.dayOfBirth = e.target.value} />
                    <p />
                    <input className="newItem" type="text" id="title" required={true} placeholder="Введіть посаду" name="title" onChange={(e) => this.state.user.title = e.target.value} />
                    <p />
                    <Dropdown className="dropDown" options={companyNames} onChange={handleCompany} placeholder="Виберіть компанію" />
                    <p />
                    <p />
                    <button className='myButton'>Підтвердити добавлення</button>
                </form>
            </div>
        );
    }
} export default NewUser;