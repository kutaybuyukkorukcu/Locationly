import React, { Component } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import MapView from 'react-native-maps';
import Geolocation from '@react-native-community/geolocation';


export default class App extends React.Component {
  state = {
    //Baslangic Degerleri
    currentLongitude: '0',
    currentLatitude: '0',
    konumum: '0',
 }
 // React api'indan lifecycle methodu
 componentDidMount = () => {
    var that =this;
    this.callLocation(that);
 }
 callLocation(that){
  
  Geolocation.getCurrentPosition(
       (position) => {
          //boylam alma
          const currentLongitude = JSON.stringify(position.coords.longitude);
          //enlem alma
          const currentLatitude = JSON.stringify(position.coords.latitude);
         // yukarida belirttigim statelere deger atadim.
          that.setState({ currentLongitude:currentLongitude });
          that.setState({ currentLatitude:currentLatitude });
          that.setState({ konumum: 'enlem : ' + this.state.currentLatitude + ' boylam : ' + this.state.currentLongitude});
       },
       (error) => alert(error.message),
       { enableHighAccuracy: false}
    );
    that.watchID = Geolocation.watchPosition((position) => {
       const currentLongitude = JSON.stringify(position.coords.longitude);
       const currentLatitude = JSON.stringify(position.coords.latitude);
       that.setState({ currentLongitude:currentLongitude });
       that.setState({ currentLatitude:currentLatitude });
       that.setState({ konumum: 'enlem : ' + this.state.currentLatitude + 'boylam : ' + this.state.currentLongitude});
    },
    (error) => alert('Error', JSON.stringify(error)),
    {enableHighAccuracy: true, distanceFilter: 1}, // distanceFilter ornegin 10m yurudukten sonra konumunu guncelliyor
    );
 }

 // Bu da react lifecycle methodu
 componentWillUnmount = () => {
  Geolocation.clearWatch(this.watchID);
 }
 
 render() {
    return (
      // React-native-maps API'dan componentlar
      <View style={styles.container}>
        <MapView
          style={styles.map}
          initialRegion={{
            latitude: parseFloat(this.state.currentLatitude),
            longitude: parseFloat(this.state.currentLongitude),
            latitudeDelta: 0.0922,
            longitudeDelta: 0.0421,
          }}
        >
          <MapView.Marker
              coordinate = {{
                latitude: parseFloat(this.state.currentLatitude), 
                longitude: parseFloat(this.state.currentLongitude)}}
                title = {this.state.konumum} 
            />
        </MapView>
      </View>
    )
 }
}
const styles = StyleSheet.create ({
 container: {
    flex: 1,
 },
 boldText: {
    fontSize: 30,
    color: 'red',
 },
 map: {
    flex: 1
 }
})