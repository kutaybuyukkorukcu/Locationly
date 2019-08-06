var mongoose = require('mongoose');

var userSchema = mongoose.Schema({

  firstName : {
    type: String,
  },

  lastName : {
    type : String,
  },

  email : {
    type : String,
    },
  

  username : {
    type : String,
  },

  password : {
    type : String,
  },

  isMarkerSeen : {
    type : Array
  }
});

userSchema.statics.findByEmailOrUsername = function(value, usernameV, callback) {
  return this.find({ $or:[ {email : value}, {username : usernameV} ]}, callback);  
}

module.exports = mongoose.model('user', userSchema);

module.exports.get = function(callback, limit) {
    User.find(callback).limit(limit);
}