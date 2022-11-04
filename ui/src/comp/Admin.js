import React from 'react';
import { Link } from 'react-router-dom';

function Admin(props) {
    const item = props.item;
    
    return (
        <tr className= "company">
            <td>{props.key2} |</td>
            <td><img src={item.company.logoPath} alt="Icon of item" width="100" height="75"></img></td>
            <td><div className="des">{item.company.name}</div></td>
            <td><div className="des">{item.firstName}</div></td>
            <td><div className="des">{item.secondName}</div></td>
            <td><Link f = {item.id} className="updateLink" to ={"/auth/admins-details/" + item.id}>Детальніше</Link></td>
        </tr>
    );
}

export default Admin;
 