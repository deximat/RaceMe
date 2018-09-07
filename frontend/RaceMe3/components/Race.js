import {Component} from "react";
import {Text, View, StyleSheet, FlatList} from "react-native";
import React from "react";
import DistanceTrackerComponent from "./DistanceTrackerComponent";
import {Button, List, ListItem} from 'react-native-elements'
import Styles from "./Styles";
import MainServer from "./MainServer";


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
            data.runners = data.runners.sort((a, b) => b.distance - a.distance);
            this.callback(data);
        });
    }

    subscribeToRaceUpdates(callback) {
        this.callback = callback;
    }

}

class RaceComponent extends Component {

    constructor(props) {
        super(props);
        console.log("initial state: " + JSON.stringify(this.props.initialState));
        this.state = this.props.initialState;

        this.race = new MultiplayerRace(this.props.userId, this.props.initialState.id, MainServer.address, 8080);
        this.race.subscribeToRaceUpdates((state) => this.setState(state));


        this.renderRow = this.renderRow.bind(this);

        // this.state = {
        //     runners : props.runners}
        // setInterval(() => {
        //     this.onRaceUpdate();
        // }, 1000);
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
        let myPosition = this.calculatePosition(this.props.userId);

        if (myPosition >= this.state.runners.length) {
            return null;
        }

        let hisPosition = myPosition + 1;
        return this.state.runners[hisPosition - 1];
    }

    getFasterThanMe() {
        let myPosition = this.calculatePosition(this.props.userId);

        if (myPosition <= 1) {
            return null;
        }

        let hisPosition = myPosition - 1;
        return this.state.runners[hisPosition - 1];
    }

    getMe() {
        let me = this.state.runners.find((value) => value.id == this.props.userId);
        console.log("THIS IS ME: " + JSON.stringify(me));
        return me;
    }


    calculateBarSize(who) {
        if (who == null) {
            return 30;
        }

        let percent = who.distance / MAX_DISTANCE
        return Math.max(percent * MAX_BAR_SIZE, 30);
    }

    results() {
        return (<View style = {{  borderWidth: 5, padding: 10, margin: 20,  borderColor : Styles.mainColor,  backgroundColor: "white", position : "absolute", left : 0, right: 0, top : 250, zIndex: 2}}>
            <Text style = {{ fontSize : 30, textAlign: "center"}}>Finished race!</Text>
            <List>
                <FlatList
                    data={this.state.runners}
                    renderItem={this.renderRow}
                    keyExtractor={item => item.username}
                />
            </List>
            <Button title="Done" backgroundColor={Styles.mainColor} onPress={() => this.props.battleFinished()} style = {{ marginTop : 50}}></Button>
        </View>);
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
                >Time: {this.getStringifiedTime(this.getMe())}</Text>
                <Text
                    style={{
                        position: "absolute",
                        left: 0,
                        top: 200,
                        backgroundColor: "#3B5998",
                        color: "white",
                        width: "100%",
                        height: 50,
                        fontSize: 20,
                        textAlign: "center"
                    }}
                >{this.getMe().username}</Text>

                <View style={{backgroundColor : "red", position : "absolute", alignItems : "stretch", left: 0, right: 0, top: 200, bottom: 0}}>
                    <View style = {{ flex: 1, backgroundColor : "purple" }}></View>
                </View>

                <View style={{flex: 1, flexDirection: 'row', position: "absolute", top: 250, bottom: 0, left : 0, right: 0}}>
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
                            >{this.getSlowerThanMe() != null ? this.getSlowerThanMe().username : "" }</Text>
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
                        >{this.getMe().username}</Text>
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
                            >{this.getFasterThanMe() != null ? this.getFasterThanMe().username : "" }</Text>
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
                            >{this.getFasterThanMe().distance.toFixed(2) + "km"}</Text>
                        </View>) : null}
                </View>

                <DistanceTrackerComponent onDistanceChange={(distance) => this.race.sendDistance(distance)} />


                {this.getMe().finished ? this.results() : null}
            </View>

        );
    }

    getFullTimeInSeconds(person) {
        return Math.floor((person.finishedAt - this.state.startedAt) / 1000);
    }

    getTimeSeconds(person) {
        return this.getFullTimeInSeconds(person) % 60;
    }

    getTimeMinutes(person) {
        return Math.floor(this.getFullTimeInSeconds(person) / 60);
    }

    twoDigitFormat(n){
        return n > 9 ? "" + n: "0" + n;
    }

    getStringifiedTime(person) {
        return this.twoDigitFormat(this.getTimeMinutes(person)) +  ":" + this.twoDigitFormat(this.getTimeSeconds(person));
    }

    renderRow ({ item, index }) {
        return (
            <ListItem
                title={(index + 1) + "." + item.username + " " + item.distance.toFixed(2) + "km"}
                subtitle={(item.finished ? "Finish time: " : "Still running time:") + " " + this.getStringifiedTime(item)}
            />
        )
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

export default RaceComponent;