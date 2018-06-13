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

/**
* CLIENT
*/
app.post('/saveClient', function(req, res) {
    console.log(req.body);

	var id = 0;
	var sql = 'INSERT INTO client (name, email, address, cpf, pwd) VALUES (?,?,?,?,?)';
	con.query(sql, [req.body.name, req.body.email, req.body.address, req.body.cpf, req.body.pwd], function (err, result, fields) {
		if (err) throw err;
		res.end();
	});
});

app.put('/updateClient', function(req, res) {
    console.log(req.body);

	var pwd = req.body.old_pwd
	console.log(pwd);
	if (pwd != null) {
		var checkUserSql = 'SELECT id FROM client WHERE email = ? AND pwd = ?';
		
		con.query(checkUserSql, [req.body.email, req.body.old_pwd], function (err, result, fields) {
			if (err) throw err;
			
			console.log(result);
			if (result.length > 0) {
				var sql = 'UPDATE client SET name = ?, address = ?, cpf = ?, pwd = ? WHERE id = ?';
				con.query(sql, [req.body.name, req.body.address, req.body.cpf, req.body.pwd, result[0].id], function (err, result, fields) {
					if (err) throw err;
					res.end();
				});
			}else {
				res.send(404);
			}
		});
	} else {
		var sql = 'UPDATE client SET name = ?, address = ?, cpf = ? WHERE id = ?';
		console.log(req.body.price);
		con.query(sql, [req.body.name, req.body.address, req.body.cpf, id], function (err, result, fields) {
			if (err) throw err;
			res.end();
		});
	}
});

app.get('/getClients', function(req, res) {
    console.log(req.body);

	var id = 0;
	var sql = 'SELECT * FROM client WHERE worker_id IS NULL ORDER BY name';
	con.query(sql, function (err, result, fields) {
		if (err) throw err;
		
	    console.log(result);
		res.send(result);
	});
});

app.get('/getClientById', function(req, res) {
    console.log(req.body);

	var id = req.query.id;
	var sql = 'SELECT * FROM client WHERE id = ?';
	con.query(sql, [id], function (err, result, fields) {
		if (err) throw err;
		
	    console.log(result[0]);
		res.send(result[0]);
	});
});

/**
* WORKER
*/
app.post('/saveWorker', function(req, res) {
    console.log(req.body);

	var id = 0;
	var sql = 'INSERT INTO worker (salary, function, sector) VALUES (?,?,?)';
	con.query(sql, [req.body.salary, req.body.function, req.body.sector], function (err, result, fields) {
		if (err) throw err;
		console.log(result);
		var sqlClient = 'INSERT INTO client (name, email, address, cpf, worker_id, type) VALUES (?,?,?,?,?,?)';
		con.query(sqlClient, [req.body.name, req.body.email, req.body.address, req.body.cpf, result.insertId, 1], function (err, result, fields) {
			if (err) throw err;
			res.end();
		});
	});
});

app.put('/updateWorker', function(req, res) {
    console.log(req.body);

	var id = req.body.worker_id
	console.log('id: '+ id);
	var sql = 'UPDATE client JOIN worker ON worker.id = client.worker_id SET client.name = ?, client.address = ?, client.cpf = ?, worker.salary = ?, worker.sector = ?, worker.function = ? WHERE worker.id = ?';
	con.query(sql, [req.body.name, req.body.address, req.body.cpf, req.body.salary, req.body.sector, req.body.function, id], function (err, result, fields) {
		if (err) throw err;
		res.end();
	});
});

app.get('/getWorkers', function(req, res) {
    console.log(req.body);

	var id = req.query.id;
	var sql = 'SELECT * FROM client JOIN worker ON client.worker_id = worker.id WHERE worker_id IS NOT NULL AND client.id != ? ORDER BY name';
	con.query(sql, [id], function (err, result, fields) {
		if (err) throw err;
		
	    console.log(result);
		res.send(result);
	});
});

app.get('/getWorkerById', function(req, res) {
    console.log(req.query.id);

	var id = req.query.id;
	var sql = 'SELECT * FROM worker JOIN client ON worker.id = client.worker_id WHERE client.worker_id = ?';
	con.query(sql, [id], function (err, result, fields) {
		if (err) throw err;
		
	    console.log(result[0]);
		res.send(result[0]);
	});
});

app.post('/deleteWorkers', function(req, res) {
    console.log(req.body);

	var ids = JSON.parse(req.body.ids);
	console.log(ids);
	var sql = 'DELETE FROM client WHERE worker_id IN (?)';
	con.query(sql, [ids], function (err, result, fields) {
		if (err) throw err;
	
	    console.log(result);
		res.send(result);
	});
});

/**
* LOGIN
*/
app.post('/doLogin', function(req, res) {
    console.log(req.body);

	var id = 0;
	var sql = 'SELECT * FROM client WHERE email = ? AND pwd = ?';
	var result = null;
	
	con.query(sql, [req.body.email, req.body.pwd], function (err, result, fields) {
		if (err) throw err
		
		var resp = result[0];
		if (result.length > 0) {
			resp['type'] = resp['type'] == 0
			console.log(resp)
			res.send(resp);
		} else {
			res.status(404);
		}
	});
});

