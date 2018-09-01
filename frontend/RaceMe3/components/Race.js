import {Component} from "react";
import {Text, View} from "react-native";
import React from "react";
import DistanceTrackerComponent from "./DistanceTrackerComponent";

var idGenerator = 0;

const runners_COUNT = 6;
const MAX_BAR_SIZE = 400;
const MAX_DISTANCE = 5;


class MultiplayerRace {

    constructor(userId, raceId, address, port) {
        this.address = address;
        this.port = port;
        this.raceId = raceId;
        this.userId = userId;
    }

    sendMessage(endpoint, data, callback) {
        console.log("sending message to path: " + endpoint);
        fetch('http://' + this.address + ":" + this.port + "/" + endpoint, {
            method: 'POST',
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        }).then((response) => response.json())
            .then(callback)
            .catch((error) => console.log("ERROR:" + error));
    }

    sendDistance(distance) {
        this.sendMessage("/race/" + this.raceId + "/user/" + this.userId + "/distance/" + distance, {},  (data) => {
            this.callback(data);
        });
    }

    subscribeToRaceUpdates(callback) {
        this.callback = callback;
    }

}

class RaceComponent extends Component {

    generateUserId() {
        return idGenerator++;
    }


    generateName(id) {
        var names = ["Dejan", "Marko", "Pera", "Aca", "Mika", "ProRunner"];
        return names[id];
    }

    generaterunners(runnersCount) {
        let runners = [];

        for (var i = 0; i < runnersCount; i++) {
            let generatedId = this.generateUserId();
            runners.push({
                id : generatedId,
                distance : 0,
                name : this.generateName(generatedId)
            });
        }

        return runners;
    }

    constructor(props) {
        super(props);
        this.race = new MultiplayerRace(0, 1, "localhost", 8080);
        this.race.sendDistance(0);
        this.race.subscribeToRaceUpdates((state) => this.setState(state));

        this.state = {
            id : 0,
            runners : [ { id : 0, distance : 0}]
        };
        this.state.myUserId = 0;

        // this.state = {
        //     runners : props.runners}
        // setInterval(() => {
        //     this.onRaceUpdate();
        // }, 1000);
    }

    fakeUpdaterunners(runners) {
        let newrunners = [];
        let SPEED = 0.2; // this is just for testing purposes, 5k should last around 3 min
        for (let i = 0; i < runners.length; i++) {
            let player = runners[i];
            newrunners.push({
                id : player.id,
                name : player.name,
                distance : Math.min(MAX_DISTANCE, player.distance + Math.random() * SPEED)
            })
        }

        newrunners.sort((a, b) => b.distance - a.distance);

        return newrunners;
    }

    onRaceUpdate() {
        // generate new state

        let newState = {
            id : this.state.myUserId,
            runners : this.fakeUpdaterunners(this.state.runners)
        };

        // apply state
        this.setState(newState);
    }


    calculatePosition(id) {
        return this.state.runners.findIndex((value) => value.id == id) + 1;
    }

    calculatePositionStringified(who) {

        let position = this.calculatePosition(who.id);

        switch (position) {
            case 1:
                return "1st";
            case 2:
                return "2nd";
            case 3:
                return "3rd";
            default:
                return position + "th";

        }
        return position;
    }

    getSlowerThanMe() {
        let myPosition = this.calculatePosition(this.state.myUserId);

        if (myPosition >= runners_COUNT) {
            return null;
        }

        let hisPosition = myPosition + 1;
        return this.state.runners[hisPosition - 1];
    }

    getFasterThanMe() {
        let myPosition = this.calculatePosition(this.state.myUserId);

        if (myPosition <= 1) {
            return null;
        }

        let hisPosition = myPosition - 1;
        return this.state.runners[hisPosition - 1];
    }

    getMe() {
        return this.state.runners.find((value) => value.id == this.state.myUserId);
    }


    calculateBarSize(who) {
        if (who == null) {
            return 30;
        }

        let percent = who.distance / MAX_DISTANCE
        return Math.max(percent * MAX_BAR_SIZE, 30);
    }

