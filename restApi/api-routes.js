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
var logController = require('./Controller/logController');

router.route('/messages')
    .get(messageController.index)
    .post(messageController.new);

router.route('/messages/distance')
	.get(messageController.distance);

router.route('/users')
    .get(userController.index)
    .post(userController.new);

router.route('/users/login')
    .post(userController.login);

router.route('/users/id')
    .get(userController.index);

router.route('/users/checkMessages')
    .post(userController.checkMessages);

router.route('/users/login')
   .post(userController.login);

router.route('/logs')
    .post(logController.new);

module.exports = router;