/**
* PRODUCT
*/
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

app.post('/deleteProducts', function(req, res) {
    console.log(req.body);

	var ids = JSON.parse(req.body.ids);
	console.log(ids);
	var sql = 'DELETE FROM product WHERE id IN (?)';
	con.query(sql, [ids], function (err, result, fields) {
		if (err) throw err;
	
	    console.log(result);
		res.send(result);
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

app.get('/getProductById', function(req, res) {
    console.log(req.body);

	var id = req.query.id;
	var sql = 'SELECT * FROM product where id = ' + id;
	con.query(sql, function (err, result, fields) {
		if (err) throw err;
		
	    console.log(result);
		res.send(result[0]);
	});
});

app.post('/buyProduct', function(req, res) {
    console.log(req.body);

	var clientId = req.body.clientId;
	var productId = req.body.productId;
	var creditCard = req.body.creditCard;
	var currentDate = new Date();
	var arrivalDate = new Date();
	var randomDateFromNow = Math.random() * 15 + 5;
	arrivalDate.setDate(arrivalDate.getDate() + randomDateFromNow);
	
	var randomShipping = 'SELECT id FROM shipping ORDER BY rand() limit 10';
	con.query(randomShipping, function (err, sResult, fields) {
		if (err) throw err;
	
		console.log(sResult[0].id);
	    var sql = 'INSERT INTO client_has_product (client_id, product_id, product_status, arrival_date, purchase_date, shipping_id) VALUES (?,?,?,?,?,?)';
		con.query(sql, [clientId, productId, 0, arrivalDate.getTime(), currentDate.getTime(), sResult[0].id], function (err, result, fields) {
		if (err) throw err;
		
			var sql = 'UPDATE product SET qtd = qtd -1 WHERE id = ?';
			con.query(sql, [productId], function (err, result, fields) {
				if (err) throw err;
		
				console.log(result);
				res.send(result);
			});
		});
	});
});

app.get('/productImage', function(req, res){
	var file = "./images/products/" + req.query.id + ".png";
	res.download(file); // Set disposition and send it.
});

/**
* ORDER
*/
app.get('/getOrders', function(req, res) {
    console.log(req.body);

	var id = req.query.id;
	var sql = 'SELECT product.name, client_has_product.product_id, client_has_product.product_status, client_has_product.arrival_date, client_has_product.purchase_date FROM product JOIN client_has_product ON product.id = client_has_product.product_id WHERE client_has_product.client_id = ? ORDER BY client_has_product.purchase_date';
	
	console.log(sql);
	con.query(sql, [id], function (err, result, fields) {
		if (err) throw err;
		
	    console.log(result);
		res.send(result);
	});
});

/**
* SHIPPING
*/
app.post('/saveShipping', function(req, res) {
    console.log(req.body);

	var id = 0;
	var sql = 'INSERT INTO shipping (name, company, payment) VALUES (?,?,?)';
	con.query(sql, [req.body.name, req.body.company, req.body.payment], function (err, result, fields) {
		if (err) throw err;
		console.log(result);
		res.end();
	});
});

app.put('/updateShipping', function(req, res) {
    console.log(req.body);

	var id = req.body.id
	console.log('id: '+ id);
	var sql = 'UPDATE shipping SET name = ?, company = ?, payment = ? WHERE id = ?';
	con.query(sql, [req.body.name, req.body.company, req.body.payment, id], function (err, result, fields) {
		if (err) throw err;
		res.end();
	});
});

app.get('/getAllShipping', function(req, res) {
    console.log(req.body);

	var id = req.query.id;
	var sql = 'SELECT * FROM shipping ORDER BY name';
	con.query(sql, [id], function (err, result, fields) {
		if (err) throw err;
		
	    console.log(result);
		res.send(result);
	});
});

app.get('/getShippingById', function(req, res) {
    console.log(req.query.id);

	var id = req.query.id;
	var sql = 'SELECT * FROM shipping WHERE id = ?';
	con.query(sql, [id], function (err, result, fields) {
		if (err) throw err;
		
	    console.log(result[0]);
		res.send(result[0]);
	});
});

app.post('/deleteShipping', function(req, res) {
    console.log(req.body);

	var ids = JSON.parse(req.body.ids);
	console.log(ids);
	var sql = 'DELETE FROM shipping WHERE id IN (?)';
	con.query(sql, [ids], function (err, result, fields) {
		if (err) throw err;
	
	    console.log(result);
		res.send(result);
	});
});

/**
* THREAD TO RUN EACH 1 MINUTE TO CHANGE PURCHASE STATUS
*/
function purchaseStatusChange() {
	setInterval(function() {
		console.log("called");
		var sql = 'UPDATE client_has_product SET product_status = product_status +1 WHERE product_status < 3';
		con.query(sql, function (err, result, fields) {
			if (err) throw err;
	
			console.log(result);
		});
	}, 1000*60);
}

var server = app.listen(8000, '192.168.0.12', function () {
	var host = server.address().address
	var port = server.address().port

	console.log("Example app listening at http://%s:%s", host, port)
	
	purchaseStatusChange();
});
