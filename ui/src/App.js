import './App.css';
import SignInForm from './auth/SignInForm';
import Header from './comp/Header';
import 'react-notifications/lib/notifications.css';
import { NotificationContainer } from 'react-notifications';
import { BrowserRouter as Router, Redirect, Route, Switch } from 'react-router-dom';
import Main from './Main';



function App() {
  return (
    <div>
      <Router>
        <Header />
        <Switch>
          <Route path='/auth' component={Main} />
          <Route path='/signIn' component={SignInForm} />
          <Route render={() => (<Redirect exact to="/auth" />)} />
        </Switch>
        <NotificationContainer />
      </Router>
    </div>
  );
}

export default App;
