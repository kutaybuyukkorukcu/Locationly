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
    const message = new Message();
    
    message.username = req.body.username;
    message.text = req.body.text;
    message.location = req.body.location;

    message.save(function(err) {
        if(err) {
            res.json({
              err
            });
        }

        res.json({
            username : message.username
        });
    });
};

// Returns messages around 100 meters
exports.distance = function(req, res) {
    const lat = Number(req.query.lat);
    const lon = Number(req.query.lon);
    
    const location = [lat,lon];
    
    Message.find({
        location: {
            $near: {
                $maxDistance: 100,
                $geometry: {
                    type: "object",
                    coordinates: location
                }
            }
        }	
    }).find((err, message) => {
        if(err) {
            res.json({
              err
            });
        }

        res.json({
            message
        });
    });
};