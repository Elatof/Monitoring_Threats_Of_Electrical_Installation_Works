import './App.css';
import MainMap from './map/MainMap'
import Header from './comp/Header';
import Weather from './comp/Weather';
import 'react-notifications/lib/notifications.css';
import { NotificationContainer } from 'react-notifications';



function App() {
  return (
    <div>
      <Header />
      <MainMap />
      <Weather />
      <NotificationContainer />
    </div>
  );
}

export default App;
