'use strict';

var React = require('react-native');

var {
  AppRegistry,
  Text,
  TextInput,
  View,
  NativeModules,
} = React;

var SearchResult = React.createClass({
  render: function() {
    return (
      <View>
        <Text>{this.props.result.principalName}</Text>
        <Text>{this.props.result.firstName}</Text>
        <Text>{this.props.result.middleName}</Text>
        <Text>{this.props.result.lastName}</Text>
      </View>
    );
  },
});

var scignup = React.createClass({
  getInitialState: function() {
    return {
      result: {
        principalName: '',
        firstName: '',
        middleName: '',
        lastName: '',
      },
    };
  },
  render: function() {
    return (
      <View>
        <TextInput onSubmitEditing={this.handleSearch} placeholder='USC ID'/>
        <SearchResult result={this.state.result}/>
      </View>
    );
  },
  handleSearch: function(e) {
    var that = this;
    console.log(e.nativeEvent.text);
    NativeModules.SearchModule.search(e.nativeEvent.text,
        function(error) {
          console.log(error);
        },
        function(result) {
          that.setState({
            result: {
              principalName: result[0],
              firstName: result[1],
              lastName: result[2],
              middleName: '',
            },
          });
        }
    );
  }
});

AppRegistry.registerComponent('scignup', () => scignup);
