const express = require("express");
const path = require("path");
const bodyParser = require("body-parser");
const cors = require("cors");
const router = require("./routes/router");

const app = express();

app.use(express.json());

// buat body parser
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

let corsOptions = {
    origin: "http://localhost:8081",
};
app.use(cors(corsOptions));

// buat routes
app.use("/", router);

//error
app.use((err, req, res, next) => {
    err.statusCode = err.statusCode || 500;
    err.message = err.message || "Internal server error";
    res.status(err.statusCode).json({
        message: err.message,
    });
});

// buat server 
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => console.log(`Server running at port: ${PORT}`));
