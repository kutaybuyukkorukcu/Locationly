Location = require('../Model/locationModel');

exports.index = function(req, res) {
    Location.get(function(err, location) {
        if (err) {
            res.json({
                err
            });
        }

        res.json({
            location
        });
    });
};

exports.new = function(req, res) {
    console.log("post etiiiiiiiiiiiiiiiiiiiii")
    const location = new Location();
    
    location.userId = req.body.userId;
    location.location = req.body.location;

    location.save(function(err) {
        if(err) {
            console.log(err)
        }

        res.json({
            location : location.location
        });
    });
};
