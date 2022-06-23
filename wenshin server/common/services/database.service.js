const mysql = require("mysql");
let pool = mysql.createPool({
    connectionLimit: 100,
    host: "localhost",
    user: "server",
    database: "wenshin",
    //password: "password"
    password: "my0sql1password21337"
});

module.exports = pool