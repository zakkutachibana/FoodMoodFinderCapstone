const express = require("express");
const router = express.Router();
const db = require("../config/database");
const { registerValidation, loginValidation } = require("../middleware/auth-validation");
const { check, validationResult } = require('express-validator');
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
var http = require("http");


// REGISTRASI AKUN 
router.post("/register", 
    [
        check("email", "Isi dengan email yang valid.").isEmail().normalizeEmail({ gmail_remove_dots: true }),
        // Tambahkan validasi format email di sini
    ],
    (req, res, next) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(400).json({
                error: true,
                msg: "Isi dengan email yang valid"
            });
        }
    db.query(`SELECT * FROM user WHERE LOWER(email) = LOWER(${db.escape(req.body.email)});`, (err, result) => {
        // email sudah terdaftar
        if (result.length) {
            return res.status(409).send({
                error: true,
                msg: "Akun ini sudah ada!",
            });
        } else {

            bcrypt.hash(req.body.pass, 10, (err, hash) => {
                if (err) {
                    return res.status(500).send({
                        error:true,
                        msg: err,
                    });
                } else {
                    // hash password -> tambah ke database
                    db.query(`INSERT INTO user (email, pass) VALUES (${db.escape(req.body.email)}, ${db.escape(hash)});`, (err, result) => {
                        if (err) {
                            throw err;
                            return res.status(400).send({
                                msg: err,
                            });
                        }
                        return res.status(201).send({
                            error: false,
                            msg: "Akun berhasil didaftarkan",
                        });
                    });
                }
            });
        }
    });
});

// LOGIN AKUN 
router.post("/login", loginValidation, (req, res, next) => {
    db.query(`SELECT * FROM user WHERE email = ${db.escape(req.body.email)};`, (err, result) => {
        // akun tidak ditemukan
        if (err) {
            throw err;
            return res.status(400).send({
                msg: err,
            });
        }
        if (!result.length) {
            return res.status(401).send({
                error : true,
                msg: "Email atau password salah",
            });
        }
        // cek password
        bcrypt.compare(req.body.pass, result[0]["pass"], (bErr, bResult) => {
            // password salah
            if (bErr) {
                throw bErr;
                return res.status(401).send({
                    msg: "Email atau password salah",
                });
            }
            if (bResult) {
                const token = jwt.sign({ id: result[0].id }, "foodmoodfinder-secrect");
                // login berhasil
                return res.status(200).send({
                    error : false,
                    msg: "berhasil login.",
                    token,
                    user: result[0],
                });
            }
            return res.status(401).send({
                error: true,
                msg: "Email atau password salah",
            });
        });
    });
});

// masuk akun berdasarkan email dengan token 
function verifyToken(req, res, next) {
    const token = req.headers['authorization'];
  
    if (!token) {
      return res.status(401).json({ message: 'Token tidak tersedia' });
    }
  
    jwt.verify(token, 'foodmoodfinder-secrect', (err, decoded) => {
      if (err) {
        return res.status(403).json({ message: 'Token tidak valid' });
      }
  
      req.userId = decoded.id;
      next();
    });
  }

// tampilkan profile
router.get('/profile/:email', (req, res) => {
    const userEmail = req.params.email;
  
    // Melakukan query ke database
    db.query(`SELECT * FROM user WHERE email = '${userEmail}';`, (error, results) => {
      if (error) {
        console.error('Error saat mengambil data profil pengguna: ', error);
        res.status(500).json({ message: 'Terjadi kesalahan saat mengambil data profil pengguna' });
      } else {
        if (results.length > 0) {
            const user = results[0];
            res.status(200).json({ profile: user });
        } else {
          // Data pengguna tidak ditemukan
          res.status(404).json({ message: 'Profil pengguna tidak ditemukan' });
        }
      }
    });
  });


  //tambahkan favorit
  app.post('/favorite-foods', verifyToken, (req, res) => {
    const { foodName, image } = req.body;
    const userId = req.userId;
  
    // Validasi input
    if (!foodName || !image) {
      return res.status(400).json({ message: 'Nama makanan dan gambar harus diisi' });
    }
   
    // Tambahkan makanan favorit ke database
    db.query('INSERT INTO favorite_foods (food_name, image, user_id) VALUES (?, ?, ?)', [foodName, image, userId], (err, result) => {
      if (err) {
        return res.status(500).json({ message: 'Terjadi kesalahan dalam menambahkan data' });
      }
  
      res.status(201).json({ message: 'Makanan favorit berhasil ditambahkan' });
    });
  });
  
  // Route untuk mendapatkan makanan favorit berdasarkan pengguna yang login
  app.get('/favorite-foods', verifyToken, (req, res) => {
    const userId = req.userId;
  
    db.query('SELECT * FROM favorite_foods WHERE user_id = ?', [userId], (err, result) => {
      if (err) {
        return res.status(500).json({ message: 'Terjadi kesalahan dalam mengambil data' });
      }
  
      const favoriteFoods = result.map(food => ({
        id: food.id,
        foodName: food.food_name,
        image: food.image,
      }));
  
      res.status(200).json({ favoriteFoods });
    });
  });  



module.exports = router;