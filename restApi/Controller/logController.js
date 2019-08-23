Log = require('../Model/logModel');

exports.new = function(req, res) { 
    var log = new Log();

    log.errorMessage=req.body.errorMessage;
    log.stackTrace = req.body.stackTrace;
    log.versionRelease=req.body.versionRelease;
    log.versionSdk = req.body.versionSdk;
    log.mark = req.body.mark;
    log.model = req.body.model;

    log.save(function(err) {
        if (err) {
            res.json({
                err
            });
        }
        res.json({
            Log : log.deviceProperties
        });
        // res.json yazmadigim surece null donuyor mu?
    });
}

exports.index = function(req, res) {
    Log.get(function(err, log) {
        if (err) {
            res.json({
                err
            });
        }

        res.json({
            log
        });
    });
};