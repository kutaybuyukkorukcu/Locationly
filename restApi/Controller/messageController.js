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
            console.log(err)
        }

        res.json({
            username : message.username
        });
    });
};

// Returns messages around 100 meters
exports.distance = function(req, res) {
    const lat = Number(req.query.lat)
    const lon = Number(req.query.lon)
    
    const location = [lat,lon]
    
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
    }).find((error, message) => {
        if (error) console.log(error);
        res.json({
            message
        });
    });
};

// exports.checkMessages = async function(req, res) {
//     var newMessages = [];
    
//     var searchQuery = {
//       location: {
//         $near: {
//           $maxDistance: 100,
//           $geometry: {
//               type: "object",
//               coordinates: req.body.location
//           }
//         }
//       }
//     };
    
//     newMessages = await Message.find(searchQuery).exec();
//     newMessages = newMessages.map( item => String(item._id));
  
//     User.findById(req.body.id, function(err, user) {
//       if(err) {
//         return console.log(err)
//       };
  
//       const userArray = user.seenMessages;
//       const postArray = newMessages;
      
//       const arrayDiff = _.difference(postArray, userArray);
  
//       userArray.push(...arrayDiff);
      
//       user.seenMessages = userArray;
//       console.log(user.seenMessages);
//       user.save(function(err){
//         if(err) {
//           return res.status(404).end();
//         }
  
//         res.json({
//           messages : arrayDiff // messages idleri donecek. ResponseBody'de ArrayList<String> messages tarafindan aliniyor.
//         }); 
//       });
//     });
//   };