// contactModel.js
var mongoose = require('mongoose');

// Setup schema

var messageSchema = mongoose.Schema({

    //  name: {
    //     type: String,
    //    required: true,					name alanı daha sonra aktifleştirilecek
    //    default:'Anonim'
    // },

    message: {
        type: String,
        required: true,

    },

    location: {
        type: "Object",
    }

});

// Export Contact model

var message = module.exports = mongoose.model('message', messageSchema);
module.exports.get = function(callback, limit) {
    Message.find(callback).limit(limit);
}