import React, {Component} from 'react';
import {Text, View} from 'react-native';
import LoginComponent from './components/Login';
import RNAndroidLocationEnabler from 'react-native-android-location-enabler';
import MainScreenComponent from "./components/MainScreen";

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