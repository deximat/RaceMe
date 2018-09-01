import React, {Component} from 'react';
import {Text, View} from 'react-native';
import geolib from "geolib";

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
        this.myInterval = setInterval(() => {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    console.log(JSON.stringify(position.coords));
                    let lastPoint = this.state.coordinates[this.state.coordinates.length - 1];
                    let newPoint = {
                        latitude: position.coords.latitude,
                        longitude: position.coords.longitude,
                        latitudeDelta: 0,
                        longitudeDelta: 0
                    };

                    let timePassed = (Date.now() - this.state.startTime) / 1000;

                    let distancePassed = lastPoint != null ? geolib.getDistance(lastPoint, newPoint) / 1000 : 0;
                    // console.log("distance passed: " + distancePassed);

                    console.log("last point: " + JSON.stringify(lastPoint) + " newPoint: " + JSON.stringify(newPoint));
                    console.log("Has two distance points: " + (lastPoint != null));
                    console.log("tracked new dot, distance: " + this.state.currentDistance + "speed:" + position.coords.speed);


                    this.setState({
                        coordinates: this.state.coordinates.concat(newPoint),
                        currentDistance: this.state.currentDistance + distancePassed
                    });

                    console.log("distance change: " + distancePassed);
                    // call callback
                    this.props.onDistanceChange(distancePassed);
                },
                (error) => this.setState({error: error.message}),
                {enableHighAccuracy: true, timeout: 1000, maximumAge: 0},
            );
        }, 1000);
    }

    componentWillUnmount() {
        clearInterval(this.myInterval);
    }

    render() {
        return (<View></View>);
        // return (
        //         //   <View style = {{
        //         //       flex : 1,
        //         //       flexGrow : 1,
        //         //       alignItems: 'center',
        //         //       justifyContent: 'center'
        //         //   }}>
        //         //
        //         //       <Text>{this.state.currentDistance}km</Text>
        //         //   </View>
        //         // );
    }
}

export default DistanceTrackerComponent;