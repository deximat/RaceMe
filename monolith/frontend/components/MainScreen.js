import React, {Component} from 'react';
import {ActivityIndicator, Button, Text, View} from "react-native";
import MapView, {Polyline} from 'react-native-maps';

import geolib from "geolib";
import MainServer from "./MainServer";
import RaceComponent from "./Race";
import update from 'react-addons-update'; // ES6

const IDLE_STATE = 0;
const LOOKING_FOR_OPPONENT_STATE = 1;
const RUNNING_STATE = 2;

class MainScreenComponent extends Component {


    constructor(props) {
        super(props);

        this.state = {
            currentRace: null,
            userData: props.userData,
            appState: IDLE_STATE,
        };

    }

    mainScreen() {
        return (
            <View style={{flexGrow: 1, alignItems: 'center', justifyContent: 'center'}}>
                <MapView
                    ref={(el) => (this.map = el)}
                    style={{
                        position: "absolute",
                        left: 0,
                        right: 0,
                        top: 0,
                        bottom: 0
                    }}
                    showsUserLocation={true}
                    followsUserLocation={true}
                >
                </MapView>

                <Text style={{
                    position: "absolute",
                    left: 0,
                    top: 0,
                    backgroundColor: "#3B5998",
                    color: "white",
                    width: "100%",
                    height: 150,
                    fontSize: 60,
                    paddingTop: 50,
                    textAlign: "center"
                }}
                >RaceMe 3.0</Text>

                <Text
                    style={{
                        position: "absolute",
                        left: 0,
                        top: 180,
                        backgroundColor: "#3B5998",
                        color: "white",
                        width: "100%",
                        height: 50,
                        fontSize: 20,
                        textAlign: "center",
                        padding: 10
                    }}
                >Welcome back {this.state.userData.username}!</Text>

                <Text
                    style={{
                        position: "absolute",
                        left: 0,
                        top: 240,
                        backgroundColor: "#3B5998",
                        color: "white",
                        width: "100%",
                        height: 30,
                        fontSize: 15,
                        textAlign: "center",
                        padding: 5
                    }}
                > Rating: {this.state.userData.rating}</Text>

                {
                    this.state.appState == IDLE_STATE ?
                        this.idleState() : null
                }

                {
                    this.state.appState == LOOKING_FOR_OPPONENT_STATE ?
                        this.lookingForOpponentState() : null
                }

                <Text
                    style={{
                        position: "absolute",
                        left: 0,
                        bottom: 0,
                        backgroundColor: "#3B5998",
                        color: "white",
                        width: "100%",
                        height: 80,
                        fontSize: 10,
                        paddingBottom: 50,
                        paddingTop: 10,
                        textAlign: "center"
                    }}
                >the running app you have been waiting for</Text>
                <Text
                    style={{

                        position: "absolute",
                        left: 0,
                        bottom: 0,
                        backgroundColor: "#3B5998",
                        color: "white",
                        width: "100%",
                        height: 50,
                        fontSize: 10,
                        paddingTop: 10,
                        textAlign: "center"
                    }}
                >Codlex</Text>
            </View>

        );
    }

    render() {
        return (
            <View style={{flexGrow: 1, flex: 1}}>
                {this.state.currentRace == null ?
                    this.mainScreen()
                    : <RaceComponent userId={this.state.userData.id} initialState={this.state.currentRace}
                                     battleFinished={
                                         (reward) => {
                                             this.setState({currentRace: null, appState: IDLE_STATE, userData : update(this.state.userData, {rating : { $set : Math.max(0, this.state.userData.rating + reward)} }) });
                                         }
                                     }/>
                }
            </View>
        );

    }


    idleState() {
        return (
            <View
                style={{
                    width: "80%",
                    height: 50,
                    position: "absolute",
                    top: 500,
                    backgroundColor: "white",
                    alignItems: 'center',
                    justifyContent: 'center',
                    borderColor: "#3B5998",
                    borderWidth: 3
                }}
            >
                <Button
                    title="Find 5k RACE"
                    onPress={() => {
                        this.setState({appState: LOOKING_FOR_OPPONENT_STATE});
                        this.interval = setInterval(() => {
                            MainServer.fetch("/find-race-service/race/find/user/" + this.state.userData.id, {}, (response) => {
                                if (response.status == "Waiting") {
                                    return;
                                }

                                if (response.status == "Running") {
                                    console.log("Starting battle.");
                                    this.setState({currentRace: response});
                                    clearInterval(this.interval);
                                    return;
                                }

                                console.log(JSON.stringify(response));
                            });
                        }, 1000);
                    }}

                />
            </View>
        );
    }

    lookingForOpponentState() {
        return (
            <View style={{
                position: "absolute",
                left: 0, width: "100%",
                height: 200,

            }}>

                <Text style={{

                    backgroundColor: "#3B5998",
                    color: "white",
                    fontSize: 25,
                    paddingTop: 120,
                    paddingBottom: 20,
                    textAlign: "center",
                }}
                >Looking for a worthy runner...</Text>

                <ActivityIndicator size="large" color="white" style={{zIndex: 2, top: -120}}/>
                <Button title="Cancel" onPress={() => this.cancelFind()}/>

            </View>
        );
    }

    cancelFind() {
        MainServer.fetch("/find-race-service/race/cancel/user/" + this.state.userData.id, {}, (response) => {
            if (response) {
                this.setState({appState: IDLE_STATE});
                clearInterval(this.interval);
            }
        });
    }

}

export default MainScreenComponent;