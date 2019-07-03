
import React,{Component}from 'react';
import { View,TextInput,Text,ViewPagerAndroid, StyleSheet,Dimensions,Button} from "react-native";

import MapView,{Marker,Callout,CalloutSubview, PROVIDER_GOOGLE} from 'react-native-maps';

import axios from 'axios';

//import Button from 'react-native-button';
//import CustomCallout from './components/CustomCallout';
const {width, height}=Dimensions.get('window');
class Form extends Component{
    constructor(props) {
        super(props);
        this.state={
            coordinates:{},
            region: {
                //manuel coordinates
                latitude:43,
                longitude:25,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
              },
              text:null,  //aldıgimiz text
              new_message_pin:{latlng:{latitude:0,longitude:0}}, //markers array,
              messages:[]
        };
        //fonksiyonlarda olusturulması gereken yapılar.bind -> fonksiyonun cagirilmasiyla ilgili
        this.loadAllMessages=this.loadAllMessages.bind(this);
        this.addMarker=this.addMarker.bind(this);
        this.click=this.click.bind(this);
        this.onRegionChange=this.onRegionChange.bind(this);
        //
        this.loadAllMessages();
    }
    render(){
        return(
        //flex Direction ->hizalama
        <View style={{flex:1,flexDirection:'column' }}>  
            <View style={{flex: 6,}}>
                <MapView
                        style={{...StyleSheet.absoluteFillObject }}
                        region={this.state.region}
                        //onRegionChange={this.onRegionChange}
                        onPress={(e) => this.addMarker(e.nativeEvent.coordinate)}>

                        {
                        this.state.messages.map((marker, i) => (
                            <MapView.Marker key={i}
                                coordinate={marker.location} 
                                title={marker.message}
                            />
                                
                        ))
                        }
                            <MapView.Marker 
                                coordinate={this.state.new_message_pin.latlng}
                                pinColor="blue"
                            />
                    </MapView>

            </View>

            <View style={{flex:1,paddingHorizontal:35}}>
                <Button onPress={this.click}
                style={{ fontSize: 20 , color: 'blue', }}
                title="Click"
                ></Button>
            </View>
            <View style={{flex:1,paddingHorizontal:35}}>
                <TextInput style={{height:40,borderColor:'gray',borderWidth:1}}
                onChangeText={(text) => this.setState({text})}
                value={this.state.text}
                
                > 
                </TextInput>
            </View>
        </View>        
        );
    }
    loadAllMessages(){
        axios.get(`http://52.143.169.149:8082/api/messages`)
        .then(res => {
            this.setState({messages:res.data.data});
        })
    }
    addMarker(coordinates){
        
        this.setState({
            coordinates:  coordinates ,
            region:{
                latitude:coordinates.latitude,
                longitude:coordinates.longitude,
                latitudeDelta:this.state.region.latitudeDelta,
                longitudeDelta:this.state.region.longitudeDelta,
            },

            new_message_pin: { latlng: coordinates }
            
        });
     }
    onRegionChange(){
        console.log(this.state.region.longitude);
    }
    click(){
        var message={
            message :this.state.text,
            location:{
                latitude:this.state.coordinates.latitude,
                longitude:this.state.coordinates.longitude,
            }
        };
        axios.post(`http://52.143.169.149:8082/api/messages`, message )
        .then(res =>  this.loadAllMessages() );
    }
}
export default Form
