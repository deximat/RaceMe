import React, {Component} from 'react';
import {Text, View} from 'react-native';
import Taxi from './components/Taxi';
import RNAndroidLocationEnabler from 'react-native-android-location-enabler';
import { KeepAwake } from 'expo';

class GeolocationExample extends Component {

    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return (
            <View style={{flexGrow: 1, flex: 1}}>
                <Taxi/>
                <KeepAwake />
            </View>
        )
    };
}

export default GeolocationExample;