//To run in terminal: node ipAddress -> check my ip 
var express = require('express');
var app = express();
var http = require("http");
var fs = require("fs");
var request = require('request');
var mysql = require('mysql');

var bodyParser = require('body-parser');

var index = fs.readFileSync('./index.html');

// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: false }));
app.use(express.bodyParser({limit: '50mb'}));

// parse application/json
app.use(bodyParser.json());

var con = mysql.createConnection({
  host: "127.0.0.1",
  user: "root",
  password: "",
  database: "loja"
});
con.connect();

app.post('/saveClient', function(req, res) {
    console.log(req.body);

	var id = 0;
	var sql = 'INSERT INTO client (name, email, address, cpf, pwd) VALUES (?,?,?,?,?)';
	console.log(req.body.price);
	con.query(sql, [req.body.name, req.body.email, req.body.address, req.body.cpf, req.body.pwd], function (err, result, fields) {
		if (err) throw err;
		res.end();
	});
});

app.get('/doLogin', function(req, res) {
    console.log(req.body);

	var id = 0;
	var sql = 'SELECT id, name, email FROM client WHERE email = ? AND pwd = ?';
	con.query(sql, [req.body.email, req.body.pwd], function (err, result, fields) {
		if (err) throw err;
		
		if (result.length > 0) {
			console.log(result);
			res.send(result);
		} else {
			res.status(404);
		}
	});
});

app.post('/saveProduct', function(req, res) {
    console.log(req.body);

	var id = 0;
	var sql = 'INSERT INTO product (name, description, price, discount, qtd) VALUES (?,?,?,?,?)';
	console.log(req.body.price);
	con.query(sql, [req.body.name, req.body.description, (req.body.price).toFixed(2), req.body.discount, req.body.qtd], function (err, result, fields) {
		if (err) throw err;
	
		id = result.insertId;
		require("fs").writeFile("./images/products/" + id + ".png", req.body.image, 'base64', function(err) {
			console.log(err);
		});
		res.end();
	});
});

app.put('/updateProduct', function(req, res) {
    console.log(req.body);

	var id = req.body.id;
	var sql = 'UPDATE product SET name = ?, description = ?, price = ?, discount = ?, qtd = ? WHERE id = ?';
	console.log(req.body.price);
	con.query(sql, [req.body.name, req.body.description, (req.body.price).toFixed(2), req.body.discount, req.body.qtd, id], function (err, result, fields) {
		if (err) throw err;
	
		require("fs").writeFile("./images/products/" + id + ".png", req.body.image, 'base64', function(err) {
			console.log(err);
		});
		res.end();
	});
});

app.post('/deleteProduct', function(req, res) {
    console.log(req.body);

	var id = req.body.id;
	var sql = 'DELETE FROM product WHERE id in (?)';
	console.log(req.body.price);
	con.query(sql, [id], function (err, result, fields) {
		if (err) throw err;
	
		res.end();
	});
});

app.get('/getProducts', function(req, res) {
    console.log(req.body);

	var id = 0;
	var sql = 'SELECT * FROM product ORDER BY name';
	con.query(sql, function (err, result, fields) {
		if (err) throw err;
		
	    console.log(result);
		res.send(result);
	});
});

app.get('/getProduct', function(req, res) {
    console.log(req.body);

	var id = req.query.id;
	var sql = 'SELECT * FROM product where id = ' + id;
	con.query(sql, function (err, result, fields) {
		if (err) throw err;
		
	    console.log(result);
		res.send(result[0]);
	});
});

app.get('/productImage', function(req, res){
	var file = "./images/products/" + req.query.id + ".png";
	res.download(file); // Set disposition and send it.
});

var server = app.listen(8000, '192.168.0.14', function () {
	var host = server.address().address
	var port = server.address().port

	console.log("Example app listening at http://%s:%s", host, port)
});
