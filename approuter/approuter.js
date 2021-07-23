var approuter = require('@sap/approuter');

var ar = approuter();

ar.beforeRequestHandler.use('/userInfo', function (req, res, next) {
    if (!req.user) {
        res.statusCode = 403;
        console.log("Missing JWT Token")
        res.end(`Missing JWT Token`);
    } else {
        res.statusCode = 200;
        res.end(JSON.stringify(req.user));
    }
});
ar.start();