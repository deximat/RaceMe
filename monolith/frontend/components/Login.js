import React, {Component} from 'react'
import {
    StyleSheet,
    KeyboardAvoidingView,
    View,
    ActivityIndicator,
    TouchableOpacity,
    Image,
    TextInput,
    Text
} from 'react-native';
import Styles from './Styles'
import { Button, Icon } from 'react-native-elements';
import MapView from 'react-native-maps';
import MainServer from './MainServer';


class LoginComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            username : null
        };
    }

    login() {
        console.log("Sta sad: " + this.state.username);
        MainServer.fetch("login/" + this.state.username, {}, (response) => {
            console.log("Logged in: " + JSON.stringify(response));
            this.props.onSuccess(response);
        })
    }

    render() {
        return (<KeyboardAvoidingView behavior="padding" style = {{flex: 1, backgroundColor: Styles.mainColor, justifyContent : "flex-end", alignItems: "center", flexDirection: "column", padding: 50 }}>
            <Text style = {{ height: 200,  color: "white", margin : 0, fontSize : 50, position: "absolute", top: 50,}} >Race Me 3.0</Text>
            <MapView
                showsUserLocation={true}
                followsUserLocation={true}
                style = {{ height : 300, width: 500, position: "absolute", top : 150}}>
            </MapView>
            <Text style = {{ height: 50, fontSize : 30,  color: "white", margin : 0}} >Hello Runner!</Text>
            <Text style = {{ height: 50, fontSize : 20,  color: "white", margin : 0}} >Username:</Text>
            <TextInput
                onChangeText={(username) => {
                    this.setState({ username: username});
                    console.log("setting username: " + username);
                }}
                style = {{ width: 10000, maxWidth: "100%", height: 30, backgroundColor : "white", marginBottom: 30, padding : 2}}
                defaultValue="Deximat"
            />

            <Button
                large
                rightIcon={{name: 'wifi'}}
                title='Login'
                backgroundColor="#4267b2"
                color = "white"
                style = {{
                    width: 10000,
                    maxWidth : "100%",
                    marginBottom : 100
                }}
                onPress={() => this.login()}
            />


        </KeyboardAvoidingView>);
    }

}

export default LoginComponent;