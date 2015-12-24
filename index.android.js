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
        <TextInput ref='uscid' onSubmitEditing={this.handleSearch}/>
        <SearchResult result={this.state.result}/>
      </View>
    );
  },
  handleSearch: function(e) {
    var that = this;
    NativeModules.SearchModule.search(this.refs.uscid.value,
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
