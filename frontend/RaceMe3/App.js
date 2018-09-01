import React, {Component} from 'react';
import {Text, View} from 'react-native';
import MapView from 'react-native-maps';

import geolib from 'geolib'

// import RaceComponent from "./components/Race"
import MainScreenComponent from "./components/MainScreen"
import DistanceTrackerComponent from "./components/DistanceTrackerComponent"
import RaceComponent from "./components/Race";

class GeolocationExample extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <View style = {{ flexGrow : 1, flex : 1}}>

                // TODO: investigate why get location doesn't work without mapview

                <MapView
                    style={{
                        width: 0,
                        height: 0
                    }}
                    showsUserLocation={true}
                    followsUserLocation={true}
                >
                </MapView>
                <RaceComponent/>
            </View>
        )
    };
}

export default GeolocationExample;