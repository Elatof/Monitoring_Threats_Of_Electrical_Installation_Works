import { useRouteMatch, Link, Route, Switch, Redirect } from 'react-router-dom';
import MainMap from './map/MainMap';
import Weather from './comp/Weather';
import 'react-notifications/lib/notifications.css';
import Cookies from 'universal-cookie';
import { useHistory } from "react-router-dom";



function parseJwt(token) {
  const base64 = token.split('.')[1];
  return JSON.parse(window.atob(base64));
}

function visibleLink(user, name) {
  let userAccess = ["brand", "type", "item", "customer", "order"]
  let adminAccess = ["user", "department"]
  let mainAdminAccess = ["admin", "stat"]

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

function visiblecolorPick(user, name) {
  let answer = visibleLink(user, name);

  if (answer) {
    return "on";
  }
  return "off";
}

function Main() {
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
  return (
    <div>
      <div className="userInfo">
        Авторизований користувач: <span className="userInfo">{user.sub}</span>{pr} відділення №:<span className="userInfo">{user.departmentId}</span>
      </div>
      <button className="logOut" onClick={routeChange}>Вийти з системи</button>
      <MainMap />
      <Weather />
    </div>
  );
} export default Main;
