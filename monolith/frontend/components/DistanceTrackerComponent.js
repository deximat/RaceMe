import React, {Component} from 'react';
import {Text, View} from 'react-native';
import geolib from "geolib";
import MapView from 'react-native-maps';
class DistanceTrackerComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            startTime: Date.now(),
            coordinates: [],
            currentDistance: 0
        };
    }

    componentDidMount() {

        // console.log()
        this.myInterval = setInterval(() => {
            console.log("getting cooridnates")
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    console.log("coordinates: " + JSON.stringify(position.coords));
                    let lastPoint = this.state.coordinates[this.state.coordinates.length - 1];
                    let newPoint = {
                        latitude: position.coords.latitude,
                        longitude: position.coords.longitude,
                        latitudeDelta: 0,
                        longitudeDelta: 0
                    };

                    let distancePassed = lastPoint != null ? geolib.getDistance(lastPoint, newPoint) / 1000 : 0;

                    this.setState({
                        coordinates: this.state.coordinates.concat(newPoint),
                        currentDistance: this.state.currentDistance + distancePassed
                    });

                    this.props.onDistanceChange(distancePassed);
                },
                (error) => this.setState({error: error.message}),
                {enableHighAccuracy: true, timeout: 1000, maximumAge: 0},
            );
        }, 10000);
    }

    componentWillUnmount() {
        clearInterval(this.myInterval);
    }

    render() {
        return (<MapView showsUserLocation={true}
                         followsUserLocation={true}></MapView>);
    }
}

export default DistanceTrackerComponent;