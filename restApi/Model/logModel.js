var mongoose = require('mongoose');
var integerValidator = require('mongoose-integer'); 
var logSchema = mongoose.Schema({

  errorMessage:{
  	type:String
  },

  stackTrace : {
      type : String
  },

  versionRelease:{
  	type:String
  },
  versionSdk : {
      type : Number,
      integer: true
  },

  mark : {
      type : String
  },

  model : {
      type : String
  }
});

module.exports = mongoose.model('log', logSchema);

module.exports.get = function(callback, limit) {
    Log.find(callback).limit(limit);
}