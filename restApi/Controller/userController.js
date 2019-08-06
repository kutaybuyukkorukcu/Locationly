User = require('../Model/userModel');
var _ = require('lodash');

exports.index = function(req, res) {
  console.log(req.query.id);
  User.findById(req.query.id, function(err, user) {
    if(err) {
      res.json({
        err
      });
    }
    console.log(res);
    res.json({
      user
    });
  });
};

exports.new = function(req, res) {
  var user = new User();
  user.firstName = req.body.firstName;
  user.lastName = req.body.lastName;
  user.email = req.body.email;
  user.username = req.body.username;
  user.password = req.body.password;

  User.findByEmailOrUsername(req.body.email, req.body.username, function(err, docs){
    if(err) {console.log(err);}
    if(docs.length){
      res.writeHead(409, 'Bu email veya username kullaniliyor', {'content-type' : 'text/plain'});
      res.end("Email veya username ayni oldugundan request sonlandirildi!");
    } else {
      user.save(function(err) {
        if(err) {console.log(err);}
        res.json({
          id : user._id
        });
      });
    }
  })
};

exports.seenMarkers = function(req, res) {
  User.findById(req.body.id, function(err, user) {
    if(err) {return console.log(err)};
    var userArray = user.isMarkerSeen;
    var postArray = req.body.isMarkerSeen;
    
    var arrayDiff = _.difference(postArray, userArray);
 
    userArray.push(...arrayDiff);
    user.isMarkerSeen = userArray;
    
    user.save(function(err){
      if(err) {return res.status(404).end();}
      res.json({
        user
      });
    });
  });
};