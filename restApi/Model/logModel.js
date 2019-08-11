var mongoose = require('mongoose');

var logSchema = mongoose.Schema({

  deviceProperties : {
      type : String,
  },

  stackTrace : {
      type : String,
  }
});

module.exports = mongoose.model('log', logSchema);

module.exports.get = function(callback, limit) {
    Log.find(callback).limit(limit);
}