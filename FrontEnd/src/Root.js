import React,{Component}from 'react';
import {View,Text,}from 'react-native';
import {Router,Scene,Actions} from 'react-native-router-flux';

import Map from './components/Map';



import Form from './components/Form';

export default class Root extends Component{
  render(){
    return(
      <Router>
        <Scene
        key='Root'
        >
        <Scene
        key='Form'
        component={Form}
        hideNavBar
        initial   //ilk acılan intent
        />
        <Scene
        key='Map'
        component={Map}
        hideNavBar
        />

        </Scene>

      </Router>
    )
  }
}