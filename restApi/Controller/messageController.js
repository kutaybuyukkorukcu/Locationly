Message = require('../Model/messageModel');

exports.index = function(req, res) {
    Message.get(function(err, messages) {
        if (err) {
            res.json({
                err
            });
        }
        res.json({
            messages
        });
    });
};

exports.new = function(req, res) {
    var message = new Message();
    
    message.deviceName = req.body.deviceName;
    message.text = req.body.message;
    message.location = req.body.location;

    message.save(function(err) {
        if(err) {console.log(err)};
        res.json({
            deviceName : message.deviceName
        });
    });
};

// Returns messages around 100 meters
exports.dist = function(req, res) {
    Message.get(function(err, messages) {
        if (err) {
            res.json({
                err
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
            res.json({
                data
            });
        });

    });
};


