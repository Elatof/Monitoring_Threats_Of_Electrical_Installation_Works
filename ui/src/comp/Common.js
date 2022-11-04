import React from 'react';
import {withRouter} from 'react-router';
import MainMap from '../map/MainMap';
import Weather from './Weather';


function Common() {
    return (
        <div>
            <MainMap/>
            <Weather/>
        </div>
    );
}

export default withRouter(Common);
