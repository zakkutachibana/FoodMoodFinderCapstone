// import check dari express validator
const { check } = require("express-validator");

//cek validasi register 
exports.registerValidation = [
    check("email", "Harus diisi dengan email").isEmail().normalizeEmail({ gmail_remove_dots: true }),
    check("pass", "Password minimal 6 kata").isLength({ min: 6 }),
];

// cek validasi login
exports.loginValidation = [check("email", "Harus diisi dengan email").isEmail().normalizeEmail({ gmail_remove_dots: true }), 
check("Password minimal 6 kata").isLength({ min: 6 })];

