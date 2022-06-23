const db = require("../../common/services/database.service")

exports.isEmailNew = (req, res, next) => {
    let email = req.body?.email

    db.query("SELECT * FROM user where email=?", [email], (err, rows, fields) => {
        if(err) res.status(500).send({error: err})
        else {
            if(rows.length === 0){
                return next()
            }
            else {
                return res.status(400).send({ error: 'Такой e-mail уже зарегистрирован' })
            }

        }
    })
};