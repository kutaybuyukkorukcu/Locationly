let express = require('express');

let bodyParser = require('body-parser');

let mongoose = require('mongoose');

let app = express();

let apiRoutes = require("./api-routes")

// Configure bodyparser to handle post requests

app.use(bodyParser.urlencoded({
    extended: true
}));

app.use(bodyParser.json());

var port = process.env.PORT || 8082;

mongoose.connect('mongodb://localhost/projectx');

// Send message for default URL
app.get('/', (req, res) => res.send('/ calisti'));

// Use Api routes in the App
app.use('/api', apiRoutes);

app.listen(port, function() {
    console.log("Running projectX on port " + port);
});