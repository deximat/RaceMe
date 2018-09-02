import React, {Component} from 'react';
import {Text, View} from 'react-native';
import MapView from 'react-native-maps';
import LoginComponent from './components/Login';

import geolib from 'geolib'

// import RaceComponent from "./components/Race"
import MainScreenComponent from "./components/MainScreen"
import DistanceTrackerComponent from "./components/DistanceTrackerComponent"
import RaceComponent from "./components/Race";

class GeolocationExample extends Component {

    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return (
            <View style = {{ flexGrow : 1, flex : 1}}>
                {this.state.userData == null ?
                <LoginComponent onSuccess={(data) => this.setState({userData : data})} />
                : <MainScreenComponent userData={this.state.userData}/>}
            </View>
        )
    };
}

export default GeolocationExample;