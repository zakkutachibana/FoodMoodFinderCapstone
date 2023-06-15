const mysql = require("mysql");

// buat konfigurasi koneksi database
const dbconn = mysql.createConnection({
    port : 3306,
    host: "YOUR_DB_HOST",
    user :"YOUR_DB_USER",
    password :"YOUR_DB_PASS",
    database : "YOUR_DB_DATABASE",
    multipleStatements: true,
});

// buat koneksi ke database
dbconn.connect((err) => {
    if (err) throw err;
    console.log("Database connected.");
});

module.exports = dbconn;
