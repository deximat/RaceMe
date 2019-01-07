import {Component} from "react";
import {Text, View, StyleSheet, FlatList} from "react-native";
import React from "react";
import DistanceTrackerComponent from "./DistanceTrackerComponent";
import {Button, List, ListItem} from 'react-native-elements'
import Styles from "./Styles";

class Taxi extends Component {

    constructor(props) {
        super(props);
        this.state = { distance : 0, state : States.properties[States.Slobodno]};
    }

    render() {
        return (
            <View style={{flexDirection: 'column'}}>
                <Text
                    style={{
                        backgroundColor: this.state.state.color,
                        color: "white",
                        width: "100%",
                        fontSize: 20,
                        paddingTop: 50,
                        textAlign: "center",
                        marginBottom: 5
                    }}
                >Taksimetar</Text>
                <Text
                    style={{
                        backgroundColor: this.state.state.color,
                        color: "white",
                        width: "100%",
                        fontSize: 30,
                        textAlign: "center",
                        marginBottom: 5
                    }}
                >{this.state.distance.toFixed(2)} km</Text>
                <Text
                    style={{
                        backgroundColor: this.state.state.color,
                        color: "white",
                        width: "100%",
                        fontSize: 30,
                        textAlign: "center",
                        marginBottom: 5
                    }}
                >{this.state.state.name}</Text>


                <Text
                    style={{
                        backgroundColor: this.state.state.color,
                        color: "white",
                        width: "100%",
                        fontSize: 40,
                        textAlign: "center",
                        marginBottom: 5

                    }}
                >{this.calculatePrice().toFixed(0)} din</Text>

                {this.state.state.value != States.Placanje && this.state.state.value != States.Tarifa4? (

                <Button title='SLEDECA TARIFA' color="white" backgroundColor={this.state.state.color}
                            style={{margin: 10}} fontSize={10} onPress={() => {
                        console.log("state x : " + this.state.state.value);
                        console.log("placanje : ", States.Placanje);
                        console.log("is placanje : ", this.state.state.value == States.Placanje);
                        console.log("is 5 : ", States.Placanje == 5);

                        this.setState({
                            state: this.state.state.value == States.Placanje ? States.Slobodno : States.properties[this.state.state.value + 1],
                        })
                    }
                    }/>) : null
                }

                {this.state.state.value != States.Placanje && this.state.state.value != States.Slobodno? (

                <Button title='PLACANJE' color="white" backgroundColor={this.state.state.color} fontSize={10} style = {{ margin: 10}}onPress={() => {
                    this.setState({
                        state : {
                            name : "Placanje",
                            value : States.Placanje,
                            start : this.state.state.start,
                            km : this.state.state.km,
                            track : false,
                            color : "gray"
                        },
                        distance : this.state.distance
                    })
                    }
                } />) : null
                }

                {this.state.state.value == States.Placanje  ? (
                <Button title='ZAVRSI' color="white" backgroundColor={this.state.state.color} fontSize={10} style = {{ margin: 10}}onPress={() => {
                    this.setState({
                        state : States.properties[States.Slobodno],
                        distance : 0
                    })
                }
                } />) : null}

                <DistanceTrackerComponent onDistanceChange={

                    (distance) => {

                        console.log("Distance passed: " + distance);
                        console.log("State distance: " + this.state.distance);

                        if (this.state.state.track) {
                            this.setState({distance: (this.state.distance + distance)});
                        }
                    }

                } />

            </View>

        );

    }

    calculatePrice() {
        let state = this.state.state;
        console.log("state: " + state);
        return state.start + state.km * this.state.distance;
    }
}

let styles = StyleSheet.create({
    subtitleView: {
        flexDirection: 'row',
        paddingLeft: 10,
        paddingTop: 5
    },
    ratingImage: {
        height: 19.21,
        width: 100
    },
    ratingText: {
        paddingLeft: 10,
        color: 'grey'
    }
});

let States = {
    Slobodno : 0,
    Tarifa1 : 1,
    Tarifa2 : 2,
    Tarifa3 : 3,
    Tarifa4 : 4,
    Placanje : 5,

    properties: {
        0: {name: "Slobodno", value: 0, start : 0, km : 0, track : false, color : "green"},
        1: {name: "Tarifa 1", value: 1, start : 70, km : 55, track : true, color : "#3B5998"},
        2: {name: "Tarifa 2", value: 2, start : 100, km : 50, track : true, color : "#3B5998"},
        3: {name: "Tarifa 3", value: 3, start : 100, km : 45, track : true, color : "#3B5998"},
        4: {name: "Tarifa 4", value: 4, start : 100, km : 40, track : true, color : "#3B5998"},
        5: {name: "Placanje", value: 5, start : 0, km : 0, track : false, color : "gray"}
    }
};

export default Taxi;