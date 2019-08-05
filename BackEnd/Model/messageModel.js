// messageModel.js
var mongoose = require('mongoose');

// mongoose.connect('mongodb://localhost/projectx');


var messageSchema = mongoose.Schema({

    deviceName: {
        type: String,
        // required: true,
        // default:'Anonim'
    },

    text: {
        type: String,
        // required: true,
    },

    location: {
        type: "Object",
    }
});


module.exports = mongoose.model('message', messageSchema);

module.exports.get = function(callback, limit) {
    Message.find(callback).limit(limit);
}


/*


Not :

near fonksiyonu için gereken index manuel olarak mongodb arayüzünden oluşturuldu. o oluşturulmadan kod doğru çalışmıyor. 
Azure da çalıştırmadan önce bu indexi sanal bilgisayarda da oluşturmak gerekiyor. Indexi oluşturmak için aşağıdaki
query kullanıldı.

db.messages.createIndex( { location : "2dsphere" } )
*/