// contactController.js
// Import contact model
Message = require('./messageModel');
// Handle index actions
exports.index = function(req, res) {
    Message.get(function(err, messages) {
        if (err) {
            res.json({
                status: "error",
                message: err,
            });
        }
        res.json({
            status: res.statusCode,
            message: "Messages retrieved successfully",
            data: messages
        });
    });
};
// Handle create contact actions
exports.new = function(req, res) {
    var message = new Message();
    console.log(req);

    //message.name = req.body.name ? req.body.name : message.name;    name daha sonra aktifleştirilecek
    message.message = req.body.message;
    message.location = req.body.location;

    message.save(function(err) {
        // if (err)
        //     res.json(err);
        res.json({
            message: 'New message created!',
            data: message
        });
    });
};




// *kişinin koordinatına göre view işlemi geonear kullanılacak.