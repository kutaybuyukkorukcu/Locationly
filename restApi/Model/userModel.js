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

  seenMessages : {
    type : Array
  }
});

userSchema.statics.findByEmailOrUsername = function(emailValue, usernameValue, callback) {
  return this.find({ $or:[ {email : emailValue}, {username : usernameValue} ]}, callback);  
}

userSchema.statics.findByUsernameOrPassword = function(usernameValue, passwordValue, callback) {
  return this.find({ $and:[ {username : usernameValue}, {password : passwordValue} ]}, callback);  
}

module.exports = mongoose.model('user', userSchema);

module.exports.get = function(callback, limit) {
    User.find(callback).limit(limit);
}