    render() {
        return (
            <View>
                <Text
                    style={{
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
                >{this.calculatePositionStringified(this.getMe())}</Text>
                <Text
                    style={{
                        position: "absolute",
                        left: 0,
                        top: 120,
                        backgroundColor: "#3B5998",
                        color: "white",
                        width: "100%",
                        height: 50,
                        fontSize: 20,
                        textAlign: "center"
                    }}
                >Distance: {this.getMe().distance.toFixed(2)}</Text>
                <Text
                    style={{
                        position: "absolute",
                        left: 0,
                        top: 160,
                        backgroundColor: "#3B5998",
                        color: "white",
                        width: "100%",
                        height: 50,
                        fontSize: 20,
                        textAlign: "center"
                    }}
                >{this.getMe().name}</Text>

                <View style={{backgroundColor : "red", position : "absolute", alignItems : "stretch", left: 0, right: 0, top: 200, bottom: 0}}>
                    <View style = {{ flex: 1, backgroundColor : "purple" }}></View>
                </View>

                <View style={{flex: 1, flexDirection: 'row', position: "absolute", top: 210, bottom: 0, left : 0, right: 0}}>
                    {this.getSlowerThanMe() != null ?
                        <View style={{flex : 1, height: 500, backgroundColor: 'white', position : "relative", alignItems: 'center', justifyContent: 'flex-end'}} >
                            <Text
                                style = {{
                                    backgroundColor : "#FD6A02",
                                    width : "100%",
                                    height : this.calculateBarSize(this.getSlowerThanMe()),
                                    textAlign : "center",
                                    fontSize : 20,
                                    color : "white"
                                }}
                            >{this.getSlowerThanMe() != null ? this.getSlowerThanMe().name : "" }</Text>
                            <Text
                                style = {{
                                    backgroundColor : "#FD6A02",
                                    width : "100%",
                                    height : 30,
                                    textAlign : "center",
                                    fontSize : 30,
                                    color : "white"
                                }}
                            >{this.calculatePositionStringified(this.getSlowerThanMe())}</Text>
                            <Text
                                style = {{
                                    backgroundColor : "#FD6A02",
                                    width : "100%",
                                    height : 30,
                                    textAlign : "center",
                                    fontSize : 15,
                                    color : "white"
                                }}
                            >{this.getSlowerThanMe() != null ? this.getSlowerThanMe().distance.toFixed(2) + "km" : "" }</Text>
                        </View> : null}

                    <View style={{flex : 1, height: 500,backgroundColor: 'powderblue', alignItems: 'center', justifyContent: 'flex-end'}}>
                        <Text
                            style = {{
                                backgroundColor : "#3B5998",
                                width : "100%",
                                height : this.calculateBarSize(this.getMe()),
                                textAlign : "center",
                                fontSize : 20,
                                color : "white"
                            }}
                        >{this.getMe().name}</Text>
                        <Text
                            style = {{
                                backgroundColor : "#3B5998",
                                width : "100%",
                                height : 30,
                                textAlign : "center",
                                fontSize : 30,
                                color : "white"
                            }}
                        >{this.calculatePositionStringified(this.getMe())}</Text>
                        <Text
                            style = {{
                                backgroundColor : "#3B5998",
                                width : "100%",
                                height : 30,
                                textAlign : "center",
                                fontSize : 15,
                                color : "white"
                            }}
                        >{this.getMe().distance.toFixed(2) + "km"}</Text>
                    </View>

                    {this.getFasterThanMe() != null ? (
                        <View style={{flex : 1, height: 500, backgroundColor: 'white', position : "relative", alignItems: 'center', justifyContent: 'flex-end'}} >
                            <Text
                                style = {{
                                    backgroundColor : "green",
                                    width : "100%",
                                    height : this.calculateBarSize(this.getFasterThanMe()),
                                    textAlign : "center",
                                    fontSize : 20,
                                    color : "white"
                                }}
                            >{this.getFasterThanMe() != null ? this.getFasterThanMe().name : "" }</Text>
                            <Text
                                style = {{
                                    backgroundColor : "green",
                                    width : "100%",
                                    height : 30,
                                    textAlign : "center",
                                    fontSize : 30,
                                    color : "white"
                                }}
                            >{this.calculatePositionStringified(this.getFasterThanMe())}</Text>
                            <Text
                                style = {{
                                    backgroundColor : "green",
                                    width : "100%",
                                    height : 30,
                                    textAlign : "center",
                                    fontSize : 15,
                                    color : "white"
                                }}
                            >{this.getMe().distance.toFixed(2) + "km"}</Text>
                        </View>) : null}
                </View>

                <DistanceTrackerComponent onDistanceChange={(distance) => this.race.sendDistance(distance)} />

            </View>

        );
    }


}

export default RaceComponent;