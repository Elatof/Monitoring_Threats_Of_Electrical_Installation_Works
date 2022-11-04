import React, { Component } from 'react';
import { NotificationManager } from 'react-notifications';
import Cookies from 'universal-cookie';

class ComapanyUpdate extends Component {
    constructor() {
        super();
        this.state = {
            id: '',
            name: '',
            logoPath: ''
        }

        this.handleChange = this.handleChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    handleChange(e) {
        let target = e.target;
        this.setState({ [target.name]: target.value });
    }

    componentDidMount() {
        let cookie = new Cookies();
        const Id = this.props.match.params.Id;
        let initialItems = [];
        console.log(Id)
        fetch(`http://localhost:8080/service-api/companies/${Id}`, {
            headers: {
                "Authorization": "Bearer " + cookie.get('token')
            }
        })
            .then(response => {
                return response.json();
            }).then(data => {
                initialItems = data;
                this.setState(data);
            });
    }

    handleImageChange = (e) => {
        this.setState({
            image: e.target.files[0]
        })
    };
    
    onSubmit(e) {
        e.preventDefault();
        const Id = this.props.match.params.Id;
        let form_data = new FormData();
        let cookie = new Cookies();
        if (this.state.image != null) {
            console.log("n");
            form_data.append('file', this.state.image, this.state.image.name);
        }

        fetch(`http://localhost:8080/service-api/companies/?name=${this.state.name}&id=${Id}`, {
            method: "PUT",
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
                window.location.reload();
            }
        }).catch(function () {
            NotificationManager.error('Помилка сервера');
        });
    }

    render() {
        return (
            <div className="updateCustomer">
                <form onSubmit={this.onSubmit}>
                    <b>Обновлення даних по компанії</b>
                    <p></p>
                    Ім'я:
                    <input className="updateCustomer" type="text" id="name" required={true} placeholder="Enter name" name="name" value={this.state.name} onChange={this.handleChange} />
                    <p></p>
                    Лого компанії:
                    <img src={this.state.logoPath} alt="Icon of item" width="100" height="75"></img>
                    <input className="newItem" type="file" id="image" accept="image/png, image/jpeg" onChange={this.handleImageChange} />
                    <p></p>
                    <button className='myButton'>Підтвердити обновлення</button>
                </form>
            </div>
        );
    }
} export default ComapanyUpdate;