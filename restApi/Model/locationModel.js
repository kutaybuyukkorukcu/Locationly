var mongoose = require('mongoose');

var timestamps = require('mongoose-timestamp');
var locationSchema = mongoose.Schema({

    userId : {
        type : String,
    },

    location:{
       type: 'Object'
    } 

});
locationSchema.plugin(timestamps);

module.exports = mongoose.model('location', locationSchema);

module.exports.get = function(callback, limit) {
    Location.find(callback).limit(limit);
}

