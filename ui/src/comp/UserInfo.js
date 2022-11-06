import React from 'react';
import {withRouter} from 'react-router';

function UserInfo(props) {
    
    return (
        <div>
            <div className="heade">Інформація про користувача {props.firstName} {props.secondName}</div>
            <b>Індитифікаитор:</b> {props.id} <br />
            <b>Поштова скринька:</b> {props.email} <br />
            <b>Телефон:</b> {props.phone} <br />
            <b>День народження:</b> {props.dayOfBirth} <br />
            <b>Позиція:</b> {props.title} <br />
        </div>
    );
}

export default withRouter(UserInfo);
