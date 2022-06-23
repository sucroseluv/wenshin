const db = require("../../common/services/database.service")

exports.register = (req, res) => {
    try {
        let email = req.body.email
        let password = req.body.password
        let firstname = req.body.firstname
        let lastname = req.body.lastname
        let middlename = req.body.middlename
        let phone = req.body.phone
        let date = new Date().toISOString().slice(0, 19).replace('T', ' ');

        db.query("insert into user (email, password, firstname, lastname, middlename, phone, registerdate) values (?,?,?,?,?,?,?)", 
            [email, password, firstname, lastname, middlename, phone, date], (err, rows, fields) => {
            if(err) {
                console.log("register error", err)
                return res.status(500).send({ error: 'Ошибка регистрации' })
            } 
            res.status(201).send({success: true});
        })
    }
    catch (err) {
        res.status(500, {error: err})
    }
}