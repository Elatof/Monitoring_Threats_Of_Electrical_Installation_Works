import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import Cookies from 'universal-cookie';
import Company from './Company';

class Companies extends Component {

    constructor() {
        super();
        this.state = {
            stor: true,
            items: []
        };

    }

    componentDidMount() {
        let cookie = new Cookies();
        let initialItems = [];
        fetch(`http://localhost:8080/service-api/companies/`, {
            headers: {
                "Authorization": "Bearer " + cookie.get('token')
            }
        }).then(response => { return response.json(); })
            .then(data => {
                initialItems = data.map((Item) => { return Item });
                this.setState({ items: initialItems });
            });
    }

    render() {
        let inc = 0;
        return (
            <div className="mainDivType">
                <div className="heade">Список компаній <Link className="addNewType" to="/auth/comapnies-create/"><img className="add" src="https://res.cloudinary.com/elatof/image/upload/v1667146304/treatment-weather/add_opk2dr.webp" alt="Login logo" width="35" height="25"></img></Link></div>
                <table className='company'>
                    <tbody>
                        {
                            this.state.items.map(item => {
                                inc = inc + 1;
                                return <Company item={item} key={item.id} key2={inc} />
                            })
                        }
                    </tbody>
                </table>
            </div>
        );
    }
}
export default Companies;