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
    // validate : {
    //   isAsync : true,
      // validator : function(v, cb) {
      //   User.find({email : v}, function(err, docs){
      //     if(err) {console.log(err);}
      //     // const msg = v + ' baskasi tarafindan kullanilmakta!';
      //     setTimeout(() => {
      //       const result = docs.length > 1;
      //       console.log(result);
      //       cb(result, null);
      //     }, 2000);
      //   });
    // },
    // validate : {
    //   isAsync : true,
    //   validator : function(v, cb) {
    //     User.find({email : v}, function(err, docs){
    //       if(err) {console.log(err);}
    //       const result = docs.length == 0;
    //       cb(result, null);
    //     });
    //   },
    //   message : 'Email already exists!',
    // required : true
    },
  

  username : {
    type : String,
    // required : true
  },

  password : {
    type : String,
    // required : true
  },

  isMarkerSeen : {
    type : Array
  }
});

userSchema.statics.findByEmailOrUsername = function(value, usernameV, callback) {
  return this.find({ $or:[ {email : value}, {username : usernameV} ]}, callback);  
}

// userSchema.statics.findByEmail = function(value, callback) {
//   return this.find({ email : value }, callback);
// }

// userSchema.statics.findByUsername = function(value, callback) {
//   return this.find({ username : value }, callback);
// }

module.exports = mongoose.model('user', userSchema);

// Bunun olayi ne?
module.exports.get = function(callback, limit) {
    User.find(callback).limit(limit);
}

// module.exports.difference = function(helpArray) {
//   return this.filter(function(i) {
//     return !helpArray.includes(i);
//   });
// }