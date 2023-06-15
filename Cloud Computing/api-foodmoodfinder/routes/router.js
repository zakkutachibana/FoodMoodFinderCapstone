const express = require("express");
const router = express.Router();
const db = require("../config/database");
const { check, validationResult } = require('express-validator');
const { registerValidation, loginValidation } = require("../middleware/auth-validation");
var http = require("http");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");



// daftar akun (registrasi)
router.post("/register", 
    [
        check("email", "Harus diisi dengan email.").isEmail().normalizeEmail({ gmail_remove_dots: true }),
        // Tambahkan validasi format email di sini
    ],
    (req, res, next) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(400).json({
                error: true,
                msg: "Harus diisi dengan email"
            });
        }
    db.query(`SELECT * FROM user WHERE LOWER(email) = LOWER(${db.escape(req.body.email)});`, (err, result) => {
        // email sudah pernah terdaftar
        if (result.length) {
            return res.status(409).send({
                error: true,
                msg: "Akun ini sudah terdaftar, silahkan login !",
            });
        } else {
            bcrypt.hash(req.body.pass, 10, (err, hash) => {
                if (err) {
                    return res.status(500).send({
                        error:true,
                        msg: err,
                    });
                } else {
                    db.query(`INSERT INTO user (email, pass) VALUES (${db.escape(req.body.email)}, ${db.escape(hash)});`, (err, result) => {
                        if (err) {
                            throw err;
                            return res.status(400).send({
                                msg: err,
                            });
                        }
                        return res.status(201).send({
                            error: false,
                            msg: " Pendaftaran Akun Berhasil",
                        });
                    });
                }
            });
        }
    });
});

// login
router.post("/login", loginValidation, (req, res, next) => {
    db.query(`SELECT * FROM user WHERE email = ${db.escape(req.body.email)};`, (err, result) => {
        // akun belum terdaftar 
        if (err) {
            throw err;
            return res.status(400).send({
                msg: err,
            });
        }
        if (!result.length) {
            return res.status(401).send({
                error : true,
                msg: "Email atau password anda salah!",
            });
        }
        bcrypt.compare(req.body.pass, result[0]["pass"], (bErr, bResult) => {
            if (bErr) {
                throw bErr;
                return res.status(401).send({
                    msg: "Email atau password anda salah",
                });
            }
            if (bResult) {
                const token = jwt.sign({ id: result[0].id }, "foodmoodfinder-secrect");
                return res.status(200).send({
                    error : false,
                    msg: " Login Sukses",
                    token,
                    user: result[0],
                });
            }
            return res.status(401).send({
                error: true,
                msg: "Email atau password anda salah",
            });
        });
    });
});


// tampilkan profile berdasarkan email

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

 
module.exports = router;
