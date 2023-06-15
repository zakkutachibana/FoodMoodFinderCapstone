const express = require("express");
const router = express.Router();
const db = require("../config/database");
const { check, validationResult } = require('express-validator');
const { registerValidation, loginValidation } = require("../middleware/auth-validation");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");



// daftar akun (registrasi)
router.post("/register", 
    [
        check("email", "Harus diisi dengan email.").isEmail().normalizeEmail({ gmail_remove_dots: true }),
    ],
    (req, res) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(400).json({
                error: true,
                message: "Harus diisi dengan email"
            });
        }
    db.query(`SELECT * FROM user WHERE LOWER(email) = LOWER(${db.escape(req.body.email)});`, (err, result) => {
        if (result.length) {
            return res.status(409).send({
                error: true,
                massage: "Akun ini sudah terdaftar, silahkan login !",
            });
        } else {
            bcrypt.hash(req.body.pass, 10, (err, hash) => {
                if (err) {
                    return res.status(500).send({
                        error:true,
                        message: err,
                    });
                } else {
                    db.query(`INSERT INTO user (email, pass) VALUES (${db.escape(req.body.email)}, ${db.escape(hash)});`, (err, result) => {
                        if (err) {
                            throw err;
                        }
                        return res.status(201).send({
                            error: false,
                            message: " Pendaftaran Akun Berhasil",
                        });
                    });
                }
            });
        }
    });
});

// login
router.post("/login", loginValidation, (req, res) => {
    db.query(`SELECT * FROM user WHERE email = ${db.escape(req.body.email)};`, (err, result) => {
        // akun belum terdaftar 
        if (err) {
            throw err;
        }
        if (!result.length) {
            return res.status(401).send({
                error : true,
                message: "Email atau password anda salah!",
            });
        }
        bcrypt.compare(req.body.pass, result[0]["pass"], (bErr, bResult) => {
            if (bErr) {
                throw bErr;
                return res.status(401).send({
                    message: "Email atau password anda salah",
                });
            }
            if (bResult) {
                const token = jwt.sign({ id: result[0].id }, "foodmoodfinder-secrect");
                return res.status(200).send({
                    error : false,
                    message: " Login Sukses",
                    user: result[0],
                    token
                    
                });
            }
            return res.status(401).send({
                error: true,
                message: "Email atau password anda salah",
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