import { useRouteMatch, Link, Route, Switch, Redirect, Ro } from 'react-router-dom';
import 'react-notifications/lib/notifications.css';
import Cookies from 'universal-cookie';
import { useHistory } from "react-router-dom";
import './Main.css';
import { useState } from "react";
import UserInfo from './comp/UserInfo';
import Common from './comp/Common';
import Companies from './comp/Companies';
import NewCompany from './comp/NewCompany';
import ComapanyUpdate from './comp/CompanyUpdate';
import Admins from './comp/Admins';
import UserDetails from './comp/UserDetails';
import NewUser from './comp/NewUser';
import Users from './comp/Users';
import Popup from 'reactjs-popup';
import FAQ from './comp/FAQ';



function parseJwt(token) {
  const base64 = token.split('.')[1];
  return JSON.parse(window.atob(base64));
}

function visibleLink(user, name) {
  let userAccess = ["user"]
  let adminAccess = ["user", "users"]
  let mainAdminAccess = ["user", "admins", "companies"]

  let answer = false;
  if (user.roles === "USER") {
    userAccess.forEach(element => {
      if (element === name) { answer = true; }
    });
  } else if (user.roles === "ADMIN") {
    adminAccess.forEach(element => {
      if (element === name) { answer = true; }
    });
  } else if (user.roles === "MAIN_ADMIN") {
    mainAdminAccess.forEach(element => {
      if (element === name) { answer = true; }
    });
  }
  return answer;
}

function Main() {
  const [userInfo, setUserInfo] = useState();
  let match = useRouteMatch();

  const history = useHistory();
  const cookies = new Cookies();

  const routeChange = () => {
    const cookies = new Cookies();
    cookies.set('token', ' ', { path: '/', maxAge: 0 });
    let path = `/signIn`;
    history.push(path);
    window.location.reload(false);
  }

  if (!cookies.get('token')) {
    return <Redirect to="/signIn" />
  }
  let user = parseJwt(cookies.get('token'));
  let pr;
  if (user.roles === "USER") {
    pr = ", консультант"
  }
  if (user.roles === "ADMIN") {
    pr = ", адміністратор"
  }
  if (user.roles === "MAIN_ADMIN") {
    pr = ", головний адміністратор"
  }

  if (!userInfo) {
    let cookie = new Cookies();
    fetch(`http://localhost:8080/service-api/users/current`,
      {
        headers: {
          "Authorization": "Bearer " + cookie.get('token')
        }
      })
      .then(response => { return response.json(); })
      .then(data => {
        setUserInfo(data);
      })
  }

  return userInfo ? (
    <div>

      <Popup
        trigger={<img className='additional' src="https://res.cloudinary.com/elatof/image/upload/v1667743454/treatment-weather/additional_lnoj6z.png" alt="Login logo" width="50" height="50"></img> }
        modal
        nested
      >
        {close => (
          <div>
            <div className="heade">Додаткові можливості</div>
            <div style={{fontSize: 18}}>
              {' '}
              <b>Адміністратори</b> мають можливості для додаткових дій з системою працівників які прикріплені до них (створення, видалення ...)
              <br />
              <b>Головний адміністратор</b> має можливість для додаткових дій з системою компаній та їх адміністраторів (створення, видалення ...)
              <br />
              <b>Парцівник</b> має можливість переглянути інформацію лише про себе
            </div>
            <div>
              { visibleLink(user, "user") ? <Link className="f" to={`${match.url}/user-details/` + userInfo.id}>{userInfo.firstName}{userInfo.secondName} особиста інформація</Link> : null }
              { visibleLink(user, "companies") ? <Link className="f" to={`${match.url}/comapnies/`}>Компанії </Link> : null }
              { visibleLink(user, "admins") ? <Link className="f" to={`${match.url}/admins/`}>Адміністратори </Link> : null }
              { visibleLink(user, "users") ? <Link className="f" to={`${match.url}/users/`}>Працівники </Link> : null }
              <Link className="f" to={`${match.url}/FAQ/`}>FAQ </Link>
            </div>
          </div>
        )}
      </Popup>

      <div className="userInfo">
        <img className="companyIcon" src={userInfo.company.logoPath} alt="Icon of company" width="100" height="75"></img>
        <Link className="userInfo" to={`${match.url}/user-details/` + userInfo.id}>{userInfo.firstName}<br></br>{userInfo.secondName}</Link>
        <button className="logout" onClick={routeChange}>Вийти з системи</button>
      <Link className="map" to={`${match.url}/auth`}><img src="https://res.cloudinary.com/elatof/image/upload/v1667127579/treatment-weather/world-map_1f5fa-fe0f_w51trv.png" alt="Login logo" width="50" height="50"></img></Link>
      </div>
      <Switch>
        <Route path={`${match.url}/user-details/:Id`} component={() => <UserInfo {...userInfo} />} />
        <Route path={`${match.url}/comapnies/`} component={Companies} />
        <Route path={`${match.url}/comapnies-create/`} component={NewCompany} />
        <Route path={`${match.url}/company-update/:Id`} component={ComapanyUpdate} />
        <Route path={`${match.url}/admins-details/:Id`} component={UserDetails} />
        <Route path={`${match.url}/admins/`} component={Admins} />
        <Route path={`${match.url}/admins-create/`} component={() => <NewUser userType={2} />} />
        <Route path={`${match.url}/users/`} component={Users} />
        <Route path={`${match.url}/users-create/`} component={() => <NewUser userType={1} />} />
        <Route path={`${match.url}/FAQ/`} component={FAQ} />
        <Route path={`/auth`} component={Common} />
      </Switch>
    </div>
  ) : (
    <div>
      Loading ...
    </div>
  );
} export default Main;
