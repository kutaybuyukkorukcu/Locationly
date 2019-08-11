User = require('../Model/userModel');
Message = require('../Model/messageModel');
var _ = require('lodash');

// get users eklenecek.

exports.index = function(req, res) {
  User.findById(req.query.id, function(err, user) {
    if(err) {
      res.json({
        err
      });
    }

    res.json({
      user
    });
  });
};

exports.new = function(req, res) {
  const user = new User();

  user.firstName = req.body.firstName;
  user.lastName = req.body.lastName;
  user.email = req.body.email;
  user.username = req.body.username;
  user.password = req.body.password;

  User.findByEmailOrUsername(req.body.email, req.body.username, function(err, docs){
    if(err) {
      console.log(err);
    }

    if(docs.length) {
      res.writeHead(409, 'Bu email veya username kullaniliyor', {'content-type' : 'text/plain'});
      res.end("Email veya username ayni oldugundan request sonlandirildi!");
    }else {
      user.save(function(err) {
        if(err) {
          console.log(err);
        }

        res.json({
          id : user._id
        });
      });
    }
  })
};

exports.checkMessages = async function(req, res) {
  var newMessages = [];
  
  var searchQuery = {
    location: {
      $near: {
        $maxDistance: 100,
        $geometry: {
            type: "object",
            coordinates: req.body.location
        }
      }
    }
  };
  console.log(newMessages);
  newMessages = await Message.find(searchQuery).exec();
  newMessages = newMessages.map( item => String(item._id));
  console.log(typeof []);
  User.findById(req.body.id, function(err, user) {
    if(err) {
      return console.log(err)
    };

    const userArray = user.seenMessages;
    const postArray = newMessages;
    
    const arrayDiff = _.difference(postArray, userArray);

    user.save(function(err){
      if(err) {
        return res.status(404).end();
      }

      res.json({
        messages : arrayDiff // messages idleri donecek. ResponseBody'de ArrayList<String> messages tarafindan aliniyor.
      }); 
    });
  });
};

exports.login = function(req,res){
  User.findByUsernameOrPassword(req.body.username, req.body.password, function(err,docs){
    if(err) {
      console.log(err);
    }

    const user = docs[0]
    
    if(docs.length) {
      res.json({
        user
      });
    }else {
      console.log(res);
      res.writeHead(400,"Girdiginiz bilgiler hatalidir.",{"content-type" : "text/plain"});
      res.end("Girdiğiniz bilgiler hatalı olduğundan dolayı işlem sonlandırıldı.");
    }
  })
}