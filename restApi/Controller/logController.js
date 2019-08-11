Log = require('../Model/logModel');

exports.new = function(req, res) { 
    var log = new Log();

    log.deviceProperties = req.body.deviceProperties;
    log.stackTrace = req.body.stackTrace;

    log.save(function(err) {
        if(err) {
            console.log(err);
        }

        // res.json yazmadigim surece null donuyor mu?
    });
}