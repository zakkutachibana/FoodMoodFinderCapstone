const mysql = require("mysql");

// buat konfigurasi koneksi database
const dbconn = mysql.createConnection({
    port : 3306,
    host: "34.101.162.11",
    user :"root",
    password :"root123",
    database : "foodmoodfinder_db",
    multipleStatements: true,
});

// buat koneksi ke database
dbconn.connect((err) => {
    if (err) throw err;
    console.log("Database connected.");
});

module.exports = dbconn;