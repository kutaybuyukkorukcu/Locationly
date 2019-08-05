// Initialize express router
let router = require('express').Router();

// Set default API response

router.get('/', function(req, res) {
    res.json({
        status: 'API Its Working',
        message: 'Welcome to projectX crafted with <3',
    });
});


var messageController = require('./Controller/messageController');
var userController = require('./Controller/userController');

router.route('/messages')
    .get(messageController.index)
    .post(messageController.new);

router.route('/distMessages')
	.post(messageController.dist);

router.route('/users')
    .get(userController.index)
    .post(userController.new);

router.route('/users/:id')
    .get(userController.index);

router.route('/addMarker')
    .post(userController.seenMarkers);

module.exports = router;
