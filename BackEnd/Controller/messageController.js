// messageController.js
Message = require('../Model/messageModel');

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

exports.new = function(req, res) {
    var message = new Message();
    
    console.log(req.body);
    //console.log(req);
    //message.username = req.body.name ? req.body.name : message.username;
    message.deviceName = req.body.deviceName;
    message.text = req.body.message;
    message.location = req.body.location;

    message.save(function(err) {
        if(err) {console.log(err)};
        res.json({
            message: 'New message created!',
            data: message.deviceName
        });
    });
};

exports.dist = function(req, res) {
    Message.get(function(err, messages) {
        console.log("İstek yapıldı")
        if (err) {
            res.json({
                status: "error",
                message: err,
            });
        }

        Message.find({
            location: {
                $near: {
                    $maxDistance: 100,
                    $geometry: {
                        type: "object",
                        coordinates: req.body.location
                    }
                }
            }
        }).find((error, data) => {
            if (error) console.log(error);
            res.json(data);
        });

    });
};


