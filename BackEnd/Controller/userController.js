User = require('../Model/userModel');
var _ = require('lodash');

// exports.index = function(req, res) {
//   User.get((err, users) => {
//     // var x = 5;
//     // if(err || x === 5) {
//     // res.json({
//     //   status : 'error',
//     //   message : err
//     // });
//     if(err) {console.log(err);}
//     //   res.writeHead( 409, 'Bu email kullaniliyor', {'content-type' : 'text/plain'});
//     //   res.end( 'Current value does not match');
//     //   console.log(res);
//     // }
//     res.json({
//       user : users
//     });
//   });
// };


exports.index = function(req, res) {
  console.log(req.query.id);
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


// exports.new = function(req, res) {
//   var user = new User();

//   user.firstName = req.body.firstName;
//   user.lastName = req.body.lastName;
//   user.email = req.body.email;
//   user.username = req.body.username;
  
//   // db.User.find({'name' : new RegExp(data, 'i')}, function(err, docs){
//   //   cb(docs);
//   // });
//   // console.log(user); // Neden isMarkerSeen bos donuyor? Engellenebilir mi?
//   user.save(function(err) {
//     if(err) {console.log(err);}
//     res.json({
//       message : 'New user created!',
//       data : user._id
//     });
//   });
// };

// 1-ilk once email icin kontrol et varsa hata donsun 
// 2-sonra username icin kontrol et varsa hata donsun 
// 3-iki checkte de hata bulamadi bu yuzden kayit islemini gerceklestirsin.

// function omega() {
//   await checkEmail();
//   await checkUsername(); // checkUsername.catch(err => {console.log(err);});
//   await saveIslemi();
// };

// async function checkEmail(reqBody, res) { // reqBody = req.body
//   User.findByEmail(reqBody.email, function(err, docs) {
//     if(err) {console.log(err);} // nasil bir hata doneceginden emin degilim / Dusununce ve deneyince degistirecegim
//     if(docs.length) {
//       res.writeHead(409, "Bu email kullaniliyor", {"content-type" : "text/plain"});
//       res.end("Username ayni oldugundan request sonlandirildi");
//       return Promise.reject("Emailler ayni").catch( () => {
//         new Error("Save islemi gerceklestirmeEmail");
//       })
//     } else {
//       return Promise.resolve("BasariliEmail");
//     }
//   })
// };

// async function checkUsername(reqBody, res) { // Gerekli olursa res parametresini verecegim
//   User.findByUsername(reqBody.username, function(err, docs) {
//     if(err) {console.log(err);}
//     if(docs.length){
//       res.writeHead(409, "Bu username kullaniliyor", {"content-type" : "text/plain"});
//       res.end("Username ayni oldugundan request sonlandirildi");
//       return Promise.reject("Usernameler ayni").catch(() => {
//         new Error("Save islemi gerceklestirmeUsername");
//       })
//     } else {
//       return Promise.resolve("BasariliUsername");
//     }
//   })
// };

exports.new = function(req, res) { // removed async keyword
  var user = new User();
  user.firstName = req.body.firstName;
  user.lastName = req.body.lastName;
  user.email = req.body.email;
  user.username = req.body.username;
  user.password = req.body.password;

  // const param = req.body;
  // try { 
  //   const usernameAwait = await checkUsername(param, res);
  //   console.log(usernameAwait);
  //   console.log("slm");
  //   const emailAwait = await checkEmail(param, res);
  //   console.log(emailAwait);
  //   console.log("yo");
  // }catch (err) {
  //   console.log(err);
  // }

  User.findByEmailOrUsername(req.body.email, req.body.username, function(err, docs){
    if(err) {console.log(err);} // nasil bir hata doneceginden emin degilim / Dusununce ve deneyince degistirecegim
    if(docs.length){
      res.writeHead(409, 'Bu email veya username kullaniliyor', {'content-type' : 'text/plain'});
      res.end("Email veya username ayni oldugundan request sonlandirildi!");
    } else {
      user.save(function(err) {
        if(err) {console.log(err);}
        res.json({
          user._id
        });
      });
    }
  })

  // User.findByEmail(req.body.email, function(err, docs) {
  //   if(err) {console.log(err);} // nasil bir hata doneceginden emin degilim / Dusununce ve deneyince degistirecegim
  //   if(docs.length){
  //     res.writeHead(409, 'Bu email kullaniliyor', {'content-type' : 'text/plain'});
  //     res.end("Email ayni oldugundan request sonlandirildi!");
  //   } else {
  //     user.save(function(err) {
  //       if(err) {console.log(err);}
  //       res.json({
  //         message : 'Yeni kullanici olusturuldu!',
  //         data : user._id,
  //       });
  //     });
  //   }
  // })

  // User.findBy(req.body.email, function(err, docs) {
  //   if(err) {console.log(err);} // nasil bir hata doneceginden emin degilim / Dusununce ve deneyince degistirecegim
  //   if(docs.length){
  //     res.writeHead(409, 'Bu email kullaniliyor', {'content-type' : 'text/plain'});
  //     res.end("Email ayni oldugundan request sonlandirildi!");
  //   } else {
  //     user.save(function(err) {
  //       if(err) {console.log(err);}
  //       res.json({
  //         message : 'Yeni kullanici olusturuldu!',
  //         data : user._id,
  //       });
  //     });
  //   }
  // })

  // User.findByUsername(req.body.username, function(err, docs) {
  //   if(err) {console.log(err);} // nasil bir hata doneceginden emin degilim / Dusununce ve deneyince degistirecegim
  //   if(docs.length){
  //     res.writeHead(409, 'Bu username kullaniliyor', {'content-type' : 'text/plain'});
  //     res.end('Current value does not match');
  //   } else {
  //     user.save(function(err) {
  //       if(err) {console.log(err);}
  //       res.json({
  //         message : 'New user created!',
  //         data : user._id,
  //       });
  //     });
  //   }
  // })
};



exports.seenMarkers = function(req, res) {
  User.findById(req.body.id, function(err, user) {
    if(err) {return console.log(err)}; // {return res.status().end();}

    var userArray = user.isMarkerSeen;
    var postArray = req.body.isMarkerSeen;

    // reduce() ile refactor edecegim.
    // userModel uzerinde exports ile deneyecegim.
    
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