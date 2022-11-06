import React, { Component } from 'react';
import { NotificationManager } from 'react-notifications';
import Cookies from 'universal-cookie';
import { Link } from 'react-router-dom';

class UserDetails extends Component {

    constructor(props) {
        super();
        this.state = {
            user: {
                
            }
        };

        fetch(`http://localhost:8080/service-api/users/` + props.match.params.Id, {
            headers: {
                "Authorization": "Bearer " + new Cookies().get('token')
            }
        }).then(response => {
            return response.json();
        }).then(data => {
            this.setState({
                user: data,
            });
        });

        this.deleteElem = this.deleteElem.bind(this);
    }

    deleteElem(e) {
        e.preventDefault();
        let answer = window.confirm("Ви видаляєте користувача " + this.state.user.firstName + " " + this.state.user.secondName)
        if (answer) {
            fetch(`http://localhost:8080/service-api/users/${this.state.user.id}`, {
                method: 'DELETE',
                headers: {
                    "Authorization": "Bearer " + new Cookies().get('token')
                }
            }).then(function (response) {
                if (response.status === 500) {
                    NotificationManager.error('Помилка сервера');
                }
                if (response.status === 200) {
                    NotificationManager.success('Успішне видалення');
                }
            });
            this.props.history.push('/auth');
        }
    }



    render() {
        console.log(this.state.user);
        return (
            <div className="mainDivDetailsItem">
                <div className="heade">Детальна інформація про користувача {this.state.user.firstName} {this.state.user.secondName}</div>
                <b>Унікальне айді: </b>{this.state.user.id}<br />
                <b>Ім'я: </b>{this.state.user.firstName}<br />
                <b>Прізвище: </b>{this.state.user.secondName}<br />
                <b>Телефон: </b>{this.state.user.phone}<br />
                <b>Поштова скринька: </b>{this.state.user.email}<br />
                <b>День народження: </b>{this.state.user.dayOfBirth}<br />
                <b>Посада: </b>{this.state.user.title}<br />
                <button className="delete" onClick={this.deleteElem}>Видалити</button>
                <Link className="updateLink" to={"/auth/items-update/" + this.state.user.id}>Обновити</Link>
            
            </div>
        );
    }
} export default UserDetails; 